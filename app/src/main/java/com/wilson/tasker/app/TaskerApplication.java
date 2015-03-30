package com.wilson.tasker.app;

import android.app.Application;
import android.content.Intent;
import android.os.*;
import android.os.Process;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wilson.tasker.actions.BluetoothAction;
import com.wilson.tasker.actions.BrightnessAction;
import com.wilson.tasker.actions.TestAction;
import com.wilson.tasker.actions.WifiAction;
import com.wilson.tasker.conditions.BatteryLevelCondition;
import com.wilson.tasker.conditions.CallerCondition;
import com.wilson.tasker.conditions.SmsCondition;
import com.wilson.tasker.conditions.TopAppCondition;
import com.wilson.tasker.events.BatteryLevelEvent;
import com.wilson.tasker.manager.SceneManager;
import com.wilson.tasker.model.Action;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Scene;
import com.wilson.tasker.service.WorkerService;
import com.wilson.tasker.utils.ActionTypeAdapter;
import com.wilson.tasker.utils.ConditionTypeAdapter;
import com.wilson.tasker.utils.SceneTypeAdapter;
import com.wilson.tasker.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskerApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// 初始化百度地图SDK
		SDKInitializer.initialize(this);
		startService(new Intent(this, WorkerService.class));
		initDefaultScenes();

		test();
	}

	private void initDefaultScenes() {
		// TODO - 默认的几个Scene
//		batteryConditions.add(new TopAppCondition("com.android.chrome"));
//		batteryConditions.add(new BatteryLevelCondition(BatteryLevelCondition.BatteryLevelType.ABOVE, 0.8f));
//		batteryConditions.add(new TopAppCondition("com.android.chrome"));
//		batteryConditions.add(new CallerCondition("123"));

		Scene batteryScene = new Scene("battery", "Battery", false);
//		batteryScene.addCondition(new SmsCondition("123456", "hello"));
		batteryScene.addCondition(new CallerCondition("26888"));
//		batteryScene.addAction(new BluetoothAction(true));
//		batteryScene.addAction(new WifiAction(true));
		batteryScene.addAction(new TestAction());

		Scene homeScene = new Scene("home", "Home", false);
		Scene sleepScene = new Scene("sleep", "Sleep", false);

		SceneManager.getInstance().addScene(this, batteryScene);
	}

	private void test() {

		BatteryLevelCondition condition = new BatteryLevelCondition(BatteryLevelCondition.BatteryLevelType.ABOVE, 100);
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

}
