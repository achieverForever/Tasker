package com.wilson.tasker.actions;

import android.content.Context;

import com.wilson.tasker.manager.AirplaneModeEnabler;
import com.wilson.tasker.model.Action;

public class AirplaneModeAction extends Action {
	public boolean on;

	public AirplaneModeAction(boolean on) {
		super("airplane_mode");
		this.on = on;
	}

	@Override
	public boolean performAction(Context context) {
		if (on != AirplaneModeEnabler.getInstance(context).isAirplaneModeOn()) {
			AirplaneModeEnabler.getInstance(context).setAirplaneModeOn(on);
		}
		return true;
	}
}
