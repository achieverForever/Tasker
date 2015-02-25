package com.wilson.tasker.conditions;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.wilson.tasker.events.ChargerEvent;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;

public class ChargerCondition extends Condition {
	public boolean charging;

	public ChargerCondition(boolean charging) {
		super(Event.EVENT_CHARGER, "Charger");
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
		return null;
	}
}
