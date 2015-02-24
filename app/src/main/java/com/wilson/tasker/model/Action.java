package com.wilson.tasker.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.wilson.tasker.manager.AirplaneModeEnabler;

import java.util.HashMap;
import java.util.Map;

public abstract class Action {
	public static final int STATE_IDLE = 0;
	public static final int STATE_RUNNING = 1;
	public static final int STATE_COMPLETED = 2;

	public String type;
	public int state;

	public Action(String type) {
		this.type = type;
		this.state = STATE_IDLE;
	}

	/**
	 * 执行Action
	 *
	 * @param context
	 * @return 成功返回true，否则为false
	 */
	public abstract boolean performAction(Context context);

	/**
	 * 返回Action的View表示
	 *
	 * @param context
	 * @param parent
	 */
	public abstract View getView(Context context, ViewGroup parent);

}
