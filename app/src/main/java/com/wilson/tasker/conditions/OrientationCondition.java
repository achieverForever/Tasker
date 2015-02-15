package com.wilson.tasker.conditions;

import com.wilson.tasker.events.OrientationEvent;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Scene;

public class OrientationCondition extends Condition {
	private int targetOrientation;

	public OrientationCondition(Scene scene, int targetOrientation) {
		super(Event.EVENT_ORIENTATION, scene);
		this.targetOrientation = targetOrientation;
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		OrientationEvent ev = (OrientationEvent) event;
		return ev.orientation == targetOrientation;
	}
}
