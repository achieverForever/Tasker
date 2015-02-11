package com.wilson.tasker.conditions;

import com.wilson.tasker.events.CallerEvent;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Task;

public class CallerCondition extends Condition {
	public String callerNumber;

	public CallerCondition(Task task, String callerNumber) {
		super(Event.EVENT_CALLER, task);
		this.callerNumber = callerNumber;
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		CallerEvent ev = (CallerEvent) event;
		return ev.incomingNumber.equals(callerNumber);
	}
}
