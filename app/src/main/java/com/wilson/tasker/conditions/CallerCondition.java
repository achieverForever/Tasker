package com.wilson.tasker.conditions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilson.tasker.R;
import com.wilson.tasker.events.CallerEvent;
import com.wilson.tasker.manager.FontManager;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class CallerCondition extends Condition {
	public static final String DOT_ALL = ".*";

	public String callerNumberPtn;

	public CallerCondition(String callerNumberPtn) {
		super(Event.EVENT_CALLER, "Caller", R.drawable.ic_caller, false);
		this.callerNumberPtn = callerNumberPtn.trim();
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		CallerEvent ev = (CallerEvent) event;
		return Pattern.matches(DOT_ALL + callerNumberPtn + DOT_ALL, ev.incomingNumber);
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
		title.setText("Phone Call");
		title.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		desc.setText("Trigger When Caller's Number contains " + callerNumberPtn);
		desc.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		return view;
	}
}
