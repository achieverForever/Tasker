package com.wilson.tasker.conditions;

import com.wilson.tasker.events.TimeEvent;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Scene;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;

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

}
