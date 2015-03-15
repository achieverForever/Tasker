package com.wilson.tasker.events;

import com.wilson.tasker.model.Event;

public class AddGeofenceEvent extends Event {
	public double longitude;
	public double latitude;
	public String geofenceId;

	public AddGeofenceEvent(double longitude, double latitude, String geofenceId) {
		super(Event.EVENT_ADD_GEOFENCE);
		this.longitude = longitude;
		this.latitude = latitude;
		this.geofenceId = geofenceId;
	}
}
