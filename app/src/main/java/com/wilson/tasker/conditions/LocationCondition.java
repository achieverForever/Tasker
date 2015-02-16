package com.wilson.tasker.conditions;

import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Scene;

//TODO - implement me
public class LocationCondition extends Condition {
	public LocationCondition() {
		super(Event.EVENT_LOCATION);
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		return false;
	}
}
