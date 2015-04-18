package com.wilson.tasker.manager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.wilson.tasker.events.TopAppChangedEvent;
import com.wilson.tasker.utils.Utils;

import de.greenrobot.event.EventBus;

//CHECK
public class ApplicationManager {

	private static ApplicationManager sInstance;
	private ActivityManager am;
	private PackageManager pm;
	private String lastPkgName;

	private ApplicationManager(Context context) {
		this.am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		this.pm = context.getPackageManager();
		this.lastPkgName = "";
	}

	public static synchronized ApplicationManager getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new ApplicationManager(context.getApplicationContext());
		}
		return sInstance;
	}

	public boolean checkTopApp() {
		String pkgName = am.getRunningTasks(1).get(0).topActivity.getPackageName();
		Log.d(Utils.LOG_TAG, "package name: " + pkgName);
		if (!lastPkgName.equals(pkgName)) {
			lastPkgName = pkgName;
			Intent launchIntent = pm.getLaunchIntentForPackage(pkgName);
			notifyTopAppChanged(pkgName, launchIntent);
			Log.d(Utils.LOG_TAG, "Top app changed: pkgName=" + pkgName);
			return true;
		} else {
			return false;
		}
	}

	private void notifyTopAppChanged(String pkgName, Intent launchIntent) {
		EventBus.getDefault().post(new TopAppChangedEvent(pkgName, launchIntent));
	}
}

