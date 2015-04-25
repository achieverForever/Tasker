package com.wilson.tasker.manager;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

/**
 * 开启/关闭蓝牙
 */
public final class BluetoothEnabler {
	private final BluetoothAdapter localAdapter;
	private static BluetoothEnabler sInstance;

	private BluetoothEnabler() {
		// Note: getDefaultAdapter返回null时，表示不支持蓝牙
		localAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	public static synchronized BluetoothEnabler getsInstance() {
		if (sInstance == null) {
			sInstance = new BluetoothEnabler();
		}
		return sInstance;
	}

	/**
	 * 设置蓝牙的开/关
	 *
	 * @param enabled True为启动，false为关闭
	 * @return 成功返回true，失败返回false
	 */
	public boolean setBluetoothEnabled(boolean enabled) {
		if (localAdapter != null) {
			if (enabled) {
				return localAdapter.enable();
			} else {
				return localAdapter.disable();
			}
		}
		return false;
	}

	public boolean isBluetoothEnabled() {
		if (localAdapter != null) {
			return localAdapter.isEnabled();
		}
		return false;
	}

	/**
	 * 获取设备是否支持蓝牙
	 *
	 * @return True为支持，false为不支持
	 */
	public boolean isBluetoothSupported() {
		return localAdapter != null;
	}
}
