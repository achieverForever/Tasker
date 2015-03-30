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

import java.util.TreeMap;

//CHECK
public class BatteryLevelCondition extends Condition {
	public enum BatteryLevelType {ABOVE, BELOW}

	/** 电量类型 */
	public BatteryLevelType type;

	/** 电量值 0.0~1.0之间 */
	public float targetValue;

	public BatteryLevelCondition(BatteryLevelType type, float targetValue) {
		super(Event.EVENT_BATTERY_LEVEL, "Battery Level", R.drawable.ic_battery_high, true);
		this.type = type;
		this.targetValue = targetValue;
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		BatteryLevelEvent ev = (BatteryLevelEvent) event;
		if (type == BatteryLevelType.ABOVE) {
			return ev.batteryLevel > targetValue;
		} else {
			return ev.batteryLevel < targetValue;
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
		title.setText("Battery Level");
		title.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		String typeStr = type == BatteryLevelType.ABOVE ? "above" : "below";
		desc.setText(String.format("Trigger When Battery Level is %s %.0f%%", typeStr, targetValue * 100));
		desc.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		return view;
	}
}
