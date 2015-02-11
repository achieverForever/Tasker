package com.wilson.tasker.conditions;

import android.content.Intent;

import com.wilson.tasker.events.TopAppChangedEvent;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Task;

public class TopAppCondition extends Condition {
	public String targetPkgName;
	public Intent launchIntent;

	public TopAppCondition(Task task, String targetPkgName) {
		super(Event.EVENT_TOP_APP_CHANGED, task);
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
