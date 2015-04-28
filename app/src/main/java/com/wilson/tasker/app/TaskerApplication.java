package com.wilson.tasker.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import com.baidu.mapapi.SDKInitializer;
import com.wilson.tasker.actions.BluetoothAction;
import com.wilson.tasker.actions.BrightnessAction;
import com.wilson.tasker.actions.RingerModeAction;
import com.wilson.tasker.actions.WifiAction;
import com.wilson.tasker.actions.WifiConnectAction;
import com.wilson.tasker.conditions.BatteryLevelCondition;
import com.wilson.tasker.conditions.LocationCondition;
import com.wilson.tasker.conditions.OrientationCondition;
import com.wilson.tasker.dao.DaoMaster;
import com.wilson.tasker.dao.DaoSession;
import com.wilson.tasker.manager.OrientationManager;
import com.wilson.tasker.manager.SceneManager;
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

		if (Utils.isFreshInstall(this)) {
			initDefaultScenes();
		} else {
			SceneManager.getInstance().loadScenes(this,
					getSharedPreferences(WorkerService.SHARED_PREF_NAME, 0));
		}

	}

	private void initDefaultScenes() {
		Scene battery = new Scene("Battery", true);
		battery.addCondition(new BatteryLevelCondition(BatteryLevelCondition.TYPE_BELOW, 0.2f));
		battery.addAction(new BluetoothAction(false));
		battery.addAction(new BrightnessAction(64));
		battery.addAction(new WifiAction(false));

		Scene home = new Scene("Home", true);
		home.addCondition(new LocationCondition("Home"));
		home.addAction(new WifiConnectAction(0, "home"));
		home.addAction(new BrightnessAction(194));

		Scene meeting = new Scene("Meeting", true);
		meeting.addCondition(new OrientationCondition(OrientationManager.ORIENTATION_FACE_DOWN));
		meeting.addAction(new RingerModeAction(AudioManager.RINGER_MODE_SILENT));

		SceneManager.getInstance().addScene(this, battery);
		SceneManager.getInstance().addScene(this, home);
		SceneManager.getInstance().addScene(this, meeting);
		SceneManager.getInstance().saveScenes(getSharedPreferences(
				WorkerService.SHARED_PREF_NAME, 0));

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
