package com.wilson.tasker.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

import com.wilson.tasker.conditions.BatteryLevelCondition;
import com.wilson.tasker.events.BatteryLevelEvent;
import com.wilson.tasker.events.ChargerEvent;

import de.greenrobot.event.EventBus;

public class BatteryLevelMonitor {
	private Context context;
	private boolean registered;
	private static BatteryLevelMonitor sInstance;

	private BatteryLevelMonitor(Context context) {
		this.context = context;
	}

	public static synchronized BatteryLevelMonitor getsInstance(Context context) {
		if (sInstance == null) {
			sInstance = new BatteryLevelMonitor(context);
		}
		return sInstance;
	}

	public float getCurrentBatteryLevel() {
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent intent = context.registerReceiver(null, intentFilter);
		int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		float batteryPct = level / (float)scale;
		return batteryPct;
	}

	public void register() {
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		context.registerReceiver(chargingStateReceiver, intentFilter);
		registered = true;
	}

	public void unregister() {
		context.unregisterReceiver(chargingStateReceiver);
		registered = false;
	}

	private BroadcastReceiver chargingStateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
			boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
					status == BatteryManager.BATTERY_STATUS_FULL;
			Log.d("Tasker", "isCharing=" + isCharging);
			EventBus.getDefault().post(new ChargerEvent(isCharging));
		}
	};

}
