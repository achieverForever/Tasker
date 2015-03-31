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

import java.util.regex.Pattern;

// CHECK
public class SmsCondition extends Condition {
	public static final String DOT_ALL = ".*";

	public String msgFromPtn;
	public String msgBodyPtn;

	public SmsCondition(String msgFromPtn, String msgBody) {
		super(Event.EVENT_SMS, "SMS", R.drawable.ic_sms, false);
		this.msgFromPtn = msgFromPtn.trim();
		this.msgBodyPtn = msgBody.trim();
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		SmsEvent ev = (SmsEvent) event;
		return Pattern.matches(DOT_ALL + msgFromPtn + DOT_ALL, ev.msgFrom)
			&& Pattern.matches(DOT_ALL + msgBodyPtn + DOT_ALL, ev.msgBody);
	}

	@Override
	public View getView(Context context, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.list_item_condition, parent, false);
		ImageView icon = (ImageView) view.findViewById(R.id.icon);
		TextView title = (TextView) view.findViewById(R.id.name);
		TextView desc = (TextView) view.findViewById(R.id.desc);

		if (iconRes != 0) {
			icon.setImageResource(iconRes);
		}
		title.setText("SMS");
		title.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		desc.setText(String.format(
				"Trigger When You Receive a SMS from \"%s\" and/or Contains \"%s\"",
				msgFromPtn, msgBodyPtn));
		desc.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		return view;
	}
}
