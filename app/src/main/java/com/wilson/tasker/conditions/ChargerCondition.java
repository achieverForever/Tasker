package com.wilson.tasker.conditions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilson.tasker.R;
import com.wilson.tasker.events.ChargerEvent;
import com.wilson.tasker.manager.FontManager;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;

public class ChargerCondition extends Condition {
	public boolean charging;

	public ChargerCondition(boolean charging) {
		super(Event.EVENT_CHARGER, "Charger", R.drawable.ic_charger);
		this.charging = charging;
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		ChargerEvent ev = (ChargerEvent) event;
		return ev.isCharging == charging;
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
		title.setText("Charging");
		title.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		String typeStr = charging ? "" : "not ";
		desc.setText("Trigger When Your Phone is " + typeStr + "Charging");
		desc.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		return view;
	}
}
