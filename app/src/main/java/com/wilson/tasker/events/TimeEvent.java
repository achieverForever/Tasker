package com.wilson.tasker.events;

import com.wilson.tasker.model.Event;

public class TimeEvent extends Event {
	public static final int TYPE_START = 0;
	public static final int TYPE_END = 1;

	public int id;
	public int type;

	public TimeEvent(int id, int type) {
		super(Event.EVENT_TIME);
		this.id = id;
		this.type = type;
	}
}
