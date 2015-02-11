package com.wilson.tasker.events;

import com.wilson.tasker.model.Event;

public class CallerEvent extends Event {
	public String incomingNumber;

	public CallerEvent(String incomingNumber) {
		super(Event.EVENT_CALLER);
		this.incomingNumber = incomingNumber;
	}
}
