package com.wilson.tasker.events;

import android.content.Intent;

import com.wilson.tasker.model.Event;

public class TopAppChangedEvent extends Event {
	public String pkgName;
	public Intent launchIntent;

	public TopAppChangedEvent(String pkgName, Intent launchIntent) {
		super(Event.EVENT_TOP_APP_CHANGED);
		this.pkgName = pkgName;
		this.launchIntent = launchIntent;
	}
}
