package com.wilson.tasker.model;


public abstract class Condition {
	public static final int STATE_UNSATISFIED = 0;
	public static final int STATE_SATISFIED = 1;

	public int eventCode;
	public int state;
	public ConditionStateChangedListener listener;

	private boolean called = false;

	protected Condition(int eventCode, ConditionStateChangedListener listener) {
		this.eventCode = eventCode;
		this.listener = listener;
		this.state = STATE_UNSATISFIED;
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
		if (!called) {
			throw new IllegalStateException("Derived class must call through super.performCheckEvent().");
		}
		// 更新条件的状态
		if ((satisfied && state == STATE_UNSATISFIED) || (!satisfied && state == STATE_SATISFIED)) {
			if (satisfied) {
				state = STATE_SATISFIED;
			} else {
				state = STATE_UNSATISFIED;
			}
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

	private void validateEvent(Event event) {
		if (event.eventCode != this.eventCode) {
			throw new IllegalStateException("Event type not match");
		}
	}

	public interface ConditionStateChangedListener {
		public void onConditionStateChanged(Condition condition, boolean satisfied);
	}
}