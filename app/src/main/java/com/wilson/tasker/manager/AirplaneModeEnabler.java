package com.wilson.tasker.manager;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 开启/关闭飞行模式
 */
public class AirplaneModeEnabler {

	private Context context;
	private static AirplaneModeEnabler sInstance;

	private AirplaneModeEnabler(Context context) {
		this.context = context;
	}

	public static synchronized AirplaneModeEnabler getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new AirplaneModeEnabler(context.getApplicationContext());
		}
		return sInstance;
	}

	@SuppressWarnings("deprecation")
	public boolean isAirplaneModeOn() {
		return Settings.System.getInt(context.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, 0) != 0;
	}

	@SuppressWarnings("deprecation")
	public void setAirplaneModeOn(boolean enable) {
		// TODO - 以下方法在Android 4.2+行不通
		Settings.System.putInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON,
				enable ? 1 : 0);
		Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		intent.putExtra("state", enable);
		context.sendBroadcast(intent);

//		try {
//			String line;
//			StringBuilder log = new StringBuilder();
//			if (enable) {
//				Process process = Runtime.getRuntime().exec("settings put global airplane_mode_on 1".split(" "));
//				Log.d(Utils.LOG_TAG, "exec output: " + getProcessOutput(process));
//				Process process2 = Runtime.getRuntime().exec("am broadcast -a android.intent.action.AIRPLANE_MODE --ez state true".split(" "));
//				Log.d(Utils.LOG_TAG, "exec output: " + getProcessOutput(process2));
//			} else {
//				Process process = Runtime.getRuntime().exec("settings put global airplane_mode_on 0".split(" "));
//				Log.d(Utils.LOG_TAG, "exec output: " + getProcessOutput(process));
//				Process process2 = Runtime.getRuntime().exec("am broadcast -a android.intent.action.AIRPLANE_MODE --ez state false".split(" "));
//				Log.d(Utils.LOG_TAG, "exec output: " + getProcessOutput(process2));
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	private String getProcessOutput(Process process) {
		String line;
		StringBuilder log = new StringBuilder();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		try {
			while ((line = bufferedReader.readLine()) != null) {
				log.append(line + "\n");
			}
		} catch (IOException e) {
		}
		return log.toString();
	}
}
