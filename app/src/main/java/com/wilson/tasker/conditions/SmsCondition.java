package com.wilson.tasker.conditions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilson.tasker.R;
import com.wilson.tasker.events.SmsEvent;
import com.wilson.tasker.manager.FontManager;
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
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.list_item_condition, parent, false);
		ImageView icon = (ImageView) view.findViewById(R.id.iv_icon);
		TextView title = (TextView) view.findViewById(R.id.tv_title);
		TextView desc = (TextView) view.findViewById(R.id.tv_desc);

		if (iconRes != 0) {
			icon.setImageResource(iconRes);
		}
		title.setText("SMS");
		title.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		desc.setText("Trigger When You Receive a SMS from Specified Contact and/or " +
			"Contains Specified Content");
		desc.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		return view;
	}
}
