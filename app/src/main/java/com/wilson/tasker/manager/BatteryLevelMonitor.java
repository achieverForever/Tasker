package com.wilson.tasker.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

import com.wilson.tasker.events.ChargerEvent;

import de.greenrobot.event.EventBus;

//CHECK
public class BatteryLevelMonitor {
	public static final String TAG = "BatteryLevelMonitor";

	/** Context对象 */
	private Context context;

	/** 是否已经注册了电量变化BroadcastReceiver */
	private boolean isRegistered;

	/** 单例 */
	private static BatteryLevelMonitor sInstance;

	/** 是否在充电 */
	private boolean isCharging;

	private BatteryLevelMonitor(Context context) {
		this.context = context;
	}

	public static synchronized BatteryLevelMonitor getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new BatteryLevelMonitor(context.getApplicationContext());
		}
		return sInstance;
	}

	/**
	 * 不注册BroadcastReceiver，直接通过Sticky Intent获取最新的电量水平
	 *
	 * @return 当前的电量，0.0~1.0之间
	 */
	public float getCurrentBatteryLevel() {
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent intent = context.registerReceiver(null, intentFilter);
		int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		float batteryPct = level / (float) scale;
		return batteryPct;
	}

	public void register() {
		if (!isRegistered) {
			IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
			context.registerReceiver(chargerStateReceiver, intentFilter);
			isRegistered = true;
		}
	}

	public void unregister() {
		if (isRegistered) {
			context.unregisterReceiver(chargerStateReceiver);
			isRegistered = false;
		}
	}

	private BroadcastReceiver chargerStateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
			boolean isChargingNow= (status == BatteryManager.BATTERY_STATUS_CHARGING ||
					status == BatteryManager.BATTERY_STATUS_FULL);
			if (isChargingNow != isCharging) {
				isCharging = isChargingNow;
				Log.d("BatteryLevelMonitor", "isCharing=" + isCharging);
				EventBus.getDefault().post(new ChargerEvent(isCharging));
			}
		}
	};

	public boolean isRegistered() {
		return isRegistered;
	}

}
