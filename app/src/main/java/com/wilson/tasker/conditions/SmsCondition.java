package com.wilson.tasker.conditions;

import android.content.Context;
import android.util.Log;
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
import com.wilson.tasker.utils.Utils;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class SmsCondition extends Condition {
	public static final String DOT_ALL = ".*";

	public String msgFromPtn;
	public String msgBodyPtn;

	public SmsCondition(String msgFromPtn, String msgBody) {
		super(Event.EVENT_SMS, "SMS", R.drawable.ic_sms, false);
		this.msgFromPtn = DOT_ALL + msgFromPtn.trim() + DOT_ALL;
		this.msgBodyPtn = DOT_ALL + msgBody.trim() + DOT_ALL;
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		SmsEvent ev = (SmsEvent) event;
		return Pattern.matches(msgFromPtn, ev.msgFrom)
			&& Pattern.matches(msgBodyPtn, ev.msgBody);
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
