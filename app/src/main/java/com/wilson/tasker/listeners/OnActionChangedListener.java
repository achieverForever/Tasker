package com.wilson.tasker.listeners;

import com.wilson.tasker.model.Action;

public interface OnActionChangedListener {
	public void onActionChanged(Action previous, Action current);
}