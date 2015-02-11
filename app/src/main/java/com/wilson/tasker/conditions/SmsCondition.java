package com.wilson.tasker.conditions;

import com.wilson.tasker.events.SmsEvent;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Task;

public class SmsCondition extends Condition {
	public String msgFrom;
	public String msgBody;

	public SmsCondition(Task task, String msgFrom, String msgBody) {
		super(Event.EVENT_SMS, task);
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
}
