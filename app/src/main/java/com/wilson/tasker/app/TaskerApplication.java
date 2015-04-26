package com.wilson.tasker.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.baidu.mapapi.SDKInitializer;
import com.wilson.tasker.actions.BluetoothAction;
import com.wilson.tasker.actions.BrightnessAction;
import com.wilson.tasker.conditions.BatteryLevelCondition;
import com.wilson.tasker.conditions.SmsCondition;
import com.wilson.tasker.conditions.TopAppCondition;
import com.wilson.tasker.dao.DaoMaster;
import com.wilson.tasker.dao.DaoSession;
import com.wilson.tasker.manager.SceneManager;
import com.wilson.tasker.model.Action;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Scene;
import com.wilson.tasker.service.WorkerService;
import com.wilson.tasker.utils.Utils;

public class TaskerApplication extends Application {

	private static DaoMaster daoMaster;
	private static DaoSession daoSession;

	@Override
	public void onCreate() {
		super.onCreate();

		// 初始化百度地图SDK
		SDKInitializer.initialize(this);
		startService(new Intent(this, WorkerService.class));

		//test();
		if (Utils.isFreshInstall(this)) {
			initDefaultScenes();
		} else {
			SceneManager.getInstance()
					.loadScenes(this, getSharedPreferences(WorkerService.SHARED_PREF_NAME, 0));
		}

	}

	private void initDefaultScenes() {
		// TODO - 默认的几个Scene
//		batteryConditions.add(new TopAppCondition("com.android.chrome"));
//		batteryConditions.add(new BatteryLevelCondition(BatteryLevelCondition.BatteryLevelType.ABOVE, 0.8f));
//		batteryConditions.add(new TopAppCondition("com.android.chrome"));
//		batteryConditions.add(new CallerCondition("123"));

		Scene batteryScene = new Scene("battery", "Battery", false);
		batteryScene.addCondition(new SmsCondition("10657301", "hi"));
//		batteryScene.addCondition(new CallerCondition("26888"));
//		batteryScene.addAction(new BluetoothAction(true));
//		batteryScene.addAction(new WifiAction(true));

		Scene homeScene = new Scene("home", "Home", false);
		Scene sleepScene = new Scene("sleep", "Sleep", false);

		SceneManager.getInstance().addScene(this, batteryScene);
		SceneManager.getInstance().addScene(this, homeScene);
		SceneManager.getInstance().addScene(this, sleepScene);
		SceneManager.getInstance()
				.saveScenes(getSharedPreferences(WorkerService.SHARED_PREF_NAME, 0));

	}

	private void test() {

		BatteryLevelCondition condition = new BatteryLevelCondition(BatteryLevelCondition.TYPE_ABOVE, 100);
		String streamCondition = Utils.GSON.toJson(condition, Condition.class);
		BatteryLevelCondition condition2 = (BatteryLevelCondition) Utils.GSON.fromJson(streamCondition, Condition.class);

		BrightnessAction action = new BrightnessAction(100);
		String streamAction = Utils.GSON.toJson(action, Action.class);
		BrightnessAction action2 = (BrightnessAction) Utils.GSON.fromJson(streamAction, Action.class);

		Scene batteryScene = new Scene("battery", "Battery", false);
		batteryScene.addCondition(new TopAppCondition("com.android.chrome"));
		batteryScene.addAction(new BluetoothAction(true));
		String streamScene = Utils.GSON.toJson(batteryScene, Scene.class);
		Scene scene2 = Utils.GSON.fromJson(streamScene, Scene.class);

	}

	public static DaoMaster getDaoMaster(Context context)
	{
		if (daoMaster == null) {
			DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "timeline-db", null);
			daoMaster = new DaoMaster(helper.getWritableDatabase());
		}
		return daoMaster;
	}

	public static DaoSession getDaoSession(Context context)
	{
		if (daoSession == null) {
			if (daoMaster == null) {
				daoMaster = getDaoMaster(context);
			}
			daoSession = daoMaster.newSession();
		}
		return daoSession;
	}

}
