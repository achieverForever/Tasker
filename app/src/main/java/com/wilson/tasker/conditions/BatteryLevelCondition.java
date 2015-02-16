package com.wilson.tasker.conditions;

import com.wilson.tasker.events.BatteryLevelEvent;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;

public class BatteryLevelCondition extends Condition {
	public enum BatteryLevelType { ABOVE, BELOW }

	public BatteryLevelType type;
	public float targetValue;

	public BatteryLevelCondition(BatteryLevelType type, float targetValue) {
		super(Event.EVENT_BATTERY_LEVEL);
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
