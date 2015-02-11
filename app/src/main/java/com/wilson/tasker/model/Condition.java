package com.wilson.tasker.model;


import android.graphics.drawable.Drawable;

import com.wilson.tasker.R;
import com.wilson.tasker.adapters.ConditionListAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class Condition {
	public boolean isSatisfied;
	public int eventCode;
	public ConditionStateChangedListener listener;
	public Drawable actionIcon;
	public String desc;

	/*private static final Map<Integer, Map<Boolean, Integer>> STATE_TRANSITION_MAP;
	static {
		STATE_TRANSITION_MAP = new HashMap<Integer, Map<Boolean, Integer>>() {{
			put(STATE_UNSATISFIED, new HashMap<Boolean, Integer>() {{
				put(true, STATE_UNSATISFIED_TO_SATISFIED);
				put(false, -1);     // -1表示无操作
			}});
		}};

	}

	*//**
	 * 条件尚未满足
	 *//*
	public static final int STATE_UNSATISFIED = 1;

	*//**
	 * 条件由不满足变为满足
	 *//*
	public static final int STATE_UNSATISFIED_TO_SATISFIED = 4;

	*//**
	 * 条件由满足变为不满足
	 *//*
	public static final int STATE_SATISFIED_TO_UNSATISFIED = 8;*/

	protected Condition(int eventCode, ConditionStateChangedListener listener) {
		this.listener = listener;
		this.isSatisfied = false;
		this.eventCode = eventCode;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Condition)) {
			return false;
		}
		return ((Condition) o).eventCode == this.eventCode;
	}

	public void doCheckEvent(Event event) {
		boolean satisfied = performCheckEvent(event);
		// 更新条件的状态
		if (satisfied != this.isSatisfied) {
			this.isSatisfied = satisfied;
			listener.onConditionStateChanged(this, satisfied);
		}
	}

	public boolean performCheckEvent(Event event) {
		validateEvent(event);
		return false;
	};

	private void validateEvent(Event event) {
		if (event.eventCode != this.eventCode) {
			throw new IllegalStateException("Event type not match");
		}
	}

	public interface ConditionStateChangedListener {
		public void onConditionStateChanged(Condition condition, boolean satisfied);
	}

	public static String getConditionName(int eventCode) {
		switch (eventCode) {
			case 1:
				return "Battery";
			case 2:
				return "Caller";
			case 3:
				return "Charger";
			case 4:
				return "Location";
			case 5:
				return "Orientation";
			case 6:
				return "Time";
			case 7:
				return "Sms";
			case 8:
				return "Top Application";
			default:
				return "Unknown";
		}
	}

	public static int getConditionIcon(int eventCode) {
		return R.drawable.ic_charger;
	}

	public static List<ConditionListAdapter.ConditionItem> asList() {
		List<ConditionListAdapter.ConditionItem> items = new ArrayList<ConditionListAdapter.ConditionItem>();
		items.add(new ConditionListAdapter.ConditionItem(Event.EVENT_BATTERY_LEVEL));
		items.add(new ConditionListAdapter.ConditionItem(Event.EVENT_CALLER));
		items.add(new ConditionListAdapter.ConditionItem(Event.EVENT_CHARGER));
		items.add(new ConditionListAdapter.ConditionItem(Event.EVENT_LOCATION));
		items.add(new ConditionListAdapter.ConditionItem(Event.EVENT_ORIENTATION));
		items.add(new ConditionListAdapter.ConditionItem(Event.EVENT_TIME));
		items.add(new ConditionListAdapter.ConditionItem(Event.EVENT_SMS));
		items.add(new ConditionListAdapter.ConditionItem(Event.EVENT_TOP_APP_CHANGED));
		return items;
	}
}
