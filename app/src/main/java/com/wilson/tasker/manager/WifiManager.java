package com.wilson.tasker.manager;

import android.content.Context;

public class WifiManager {
	private final Context context;
	private final android.net.wifi.WifiManager wifiManager;
	private static WifiManager sInstance;

	private WifiManager(Context context) {
		this.context = context;
		this.wifiManager = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	}

	public static WifiManager getsInstance(Context context) {
		if (sInstance == null) {
			sInstance = new WifiManager(context.getApplicationContext());
		}
		return sInstance;

	}

	/**
	 * 启动/关闭Wifi
	 *
	 * @param enabled 是否开启Wifi
	 * @return 成功返回true，否则为false
	 */
	public boolean setWifiEnabled(boolean enabled) {
		return wifiManager.setWifiEnabled(enabled);
	}

	public boolean isWifiEnabled() {
		return wifiManager.isWifiEnabled();
	}

	/**
	 * 连接到指定networkId的Wifi网络
	 *
	 * @param networkId 从WifiManager.getConfiguredNetworks()获取到的networkId
	 * @return 成功返回true，否则为false
	 */
	public boolean connectToNetwork(int networkId) {
		boolean ret = true;
		synchronized (this) {
			try {
				setWifiEnabled(true);
				wait(2000);
				ret = wifiManager.disconnect();
				ret = ret & wifiManager.enableNetwork(networkId, true);
				ret = ret & wifiManager.reconnect();
			} catch (InterruptedException e) {
			}
		}
		return ret;
	}
}