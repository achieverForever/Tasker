package com.wilson.tasker.conditions;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.wilson.tasker.R;
import com.wilson.tasker.events.CallerEvent;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;


public class CallerCondition extends Condition {
	public String callerNumber;

	public CallerCondition(String callerNumber) {
		super(Event.EVENT_CALLER, "Caller", R.drawable.ic_caller);
		this.callerNumber = callerNumber;
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		CallerEvent ev = (CallerEvent) event;
		return ev.incomingNumber.equals(callerNumber);
	}

	@Override
	public View getView(Context context, ViewGroup parent) {
		return null;
	}
}
