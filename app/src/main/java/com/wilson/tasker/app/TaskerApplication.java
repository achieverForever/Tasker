package com.wilson.tasker.app;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.wilson.tasker.actions.BluetoothAction;
import com.wilson.tasker.conditions.TopAppCondition;
import com.wilson.tasker.manager.SceneManager;
import com.wilson.tasker.model.Action;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Scene;
import com.wilson.tasker.service.WorkerService;

import java.util.Arrays;

public class TaskerApplication extends Application {
	@Override
	public void onCreate() {
		Log.d("TaskerApplication", "onCreate()");
		super.onCreate();
		startService(new Intent(this, WorkerService.class));
		initDefaultScenes();
	}

	private void initDefaultScenes() {
		// TODO - 默认的几个Scene
		Scene batteryScene = new Scene("battery",
				Arrays.asList(new Condition[] {
						new TopAppCondition("com.android.chrome"),
				}),
				Arrays.asList(new Action[] {
						new BluetoothAction(true),
				})
		);
		SceneManager.getInstance().addScene(batteryScene, this);
	}
}
