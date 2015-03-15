package com.wilson.tasker.events;

import com.wilson.tasker.model.Event;

public class LocationEvent extends Event {
	public static final int GEOFENCE_ENTER = 0;
	public static final int GEOFENCE_EXIT = 1;

	public String geofencId;
	public int state;

	public LocationEvent(String geofencId, int state) {
		super(Event.EVENT_LOCATION);
		this.geofencId = geofencId;
		this.state = state;
	}
}
