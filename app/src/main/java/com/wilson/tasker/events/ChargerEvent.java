package com.wilson.tasker.events;

import com.wilson.tasker.model.Event;

public class ChargerEvent extends Event {
	public boolean isCharging;

	public ChargerEvent(boolean isCharging) {
		super(Event.EVENT_CHARGER);
		this.isCharging = isCharging;
	}
}
