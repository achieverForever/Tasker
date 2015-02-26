package com.wilson.tasker.conditions;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.wilson.tasker.R;
import com.wilson.tasker.events.SmsEvent;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;

public class SmsCondition extends Condition {
	public String msgFrom;
	public String msgBody;

	public SmsCondition(String msgFrom, String msgBody) {
		super(Event.EVENT_SMS, "SMS", R.drawable.ic_sms);
		this.msgFrom = msgFrom.trim();
		this.msgBody = msgBody.trim();
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		SmsEvent ev = (SmsEvent) event;
		if (msgFrom.equals("*")) {
			return msgBody.equals(ev.msgBody.trim());
		} else {
			return msgFrom.equals(ev.msgFrom.trim()) && msgBody.equals(ev.msgBody.trim());
		}
	}

	@Override
	public View getView(Context context, ViewGroup parent) {
		return null;
	}
}
