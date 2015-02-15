package com.wilson.tasker.conditions;

import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Scene;

//TODO - implement me
public class LocationCondition extends Condition {
	public LocationCondition(Scene scene) {
		super(Event.EVENT_LOCATION, scene);
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		return false;
	}
}
