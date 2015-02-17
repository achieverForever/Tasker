package com.wilson.tasker.manager;

import java.util.List;


import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.wilson.tasker.events.TopAppChangedEvent;

import de.greenrobot.event.EventBus;

public class ApplicationManager {
	private static final String TAG = "Tasker";

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

	public void getCurrTopApp() {
		String pkgName = am.getRunningTasks(1).get(0).topActivity.getPackageName();
		if (!lastPkgName.equals(pkgName)) {
			lastPkgName = pkgName;
			Intent launchIntent = pm.getLaunchIntentForPackage(pkgName);
			notifyTopAppChanged(pkgName, launchIntent);
			Log.d(TAG, "Top app changed: pkgName=" + pkgName + ", intent=" + launchIntent.toString());
		}
		Log.d(TAG, "package name: " + pkgName);
	}

	private void notifyTopAppChanged(String pkgName, Intent launchIntent) {
		EventBus.getDefault().post(new TopAppChangedEvent(pkgName, launchIntent));
	}

	public List<ApplicationInfo> getAllAppsInfo() {
		return pm.getInstalledApplications(0);
	}
}

