package com.wilson.tasker.conditions;

import android.content.Context;
import android.util.Log;
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
import com.wilson.tasker.utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


// CHECK
public class CallerCondition extends Condition {

	public String callerNumber;

	public CallerCondition(String callerNumber) {
		super(Event.EVENT_CALLER, "Caller", R.drawable.ic_caller, false);
		this.callerNumber = callerNumber.trim();
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		CallerEvent ev = (CallerEvent) event;
		String cleanNumber = Utils.cleanNumber(callerNumber);
		return Pattern.matches(Utils.DOT_ALL + cleanNumber + Utils.DOT_ALL, ev.incomingNumber);
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
		title.setText("Phone Call");
		title.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		desc.setText("Trigger When Caller's Number contains " + callerNumber);
		desc.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		return view;
	}
}
