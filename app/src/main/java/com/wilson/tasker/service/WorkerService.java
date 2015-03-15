package com.wilson.tasker.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDGeofence;
import com.baidu.location.BDLocationStatusCodes;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.wilson.tasker.events.AddGeofenceEvent;
import com.wilson.tasker.events.BatteryLevelEvent;
import com.wilson.tasker.events.LocationEvent;
import com.wilson.tasker.events.SceneActivatedEvent;
import com.wilson.tasker.events.SceneDeactivatedEvent;
import com.wilson.tasker.manager.ApplicationManager;
import com.wilson.tasker.manager.BatteryLevelMonitor;
import com.wilson.tasker.manager.SceneManager;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Scene;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.greenrobot.event.EventBus;

public class WorkerService extends Service {
	private static final String TAG = "WorkerService";
	public static final int HOUR = 60 * 60 * 1000;

	private static final int START_ID_SCHEDULE_JOBS = 0;
	private static final int START_ID_GPS = 1;
	private static final int SECOND = 1000;
	private static final int SCHEDULE_INTERVAL = 10 * SECOND;
	private static final int REQUEST_LOCATION_INTERVAL = 5 * 60 * SECOND;

	// 需要定时轮询的条件
	private static final Set<Integer> PASSIVE_POLLING_CONDITIONS
			= new HashSet<>(Arrays.asList(new Integer[]{
		Event.EVENT_BATTERY_LEVEL, Event.EVENT_TOP_APP_CHANGED
	}));


	private static LocationClient locationClient;
	private static GeofenceClient geofenceClient;

	private Handler handler;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate()");
		// 开启一个后台线程执行定时任务
		HandlerThread thread = new HandlerThread("worker_thread", Process.THREAD_PRIORITY_DEFAULT);
		thread.start();
		handler = new Handler(thread.getLooper());
		EventBus.getDefault().register(this);
		scheduleAlarms();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand()");
		if (intent == null) {
			Log.d(TAG, "service recreated.");
		}
		Runnable runnable = null;
		if (startId == START_ID_SCHEDULE_JOBS) {
			runnable = new Runnable() {
				@Override
				public void run() {
					runScheduledJobs();
				}
			};
		} else if (startId == START_ID_GPS) {
			runnable = new Runnable() {
				@Override
				public void run() {
					getLocationClient(WorkerService.this).requestLocation();
				}
			};
		}

		if (runnable != null) {
			handler.post(runnable);
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy()");
		// 关闭定位
		stopLocationClient();
		// 关闭Geofence检测
		stopGeofenceClient();
		EventBus.getDefault().unregister(this);
	}

	public void onEvent(final Event event) {
		Log.d(TAG, "onEvent [" + Event.eventCodeToString(event.eventCode) + "]");

		// Post到后台线程进行消息处理
		handler.post(new Runnable() {
			@Override
			public void run() {
				switch (event.eventCode) {
					case Event.EVENT_SCENE_ACTIVATED:
						// 处理Scene激活事件
						SceneManager.getInstance()
							.handleSceneActivated(WorkerService.this,
								((SceneActivatedEvent) event).scene);
						return;
					case Event.EVENT_SCENE_DEACTIVATED:
						// 处理Scene非激活事件
						SceneManager.getInstance()
							.handleSceneDeactivated(((SceneDeactivatedEvent) event).scene);
						return;
					case Event.EVENT_ADD_GEOFENCE:
						handleAddGeofenceEvent((AddGeofenceEvent) event);
					default:
						break;
				}
				List<Scene> interestedScenes
					= SceneManager.getInstance().findScenesByEvent(event.eventCode);
				if (interestedScenes.size() <= 0) {
					return;
				}
				for (Scene s : interestedScenes) {
					if (s.state == Scene.STATE_DISABLED) {
						continue;
					}
					s.dispatchEvent(event);
				}
			}
		});
	}

	/**
	 * 调度执行Jobs
	 */
	private void runScheduledJobs() {
		Log.d(TAG, "runScheduledJobs()");
		Toast.makeText(WorkerService.this, "runScheduledJobs()", Toast.LENGTH_SHORT).show();
//		checkPassiveConditions();
	}

	/**
	 * 部分Condition不是订阅/发布模式的，如电量、当前APP，需要周期性轮询
	 */
	private void checkPassiveConditions() {
		for (Scene s : SceneManager.getInstance().getScenes()) {
			if (s.state != Scene.STATE_DISABLED) {
				for (Condition c : s.conditions) {
					if (PASSIVE_POLLING_CONDITIONS.contains(c.eventCode)) {
						if (c.eventCode == Event.EVENT_BATTERY_LEVEL) {
							float currBatteryLevel
								= BatteryLevelMonitor.getInstance(this).getCurrentBatteryLevel();
							EventBus.getDefault().post(new BatteryLevelEvent(currBatteryLevel));
						} else if (c.eventCode == Event.EVENT_TOP_APP_CHANGED) {
							ApplicationManager.getInstance(this).getCurrTopApp();
						}
					}
				}
			}
		}
	}

	/**
	 * 利用AlarmManager调度执行
	 */
	private void scheduleAlarms() {
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		// 定时任务唤醒Alarm
		Intent intent = new Intent(this, WorkerService.class);
		PendingIntent pi = PendingIntent.getService(this, START_ID_SCHEDULE_JOBS, intent, 0);
		alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, SCHEDULE_INTERVAL, pi);

		// GPS定位唤醒Alarm
		PendingIntent pi2 = PendingIntent.getService(this, START_ID_GPS, intent, 0);
		alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, REQUEST_LOCATION_INTERVAL, pi2);
	}

	private void handleAddGeofenceEvent(AddGeofenceEvent event) {
		BDGeofence geoFence = new BDGeofence.Builder()
			.setGeofenceId(event.geofenceId)
			.setCoordType(BDGeofence.COORD_TYPE_BD09LL)
			.setCircularRegion(event.longitude, event.latitude, BDGeofence.RADIUS_TYPE_SMALL)
			.setExpirationDruation(12 * HOUR)
			.build();

		GeofenceClient.OnAddBDGeofencesResultListener addGeofenceResultListener = new GeofenceClient.OnAddBDGeofencesResultListener() {
			@Override
			public void onAddBDGeofencesResult(int statusCode, String geofenceId) {
				if (statusCode == BDLocationStatusCodes.SUCCESS) {
					Log.d(TAG, String.format("add geofence[id=%s] success", geofenceId));
				} else {
					Log.e(TAG, String.format("add geofence[id=%s] fail, statusCode=%d", geofenceId, statusCode));
				}
			}
		};

		GeofenceClient.OnGeofenceTriggerListener geofenceTriggerListener
			= new GeofenceClient.OnGeofenceTriggerListener() {
			@Override
			public void onGeofenceExit(String geofenceId) {
				EventBus.getDefault().post(new LocationEvent(geofenceId, LocationEvent.GEOFENCE_EXIT));
			}

			@Override
			public void onGeofenceTrigger(String geofenceId) {
				EventBus.getDefault().post(new LocationEvent(geofenceId, LocationEvent.GEOFENCE_ENTER));
			}
		};

		getGeofenceClient(this).addBDGeofence(geoFence, addGeofenceResultListener);
		getGeofenceClient(this).registerGeofenceTriggerListener(geofenceTriggerListener);
	}

	public static synchronized LocationClient getLocationClient(Context context) {
		if (locationClient == null) {
			locationClient = new LocationClient(context.getApplicationContext());
		}
		return locationClient;
	}

	public static synchronized GeofenceClient getGeofenceClient(Context context) {
		if (geofenceClient == null) {
			geofenceClient = new GeofenceClient(context.getApplicationContext());
		}
		return geofenceClient;
	}

	private void stopLocationClient() {
		if (getLocationClient(this).isStarted()) {
			getLocationClient(this).stop();
		}
	}

	private void stopGeofenceClient() {
		if (getGeofenceClient(this).isStarted()) {
			getGeofenceClient(this).stop();
		}
	}
}
