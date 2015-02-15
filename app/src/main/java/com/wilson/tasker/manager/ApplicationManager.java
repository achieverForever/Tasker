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
	
	private ActivityManager mAM;
	private PackageManager mPM;
	private Context mContext;
	private boolean mStartTracking = false;
	
	private Runnable mTrackRunnable;

	private String mLastTopApp;
	
	private Object mLock = new Object();
	
	private ApplicationManager(Context context) {
		mContext = context;
		mAM = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		mPM = context.getPackageManager();
		mTrackRunnable = new Runnable() {
			@Override
			public void run() {
				getCurrTopApp();
			}
		};
		mLastTopApp= "";
	}
	
	public static synchronized ApplicationManager getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new ApplicationManager(context);
		}
		return sInstance;
	}
	
	public void startTracking() {
		synchronized (mLock) {
			if (!mStartTracking) {
				Log.d(TAG, "startTracking");
				mStartTracking = true;
				JobScheduler.getInstance().scheduleAtFixRate(mTrackRunnable, 3, TimeUnit.SECONDS);
			}
		}
	}
	
	public void stopTracking() {
		Log.d(TAG, mTrackRunnable.toString());
		synchronized (mLock) {
			if (mStartTracking) {
				mStartTracking = false;
				JobScheduler.getInstance().removeCallback(mTrackRunnable);
			}
		}
	}
	
	public void getCurrTopApp() {
		Log.d(TAG, "getCurrTopApp");
		String pkgName = mAM.getRunningTasks(1).get(0).topActivity.getPackageName();

		if (!mLastTopApp.equals(pkgName)) {
			mLastTopApp= pkgName;

			Intent launchIntent = mPM.getLaunchIntentForPackage(pkgName);
			notifyTopAppChanged(pkgName, launchIntent);
			Log.d(TAG, "Top app changed: pkgName=" + pkgName + ", intent=" + launchIntent.toString());
		}
		ComponentName component = mAM.getRunningTasks(1).get(0).topActivity;
		Log.d(TAG, "top activity: " + component.getClassName());
	}
	
	private void notifyTopAppChanged(String pkgName, Intent intent) {
		EventBus.getDefault().post(new TopAppChangedEvent(pkgName, intent));
	}
	
	public List<ApplicationInfo> getAllAppsInfo() {
		return mPM.getInstalledApplications(0);
	}
}

