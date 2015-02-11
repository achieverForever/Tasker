package com.wilson.tasker.conditions;

import com.wilson.tasker.events.ChargerEvent;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Task;

public class ChargerCondition extends Condition {
	public boolean charging;

	public ChargerCondition(Task task, boolean charging) {
		super(Event.EVENT_CHARGER, task);
		this.charging = charging;
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		ChargerEvent ev = (ChargerEvent) event;
		return ev.isCharging == charging;
	}
}
