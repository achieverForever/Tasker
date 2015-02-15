package com.wilson.tasker.conditions;

import com.wilson.tasker.events.ChargerEvent;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Scene;

public class ChargerCondition extends Condition {
	public boolean charging;

	public ChargerCondition(Scene scene, boolean charging) {
		super(Event.EVENT_CHARGER, scene);
		this.charging = charging;
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		ChargerEvent ev = (ChargerEvent) event;
		return ev.isCharging == charging;
	}
}
