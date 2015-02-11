package com.wilson.tasker.events;

import com.wilson.tasker.model.Event;

public class BatteryLevelEvent extends Event {
	/**
	 * 当前电量水平，0.0~1.0
	 */
	public float batteryLevel;

	public BatteryLevelEvent(float batteryLevel) {
		super(Event.EVENT_BATTERY_LEVEL);
		this.batteryLevel = batteryLevel;
	}
}
