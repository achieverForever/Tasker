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

import com.wilson.tasker.events.BatteryLevelEvent;
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
	private static final int SECOND = 1000;
	private static final int SCHEDULE_INTERVAL = 10 * SECOND;
	private static final int REQUEST_LOCATION_INTERVAL = 60 * SECOND;

	// 需要定时轮询的条件
	private static final Set<Integer> PASSIVE_POLLING_CONDITIONS
		= new HashSet<>(Arrays.asList(new Integer[]{
		Event.EVENT_BATTERY_LEVEL, Event.EVENT_TOP_APP_CHANGED
	}));

	private ServiceHandler handler;

	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			sendMessageDelayed(obtainMessage(1), 10000);
			runScheduledJobs();
		}
	}

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
		handler = new ServiceHandler(thread.getLooper());
		EventBus.getDefault().register(this);
		scheduleAlarms();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand()");
		if (intent == null) {
			Log.d(TAG, "service recreated.");
		}
//		handler.removeMessages(1);
//		handler.sendEmptyMessageDelayed(1, 10000);
		runScheduledJobs();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy()");
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
							.handleSceneActivated(WorkerService.this, ((SceneActivatedEvent) event).scene);
						return;
					case Event.EVENT_SCENE_DEACTIVATED:
						// 处理Scene非激活事件
						SceneManager.getInstance()
							.handleSceneDeactivated(((SceneDeactivatedEvent) event).scene);
						return;
					default:
						break;
				}
				List<Scene> interestedScenes = SceneManager.getInstance().findScenesByEvent(event.eventCode);
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
							float currBatteryLevel = BatteryLevelMonitor.getInstance(this).getCurrentBatteryLevel();
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
		Intent intent = new Intent(this, WorkerService.class);
		PendingIntent pi = PendingIntent.getService(this, 0, intent, 0);
		alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, SCHEDULE_INTERVAL, pi);
	}
}
