package com.wilson.tasker.conditions;

import com.wilson.tasker.events.CallerEvent;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Scene;


public class CallerCondition extends Condition {
	public String callerNumber;

	public CallerCondition(Scene scene, String callerNumber) {
		super(Event.EVENT_CALLER, scene);
		this.callerNumber = callerNumber;
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		CallerEvent ev = (CallerEvent) event;
		return ev.incomingNumber.equals(callerNumber);
	}
}
