package com.wilson.tasker.model;

public abstract class Event {
	public static final int EVENT_UNKNOWN = -1;
	public static final int EVENT_RUN_SCENE = 64;
	public static final int EVENT_SCENE_ACTIVATED = 127;
	public static final int EVENT_SCENE_DEACTIVATED = 128;
	public static final int EVENT_BATTERY_LEVEL = 1;
	public static final int EVENT_CALLER = 2;
	public static final int EVENT_CHARGER = 3;
	public static final int EVENT_LOCATION = 4;
	public static final int EVENT_ORIENTATION = 5;
	public static final int EVENT_TIME = 6;
	public static final int EVENT_SMS = 7;
	public static final int EVENT_TOP_APP_CHANGED = 8;

	public int eventCode = EVENT_UNKNOWN;

	public Event(int eventCode) {
		this.eventCode = eventCode;
	}

	public static String eventCodeToString(int eventCode) {
		switch (eventCode) {
			case 0:
				return "EVENT_SCENE_DEACTIVATED";
			case 1:
				return "EVENT_BATTERY_LEVEL";
			case 2:
				return "EVENT_CALLER";
			case 3:
				return "EVENT_CHARGER";
			case 4:
				return "EVENT_LOCATION";
			case 5:
				return "EVENT_ORIENTATION";
			case 6:
				return "EVENT_TIME";
			case 7:
				return "EVENT_SMS";
			case 8:
				return "EVENT_TOP_APP_CHANGED";
			case 64:
				return "EVENT_RUN_SCENE";
			default:
				return "EVENT_UNKNOWN";
		}
	}
}
