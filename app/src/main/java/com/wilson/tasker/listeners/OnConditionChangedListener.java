package com.wilson.tasker.listeners;

import com.wilson.tasker.model.Condition;

public interface OnConditionChangedListener {
	public void onConditionChanged(Condition previous, Condition current);
}