package com.wilson.tasker.events;

import com.wilson.tasker.model.Event;

public class OrientationEvent extends Event {
	public int orientation;

	public OrientationEvent(int orientation) {
		super(Event.EVENT_ORIENTATION);
		this.orientation = orientation;
	}
}
