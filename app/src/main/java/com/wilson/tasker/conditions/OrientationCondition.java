package com.wilson.tasker.conditions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilson.tasker.R;
import com.wilson.tasker.events.OrientationEvent;
import com.wilson.tasker.manager.FontManager;
import com.wilson.tasker.manager.OrientationManager;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;

public class OrientationCondition extends Condition {
	private int targetOrientation;

	public OrientationCondition(int targetOrientation) {
		super(Event.EVENT_ORIENTATION, "Orientation", R.drawable.icon_orientation);
		this.targetOrientation = targetOrientation;
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		OrientationEvent ev = (OrientationEvent) event;
		return ev.orientation == targetOrientation;
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
		title.setText("Orientation");
		title.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		String orientationStr = targetOrientation == OrientationManager.ORIENTATION_FACE_DOWN ?
				"down" : "up";
		desc.setText("Trigger When Your Phone is facing " + orientationStr);
		desc.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		return view;
	}
}
