package com.wilson.tasker.model;

import java.util.HashMap;
import java.util.Map;

public class Action {
	public static final int ACTION_UNKNOWN = -1;
	public static final int ACTION_AIRPLANE_MODE = 1;
	public static final int ACTION_APPLICATION = 2;
	public static final int ACTION_BLUETOOTH = 3;
	public static final int ACTION_DISPLAY = 4;
	public static final int ACTION_RINGTONE = 5;
	public static final int ACTION_NOTIFICATION = 6;
	public static final int ACTION_RINGER_MODE = 7;
	public static final int ACTION_WIFI = 8;

	public int actionCode;
	public Map<String, String> params;

	public Action(int actionCode) {
		this.actionCode = actionCode;
		params = new HashMap<String, String>();
	}
}
