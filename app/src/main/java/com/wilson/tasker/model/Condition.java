package com.wilson.tasker.model;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.wilson.tasker.conditions.BatteryLevelCondition;
import com.wilson.tasker.conditions.CallerCondition;
import com.wilson.tasker.conditions.ChargerCondition;
import com.wilson.tasker.conditions.LocationCondition;
import com.wilson.tasker.conditions.OrientationCondition;
import com.wilson.tasker.conditions.SmsCondition;
import com.wilson.tasker.conditions.TimeCondition;
import com.wilson.tasker.conditions.TopAppCondition;
import com.wilson.tasker.manager.OrientationManager;

import java.util.ArrayList;
import java.util.List;

public abstract class Condition {
	public static final int STATE_UNSATISFIED = 0;
	public static final int STATE_SATISFIED = 1;

	/** 事件类型 */
	public int eventCode;

	/** Condition的当前状态 */
	public int state;

	/** Condition名称 */
	public String name;

	/** Condition的显示Icon */
	public int iconRes;

	/** isSticky为true，表示是持续性的Condition，否则为瞬时性的Condition */
	public boolean isSticky;

	/** Condition状态变化监听者 */
	public transient ConditionStateChangedListener listener;

	private boolean called = false;

	protected Condition() {
		// Required empty constructor for serialize/deserialize
	}

	protected Condition(int eventCode, String name, int iconRes, boolean isSticky) {
		this.eventCode = eventCode;
		this.name = name;
		this.state = STATE_UNSATISFIED;
		this.iconRes = iconRes;
		this.isSticky = isSticky;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Condition)) {
			return false;
		}
		return ((Condition) o) == this;
	}

	public void doCheckEvent(Event event) {
		boolean satisfied = performCheckEvent(event);
		if (!called) {
			throw new IllegalStateException("Derived class must call through super.performCheckEvent().");
		}

		if (isSticky) {
			// 持续性条件状态变化
			if ((satisfied && state == STATE_UNSATISFIED)
				|| (!satisfied && state == STATE_SATISFIED)) {
				state = satisfied ? STATE_SATISFIED : STATE_UNSATISFIED;
				listener.onConditionStateChanged(this, satisfied);
			}
		} else if (satisfied) {
			// 瞬时性条件满足
			state = satisfied ? STATE_SATISFIED : STATE_UNSATISFIED;
			listener.onConditionStateChanged(this, satisfied);
		}

	}

	/**
	 * NOTE: 派生类一定要调用super的实现
	 */
	public boolean performCheckEvent(Event event) {
		validateEvent(event);
		called = true;
		return false;
	}

	/**
	 * 返回Condition的View表示
	 *
	 * @param context
	 * @param parent
	 */
	public abstract View getView(Context context, ViewGroup parent);

	/**
	 * 确保进行满足性检验的是相同类型的Events
	 *
	 * @param event
	 */
	private void validateEvent(Event event) {
		if (event.eventCode != this.eventCode) {
			throw new IllegalStateException("Event type not match");
		}
	}

	public interface ConditionStateChangedListener {
		public void onConditionStateChanged(Condition condition, boolean satisfied);
	}

	public static List<Condition> asList() {
		// TODO - 每增加一个Condition，都要在这里新增一项
		List<Condition> conditions = new ArrayList<>();
		conditions.add(new BatteryLevelCondition(BatteryLevelCondition.TYPE_BELOW, 0.3f));
		conditions.add(new CallerCondition("10086"));
		conditions.add(new ChargerCondition(true));
		conditions.add(new LocationCondition("Home"));
		conditions.add(new OrientationCondition(OrientationManager.ORIENTATION_FACE_DOWN));
		conditions.add(new SmsCondition("10086", "hi"));
		conditions.add(new TopAppCondition("com.android.anything"));
		conditions.add(new TimeCondition(0, 0));
		return conditions;
	}


}