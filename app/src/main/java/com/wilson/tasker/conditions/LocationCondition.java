package com.wilson.tasker.conditions;

import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Task;

//TODO - implement me
public class LocationCondition extends Condition {
	public LocationCondition(Task task) {
		super(Event.EVENT_LOCATION, task);
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		return false;
	}
}
