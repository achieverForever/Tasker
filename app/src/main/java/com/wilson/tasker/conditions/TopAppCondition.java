package com.wilson.tasker.conditions;

import android.content.Intent;

import com.wilson.tasker.events.TopAppChangedEvent;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Scene;

public class TopAppCondition extends Condition {
	public String targetPkgName;
	public Intent launchIntent;

	public TopAppCondition(Scene scene, String targetPkgName) {
		super(Event.EVENT_TOP_APP_CHANGED, scene);
		this.targetPkgName = targetPkgName;
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		TopAppChangedEvent ev = (TopAppChangedEvent) event;
		if (targetPkgName.equals(ev.pkgName)) {
			this.launchIntent = ev.launchIntent;
			return true;
		} else {
			return false;
		}
	}
}
