package com.wilson.tasker.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

import com.baidu.location.BDGeofence;
import com.baidu.location.BDLocationStatusCodes;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.wilson.tasker.events.AddGeofenceEvent;
import com.wilson.tasker.events.BatteryLevelEvent;
import com.wilson.tasker.events.LocationEvent;
import com.wilson.tasker.events.SceneActivatedEvent;
import com.wilson.tasker.events.SceneDeactivatedEvent;
import com.wilson.tasker.manager.ApplicationManager;
import com.wilson.tasker.manager.BatteryLevelMonitor;
import com.wilson.tasker.manager.LocationManager;
import com.wilson.tasker.manager.SceneManager;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;

//TODO - 将定位相关代码从Service中剥离
public class WorkerService extends Service {
	public static final String TAG = "WorkerService";

	public static final int SECOND = 1000;
	public static final int MINUTE = 60 * SECOND;
	public static final int HOUR = 60 * MINUTE;

	/**
	 * 定时任务时间间隔
	 */
	private static final int SCHEDULE_INTERVAL = 10 * SECOND;

	/**
	 * 调度获取地理位置更新时间间隔
	 */
	private static final int REQUEST_LOCATION_INTERVAL = 60 * SECOND;

	/**
	 * 用于串行化执行任务的Handler
	 */
	private Handler handler;

	/**
	 * 需要定时轮询的条件
	 */
	private static final Set<Integer> PASSIVE_POLLING_CONDITIONS
			= new HashSet<>(Arrays.asList(new Integer[]{
			Event.EVENT_BATTERY_LEVEL,
			Event.EVENT_TOP_APP_CHANGED,
			Event.EVENT_LOCATION,
	}));

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.d(TAG, String.format("onCreate: myPid=%d, myTid=%d, myUid=%d",
				Process.myPid(), Process.myTid(), Process.myUid()));

		// 开启一个后台线程执行定时任务
		HandlerThread thread = new HandlerThread("worker_thread", Process.THREAD_PRIORITY_DEFAULT);
		thread.start();
		handler = new Handler(thread.getLooper());

		scheduleSelf();
		EventBus.getDefault().register(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent == null) {
			Log.d(TAG, "service recreated.");
		}
		Log.d(TAG, String.format("onStartCommand: myPid=%d, myTid=%d, myUid=%d",
				Process.myPid(), Process.myTid(), Process.myUid()));

		handler.post(new Runnable() {
			@Override
			public void run() {
				checkConditions();
			}
		});

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");

		EventBus.getDefault().unregister(this);
	}

	public void onEvent(final Event event) {
		Log.d(TAG, "onEvent [" + Event.eventCodeToString(event.eventCode) + "]");

		// Post到后台线程进行消息处理
		handler.post(new Runnable() {
			@Override
			public void run() {
				switch (event.eventCode) {
					case Event.EVENT_SCENE_ACTIVATED: {
						// 处理Scene激活事件
						SceneManager.getInstance()
								.handleSceneActivated(WorkerService.this,
										((SceneActivatedEvent) event).scene);
						return;
					}
					case Event.EVENT_SCENE_DEACTIVATED: {
						// 处理Scene非激活事件
						SceneManager.getInstance()
								.handleSceneDeactivated(WorkerService.this,
										((SceneDeactivatedEvent) event).scene);
						return;
					}
					case Event.EVENT_ADD_GEOFENCE: {
						// 处理新增地理围栏事件
						LocationManager.getInstance(WorkerService.this)
							.handleAddGeofenceEvent((AddGeofenceEvent) event);
						return;
					}
					default:
						break;
				}
				List<Scene> interestedScenes
						= SceneManager.getInstance().findScenesByEvent(event.eventCode);
				if (interestedScenes.size() <= 0) {
					return;
				}
				for (Scene s : interestedScenes) {
					if (s.getState() == Scene.STATE_DISABLED) {
						continue;
					}
					// 将事件分发到对应的Scene进行处理
					s.dispatchEvent(event);
				}
			}
		});
	}

	/**
	 * 检测非订阅/发布模式的事件，如电量、当前APP是否满足，若有满足条件的Scene，马上执行对应的Actions
	 */
	private void checkConditions() {
		Map<Integer, List<Scene>> scenesByEventType = new HashMap<>();
		for (Scene scene : SceneManager.getInstance().getScenes()) {
			if (scene.getState() != Scene.STATE_DISABLED) {
				for (Condition condition : scene.getConditions()) {
					if (PASSIVE_POLLING_CONDITIONS.contains(condition.eventCode)) {
						if (scenesByEventType.get(condition.eventCode) == null) {
							scenesByEventType.put(condition.eventCode, new ArrayList<Scene>());
						}
						scenesByEventType.get(condition.eventCode).add(scene);
					}
				}
			}
		}

		for (int eventType : PASSIVE_POLLING_CONDITIONS) {
			if (scenesByEventType.get(eventType) != null
					&& scenesByEventType.get(eventType).size() > 0) {
				postEvent(eventType);
			}
		}
	}

	private void postEvent(int eventType) {
		switch (eventType) {
			case Event.EVENT_BATTERY_LEVEL: {
				// 获取当前电量水平
				float currBatteryLevel
						= BatteryLevelMonitor.getInstance(this).getCurrentBatteryLevel();
				EventBus.getDefault().post(new BatteryLevelEvent(currBatteryLevel));
			} break;

			case Event.EVENT_TOP_APP_CHANGED: {
				// 检测Top App
				ApplicationManager.getInstance(this).checkTopApp();
			} break;

			case Event.EVENT_LOCATION: {
				// 获取地理位置更新
				LocationManager.getInstance(this).requestLocation();
			} break;

			default:
				break;
		}
	}

	/**
	 * 利用AlarmManager调度执行定时任务
	 */
	private void scheduleSelf() {
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(this, WorkerService.class);
		PendingIntent pi = PendingIntent.getService(this, 0, intent, 0);
//		alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SCHEDULE_INTERVAL, pi);
		alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SCHEDULE_INTERVAL, SCHEDULE_INTERVAL, pi);
	}
}
