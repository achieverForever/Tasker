package com.wilson.tasker.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.wilson.tasker.app.TaskerApplication;
import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Scene;
import com.wilson.tasker.service.WorkerService;
import com.wilson.tasker.utils.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneManager {
	public static final String PREF_KEY_SCENES = "com.wilson.tasker.scenes";

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
		Toast.makeText(context, String.format("Scene %s is activated", scene.getName()),
				Toast.LENGTH_SHORT).show();
		return success;
	}

	public void handleSceneDeactivated(Context context, Scene scene) {
		scene.setState(Scene.STATE_ENABLED);
		if (scene.isRollbackNeeded()) {
			scene.rollback(context);
		}
		Toast.makeText(context, String.format("Scene %s is deactivated", scene.getName()),
				Toast.LENGTH_SHORT).show();
	}

	public void handleSceneChanged(Context context, List<Condition> removedConditions,
	                               List<Condition> newConditions) {
		// 反注册删除的Condition相关的Manager，减少资源占用
		SceneManager.getInstance().unregisterManager(context, removedConditions);
		// 为新增的Condition重新注册Manager
		SceneManager.getInstance().registerManager(context, newConditions);
		// 保存所有序列化后的Scenes到SharedPrefs
		SceneManager.getInstance()
				.saveScenes(context.getSharedPreferences(WorkerService.SHARED_PREF_NAME, 0));
	}

	public synchronized void addScene(Context context, Scene scene) {
		scenes.add(scene);
		if (scene.getState() != Scene.STATE_DISABLED) {
			registerManager(context, scene.getConditions());
		}
	}

	public synchronized void removeScene(Context context, Scene scene) {
		scenes.remove(scene);
		unregisterManager(context, scene.getConditions());
	}

	/**
	 * 动态的按需启动各种系统设置的Manager，以减少资源占用
	 *
	 * @param context Context对象
	 * @param conditions 新增加的Conditions
	 */
	public synchronized void registerManager(Context context, List<Condition> conditions) {
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

	public synchronized void unregisterManager(Context context, List<Condition> conditions) {
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
				}
				break;
			case Event.EVENT_CHARGER:
				if (BatteryLevelMonitor.getInstance(context).isRegistered()) {
					BatteryLevelMonitor.getInstance(context).unregister();
				}
				break;
			case Event.EVENT_LOCATION:
				// TODO - implement me
				break;
			case Event.EVENT_ORIENTATION:
				if (OrientationManager.getInstance(context).isRegistered()) {
					OrientationManager.getInstance(context).unregister();
				}
				break;
			case Event.EVENT_SMS:
				if (SmsManager.getInstance(context).isRegistered()) {
					SmsManager.getInstance(context).unregister();
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

	public synchronized void saveScenes(SharedPreferences sharedPrefs) {
		List<String> flattenScenes = new ArrayList<>();
		for (int i = 0; i < scenes.size(); i++) {
			flattenScenes.add(Utils.GSON.toJson(scenes.get(i), Scene.class));
		}
		Log.d(Utils.LOG_TAG, String.format("saveScenes: %d scenes", flattenScenes.size()));
		Log.d(Utils.LOG_TAG, "serialized scenes:" + flattenScenes);
		sharedPrefs.edit().putString(PREF_KEY_SCENES, Utils.GSON.toJson(flattenScenes)).commit();
	}

	public synchronized void loadScenes(Context context, SharedPreferences sharedPreferences) {
		scenes.clear();
		Type listType = new TypeToken<ArrayList<String>>() {}.getType();
		List<String> serializedScenes = Utils.GSON.fromJson(sharedPreferences
				.getString(PREF_KEY_SCENES, ""), listType);
		for (String serializedScene : serializedScenes) {
			Scene scene = Utils.GSON.fromJson(serializedScene, Scene.class);
			addScene(context, scene);
		}
		Log.d(Utils.LOG_TAG, String.format("loadScenes: %d scenes", serializedScenes.size()));
	}

}
