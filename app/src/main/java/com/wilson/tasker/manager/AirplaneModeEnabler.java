package com.wilson.tasker.manager;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

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
        Settings.System.putInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON,
                                enable ? 1 : 0);
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.putExtra("state", enable);
        context.sendBroadcast(intent);
    }
}
