package com.wilson.tasker.conditions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilson.tasker.R;
import com.wilson.tasker.events.TimeEvent;
import com.wilson.tasker.manager.FontManager;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;

import java.util.concurrent.atomic.AtomicInteger;

public class TimeCondition extends Condition {
	public long startMillis;
	public long endMillis;
	public int id;

	private static final AtomicInteger UNIQUE_ID_GENERATOR = new AtomicInteger();

	public TimeCondition(long startMillis, long endMillis) {
		super(Event.EVENT_TIME, "Timer", R.drawable.ic_timer, true);
		this.startMillis = startMillis;
		this.endMillis = endMillis;
		this.id = UNIQUE_ID_GENERATOR.incrementAndGet();
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		TimeEvent event1 = (TimeEvent) event;
		if (event1.id == this.id && event1.type == TimeEvent.TYPE_START) {
			return true;
		} else if (-event1.id == this.id && event1.type == TimeEvent.TYPE_END) {
			return false;
		} else {
			return false;
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
		title.setText("Timer");
		desc.setText("Trigger When the Alarm You Set is Fired");
		title.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		desc.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		return view;
	}
}
