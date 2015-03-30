package com.wilson.tasker.manager;

import android.content.Context;
import android.util.Log;

import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Scene;
import com.wilson.tasker.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneManager {
	public static final String KEY_SCENES = "scenes";

	/** 单例 */
	private static SceneManager sInstance = new SceneManager();

	/** 所有Scene的列表，访问锁保护 */
	private ArrayList<Scene> scenes;

	public static SceneManager getInstance() {
		return sInstance;
	}

	protected SceneManager() {
		scenes = new ArrayList<>(5);
	}

	public synchronized List<Scene> findScenesByEvent(int eventCode) {
		List<Scene> result = new ArrayList<>();
		for (Scene s : scenes) {
			final List<Condition> conditions = s.getConditions();
			for (Condition c : conditions) {
				if (c.eventCode == eventCode && result.indexOf(s) == -1) {
					result.add(s);
				}
			}
		}
		return result;
	}

	public boolean handleSceneActivated(Context context, Scene scene) {
		boolean success = scene.runScene(context);
		if (success) {
			Log.d(Utils.LOG_TAG, "runScene [" + scene.toString() + "] succeeded.");
		} else {
			Log.d(Utils.LOG_TAG, "runScene [" + scene.toString() + "] failed.");
		}
		// 恢复Scene的状态为ENABLED
		scene.setState(Scene.STATE_ENABLED);
		return success;
	}

	public void handleSceneDeactivated(Context context, Scene scene) {
		// TODO - implements me
		scene.setState(Scene.STATE_ENABLED);
	}

	public synchronized void addScene(Context context, Scene scene) {
		scenes.add(scene);
		registerManager(context, scene.getConditions());
	}

	public synchronized void removeScene(Context context, Scene scene) {
		scenes.remove(scene);
		unregisterManager(context, scene.getConditions());
	}

	public synchronized void changeScene(Context context, Scene oldScene, Scene newScene) {
		removeScene(context, oldScene);
		addScene(context, newScene);
	}

	/**
	 * 动态的按需启动各种系统设置的Manager，以减少资源占用
	 *
	 * @param context Context对象
	 * @param conditions 新增加的Conditions
	 */
	private void registerManager(Context context, List<Condition> conditions) {
		for (Condition c : conditions) {
			switch (c.eventCode) {
				case Event.EVENT_BATTERY_LEVEL:
					if (!BatteryLevelMonitor.getInstance(context).isRegistered()) {
						BatteryLevelMonitor.getInstance(context).register();
					}
					break;
				case Event.EVENT_CALLER:
					if (!PhoneCallManager.getInstance(context).isRegistered()) {
						PhoneCallManager.getInstance(context).register();
					}
					break;
				case Event.EVENT_CHARGER:
					if (!BatteryLevelMonitor.getInstance(context).isRegistered()) {
						BatteryLevelMonitor.getInstance(context).register();
					}
					break;
				case Event.EVENT_LOCATION:
					// TODO - implement me
					break;
				case Event.EVENT_ORIENTATION:
					if (!OrientationManager.getInstance(context).isRegistered()) {
						OrientationManager.getInstance(context).register();
					}
					break;
				case Event.EVENT_TIME:
					break;
				case Event.EVENT_SMS:
					if (!SmsManager.getInstance(context).isRegistered()) {
						SmsManager.getInstance(context).register();
					}
					break;
				case Event.EVENT_TOP_APP_CHANGED:
					break;
			}
		}
	}

	private void unregisterManager(Context context, List<Condition> conditions) {
		// 找出不再被引用的Manager
		Map<Integer, Boolean> registrationMap = new HashMap<>();
		for (Condition condition : Condition.asList()) {
			registrationMap.put(condition.eventCode, false);
		}

		for (Scene scene : SceneManager.getInstance().getScenes()) {
			if (scene.getState() == Scene.STATE_ENABLED) {
				for (Condition condition : scene.getConditions()) {
					if (!registrationMap.get(condition.eventCode)) {
						registrationMap.put(condition.eventCode, true);
					}
				}
			}
		}

		for (Map.Entry<Integer, Boolean> entry : registrationMap.entrySet()) {
			if (!entry.getValue()) {
				final int eventCode = entry.getKey();
				doUnregisterManager(context, eventCode);
			}
		}
	}

	private void doUnregisterManager(Context context, int eventCode) {
		switch (eventCode) {
			case Event.EVENT_CALLER:
				if (PhoneCallManager.getInstance(context).isRegistered()) {
					PhoneCallManager.getInstance(context).unregister();
					Log.d(Utils.LOG_TAG, "unregister PhoneCallManager");
				}
				break;
			case Event.EVENT_CHARGER:
				if (BatteryLevelMonitor.getInstance(context).isRegistered()) {
					BatteryLevelMonitor.getInstance(context).unregister();
					Log.d(Utils.LOG_TAG, "unregister BatteryLevelMonitor");
				}
				break;
			case Event.EVENT_LOCATION:
				// TODO - implement me
				break;
			case Event.EVENT_ORIENTATION:
				if (OrientationManager.getInstance(context).isRegistered()) {
					OrientationManager.getInstance(context).unregister();
					Log.d(Utils.LOG_TAG, "unregister OrientationManager");
				}
				break;
			case Event.EVENT_SMS:
				if (SmsManager.getInstance(context).isRegistered()) {
					SmsManager.getInstance(context).unregister();
					Log.d(Utils.LOG_TAG, "unregister SmsManager");
				}
				break;

			// 以下事件无需反注册
			case Event.EVENT_TOP_APP_CHANGED:
			case Event.EVENT_TIME:
			case Event.EVENT_BATTERY_LEVEL:
				break;

			default:
				break;
		}
	}

	public ArrayList<Scene> getScenes() {
		return scenes;
	}

}
