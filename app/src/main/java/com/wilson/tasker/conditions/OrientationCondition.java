package com.wilson.tasker.conditions;

import com.wilson.tasker.events.OrientationEvent;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Task;

public class OrientationCondition extends Condition {
	private int targetOrientation;

	public OrientationCondition(Task task, int targetOrientation) {
		super(Event.EVENT_ORIENTATION, task);
		this.targetOrientation = targetOrientation;
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		OrientationEvent ev = (OrientationEvent) event;
		return ev.orientation == targetOrientation;
	}
}
