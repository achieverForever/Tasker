package com.wilson.tasker.conditions;

import com.wilson.tasker.events.BatteryLevelEvent;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Scene;

public class BatteryLevelCondition extends Condition {
	public enum BatteryLevelType { ABOVE, BELOW }

	public BatteryLevelType type;
	public float targetValue;

	public BatteryLevelCondition(Scene scene, BatteryLevelType type, float targetValue) {
		super(Event.EVENT_BATTERY_LEVEL, scene);
		this.type = type;
		this.targetValue = targetValue;
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		BatteryLevelEvent ev = (BatteryLevelEvent) event;
		if (type == BatteryLevelType.ABOVE) {
			return ev.batteryLevel > targetValue;
		} else {
			return ev.batteryLevel < targetValue;
		}
	}
}
