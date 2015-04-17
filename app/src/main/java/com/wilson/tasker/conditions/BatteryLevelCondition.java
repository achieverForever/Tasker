package com.wilson.tasker.conditions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilson.tasker.R;
import com.wilson.tasker.events.BatteryLevelEvent;
import com.wilson.tasker.manager.FontManager;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;

//CHECK
public class BatteryLevelCondition extends Condition {
	public static final int TYPE_ABOVE = 0;
	public static final int TYPE_BELOW = 1;

	/** 电量类型 */
	public int type;

	/** 电量值 0.0~1.0之间 */
	public float targetValue;

	public BatteryLevelCondition(int type, float targetValue) {
		super(Event.EVENT_BATTERY_LEVEL, "Battery Level", R.drawable.ic_battery_high, true);
		this.type = type;
		this.targetValue = targetValue;
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		BatteryLevelEvent ev = (BatteryLevelEvent) event;
		if (type == TYPE_ABOVE) {
			return ev.batteryLevel > targetValue;
		} else {
			return ev.batteryLevel < targetValue;
		}
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
		title.setText("Battery Level");
		title.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		String typeStr = type == TYPE_ABOVE ? "above" : "below";
		desc.setText(String.format("Trigger When Battery Level is %s %.0f%%", typeStr, targetValue * 100));
		desc.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		return view;
	}
}
