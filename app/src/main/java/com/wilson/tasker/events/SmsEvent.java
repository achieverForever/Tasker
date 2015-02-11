package com.wilson.tasker.events;

import com.wilson.tasker.model.Event;

public class SmsEvent extends Event {
	public String msgFrom;
	public String msgBody;

	public SmsEvent(String msgFrom, String msgBody) {
		super(Event.EVENT_SMS);
		this.msgFrom = msgFrom;
		this.msgBody = msgBody;
	}
}
