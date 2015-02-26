package com.wilson.tasker.conditions;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.wilson.tasker.R;
import com.wilson.tasker.events.OrientationEvent;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;

public class OrientationCondition extends Condition {
	private int targetOrientation;

	public OrientationCondition(int targetOrientation) {
		super(Event.EVENT_ORIENTATION, "Orientation", R.drawable.icon_orientation);
		this.targetOrientation = targetOrientation;
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		OrientationEvent ev = (OrientationEvent) event;
		return ev.orientation == targetOrientation;
	}

	@Override
	public View getView(Context context, ViewGroup parent) {
		return null;
	}
}
