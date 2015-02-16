package com.wilson.tasker.actions;

import android.content.Context;

import com.wilson.tasker.manager.BluetoothEnabler;
import com.wilson.tasker.model.Action;


public class BluetoothAction extends Action {
	public boolean on;

	public BluetoothAction(boolean on) {
		super("bluetooth");
		this.on = on;
	}

	@Override
	public boolean performAction(Context context) {
		if (!BluetoothEnabler.getsInstance().isBluetoothSupported()) {
			return false;
		}
		BluetoothEnabler.getsInstance().setBluetoothEnabled(on);
		return true;
	}
}
