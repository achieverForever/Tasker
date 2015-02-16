package com.wilson.tasker.conditions;

import android.os.Handler;
import android.os.Looper;

import com.wilson.tasker.events.TimeEvent;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.Calendar;

public class TimeConditionTest extends TestCase {

	private TimeCondition timeCondition;

	public void testperformCheckEvent() throws Exception {
		final Handler h = new Handler(Looper.getMainLooper());
		long now = Calendar.getInstance().getTimeInMillis();
		timeCondition = new TimeCondition(now + 1000, now + 2000);

		final TimeEvent event = new TimeEvent();
		h.postDelayed(new Runnable() {
			@Override
			public void run() {
				Assert.assertTrue(timeCondition.performCheckEvent(event));
			}
		}, 1500);
		h.postDelayed(new Runnable() {
			@Override
			public void run() {
				Assert.assertTrue(timeCondition.performCheckEvent(event));
			}
		}, 1000);
		h.postDelayed(new Runnable() {
			@Override
			public void run() {
				Assert.assertFalse(timeCondition.performCheckEvent(event));
			}
		}, 2001);

		Assert.assertFalse(timeCondition.performCheckEvent(event));
	}
}