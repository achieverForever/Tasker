package com.wilson.tasker.app;

import android.app.Application;
import android.util.Log;

import com.wilson.tasker.actions.BluetoothAction;
import com.wilson.tasker.conditions.TopAppCondition;
import com.wilson.tasker.manager.SceneManager;
import com.wilson.tasker.model.Action;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskerApplication extends Application {
	@Override
	public void onCreate() {
		Log.d("TaskerApplication", "onCreate()");
		super.onCreate();
//		startService(new Intent(this, WorkerService.class));
		initDefaultScenes();
	}

	private void initDefaultScenes() {
		// TODO - 默认的几个Scene
		List<Condition> emptyConditions = new ArrayList<>();
		List<Action> emptyActions = new ArrayList<>();
		Scene batteryScene = new Scene("battery", "Battery",
			Arrays.asList(new Condition[]{
				new TopAppCondition("com.android.chrome"),
			}),
			Arrays.asList(new Action[]{
				new BluetoothAction(true),
			})
		);
		Scene homeScene = new Scene("home", "Home", emptyConditions, emptyActions);
		Scene sleepScene = new Scene("sleep", "Sleep", emptyConditions, emptyActions);
		SceneManager.getInstance().addScene(batteryScene, this);
		SceneManager.getInstance().addScene(homeScene, this);
		SceneManager.getInstance().addScene(sleepScene, this);
	}
}
