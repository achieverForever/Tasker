package com.wilson.tasker.manager;

import java.util.List;
import java.util.concurrent.TimeUnit;


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
	
	/**
	 * 检测顶层App的时间间隔，毫秒为单位
	 */
	private static final long TRACK_INTERVAL = 10000;
	
	private static ApplicationManager sInstance; 
	
	private ActivityManager am;
	private PackageManager pm;
	private boolean isTracking = false;
	private Runnable trackRunnable;
	private String lastTopApp;
	private Object lock = new Object();
	
	private ApplicationManager(Context context) {
		this.am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		this.pm = context.getPackageManager();
		this.trackRunnable = new Runnable() {
			@Override
			public void run() {
				getCurrTopApp();
			}
		};
		this.lastTopApp = "";
	}
	
	public static synchronized ApplicationManager getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new ApplicationManager(context.getApplicationContext());
		}
		return sInstance;
	}
	
	public void startTracking() {
		synchronized (lock) {
			if (!isTracking) {
				Log.d(TAG, "startTracking");
				isTracking = true;
				JobScheduler.getInstance().scheduleAtFixRate(trackRunnable, 3, TimeUnit.SECONDS);
			}
		}
	}
	
	public void stopTracking() {
		Log.d(TAG, trackRunnable.toString());
		synchronized (lock) {
			if (isTracking) {
				isTracking = false;
				JobScheduler.getInstance().removeCallback(trackRunnable);
			}
		}
	}
	
	public void getCurrTopApp() {
		Log.d(TAG, "getCurrTopApp");
		String pkgName = am.getRunningTasks(1).get(0).topActivity.getPackageName();

		if (!lastTopApp.equals(pkgName)) {
			lastTopApp = pkgName;
			Intent launchIntent = pm.getLaunchIntentForPackage(pkgName);
			notifyTopAppChanged(pkgName, launchIntent);
			Log.d(TAG, "Top app changed: pkgName=" + pkgName + ", intent=" + launchIntent.toString());
		}
		ComponentName component = am.getRunningTasks(1).get(0).topActivity;
		Log.d(TAG, "top activity: " + component.getClassName());
	}
	
	private void notifyTopAppChanged(String pkgName, Intent intent) {
		EventBus.getDefault().post(new TopAppChangedEvent(pkgName, intent));
	}
	
	public List<ApplicationInfo> getAllAppsInfo() {
		return pm.getInstalledApplications(0);
	}
}

