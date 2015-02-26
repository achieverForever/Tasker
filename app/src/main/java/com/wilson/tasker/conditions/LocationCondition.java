package com.wilson.tasker.conditions;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.wilson.tasker.R;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;

//TODO - implement me
public class LocationCondition extends Condition {
	public LocationCondition() {
		super(Event.EVENT_LOCATION, "Location", R.drawable.icon_location);
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		return false;
	}

	@Override
	public View getView(Context context, ViewGroup parent) {
		return null;
	}
}
