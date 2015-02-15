package com.wilson.tasker.manager;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import com.wilson.tasker.model.Condition;

/**
 * 开启/关闭蓝牙
 */
public final class BluetoothEnabler {
    private final Context mContext;
    private final BluetoothAdapter mLocalAdapter;
	private static BluetoothEnabler sInstance;

    private BluetoothEnabler(Context context) {
        mContext = context;
        // Note: getDefaultAdapter返回null时，表示不支持蓝牙
        mLocalAdapter = BluetoothAdapter.getDefaultAdapter();
    }

	public static synchronized BluetoothEnabler getsInstance(Context context) {
		if (sInstance == null) {
			sInstance = new BluetoothEnabler(context);
		}
		return sInstance;
	}

    /**
     * 设置蓝牙的开/关
     * 
     * @param enabled True为启动，false为关闭
     * @return True如果操作成功，false如果有错误发生
     */
    public boolean setBluetoothEnabled(boolean enabled) {
		if (mLocalAdapter != null) {
			if (enabled) {
				return mLocalAdapter.enable();
			} else {
				return mLocalAdapter.disable();
			}
		}
		return false;
	}

    /**
     * 获取设备是否支持蓝牙
     * 
     * @return True为支持，false为不支持
     */
	public boolean isBluetoothSupported() {
		return mLocalAdapter != null;
	}
}
