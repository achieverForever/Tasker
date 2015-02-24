package com.wilson.tasker.conditions;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;

import java.util.Calendar;

public class TimeCondition extends Condition {
	public long start;
	public long end;

	public TimeCondition(long start, long end) {
		super(Event.EVENT_TIME);
		long now = Calendar.getInstance().getTimeInMillis();
		if (start < now || end < start) {
			throw new IllegalArgumentException("Invalid value of TimeCondition");
		}
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		//TODO - do we really need this?
		long currTime = Calendar.getInstance().getTimeInMillis();
		return currTime >= start && currTime <= end;
	}

	@Override
	public View getView(Context context, ViewGroup parent) {
		return null;
	}
}
