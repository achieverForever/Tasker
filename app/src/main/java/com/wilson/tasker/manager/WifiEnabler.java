package com.wilson.tasker.manager;

import android.content.Context;
import android.net.wifi.WifiManager;

/**
 * 开启/关闭Wifi
 */
public class WifiEnabler {
    private final Context mContext;
    private final WifiManager mWifiManager;
	private static WifiEnabler sInstance;

    private WifiEnabler(Context context) {
        mContext = context;
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

	public static WifiEnabler getsInstance(Context context) {
		if (sInstance == null) {
			sInstance = new WifiEnabler(context.getApplicationContext());
		}
		return sInstance;

	}

    /**
     * 启动/关闭Wifi
     * 
     * @param
     * @return True如果操作成功，false如果发生错误
     */
 	public boolean setWifiEnabled(boolean enabled) {
 		return mWifiManager.setWifiEnabled(true);
 	}
}