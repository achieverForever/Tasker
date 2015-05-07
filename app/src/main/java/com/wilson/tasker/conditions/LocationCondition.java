package com.wilson.tasker.conditions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilson.tasker.R;
import com.wilson.tasker.events.LocationEvent;
import com.wilson.tasker.manager.FontManager;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;

public class LocationCondition extends Condition {
	public String geofencId;

	public LocationCondition(String geofencId) {
		super(Event.EVENT_LOCATION, "Location", R.drawable.ic_location, true);
		this.geofencId = geofencId;
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		LocationEvent ev = (LocationEvent) event;
		return ev.geofencId == geofencId && ev.state == LocationEvent.GEOFENCE_ENTER;
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
		title.setText("Location");
		title.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		desc.setText("Trigger When You're near " + geofencId);
		desc.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		return view;
	}
}
