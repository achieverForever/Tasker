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
import com.wilson.tasker.actions.BluetoothAction;
import com.wilson.tasker.conditions.BatteryLevelCondition;
import com.wilson.tasker.conditions.ChargerCondition;
import com.wilson.tasker.conditions.SmsCondition;
import com.wilson.tasker.conditions.TopAppCondition;
import com.wilson.tasker.manager.SceneManager;
import com.wilson.tasker.model.Action;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Scene;
import com.wilson.tasker.service.WorkerService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskerApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("TaskerApplication", String.format("onCreate: myPid=%d, myTid=%d, myUid=%d",
				android.os.Process.myPid(), Process.myTid(), Process.myUid()));

		// 初始化百度地图SDK
		SDKInitializer.initialize(this);
		startService(new Intent(this, WorkerService.class));
		initDefaultScenes();
	}

	private void initDefaultScenes() {
		// TODO - 默认的几个Scene
		List<Condition> emptyConditions = new ArrayList<>();
		List<Action> emptyActions = new ArrayList<>();

		List<Condition> batteryConditions = new ArrayList<>();
		List<Action> batteryActions = new ArrayList<>();
//		batteryConditions.add(new TopAppCondition("com.android.chrome"));
//		batteryConditions.add(new BatteryLevelCondition(BatteryLevelCondition.BatteryLevelType.ABOVE, 0.8f));
//		batteryConditions.add(new ChargerCondition(true));
		batteryConditions.add(new SmsCondition("111", "hello"));
		batteryActions.add(new BluetoothAction(true));

		Scene batteryScene = new Scene("battery", "Battery", batteryConditions, batteryActions);
		Scene homeScene = new Scene("home", "Home", emptyConditions, emptyActions);
		Scene sleepScene = new Scene("sleep", "Sleep", emptyConditions, emptyActions);

		SceneManager.getInstance().addScene(this, batteryScene);
		SceneManager.getInstance().addScene(this, homeScene);
		SceneManager.getInstance().addScene(this, sleepScene);
	}


}
