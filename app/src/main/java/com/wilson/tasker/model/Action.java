package com.wilson.tasker.model;

import android.content.Context;

import com.wilson.tasker.manager.AirplaneModeEnabler;

import java.util.HashMap;
import java.util.Map;

public abstract class Action {
	public static final int STATE_IDLE = 0;
	public static final int STATE_RUNNING = 1;
	public static final int STATE_COMPLETED = 2;

	public String type;
	public int state;

	public Action(String type) {
		this.type = type;
		this.state = STATE_IDLE;
	}

	public abstract boolean performAction(Context context);
}
