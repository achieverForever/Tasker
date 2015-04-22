package com.wilson.tasker.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.wilson.tasker.actions.AirplaneModeAction;
import com.wilson.tasker.actions.BluetoothAction;
import com.wilson.tasker.actions.BrightnessAction;
import com.wilson.tasker.actions.WallpaperAction;
import com.wilson.tasker.actions.WifiAction;
import com.wilson.tasker.actions.WifiConnectAction;

import java.util.ArrayList;
import java.util.List;

public abstract class Action {
	public static final int STATE_IDLE = 0;
	public static final int STATE_RUNNING = 1;
	public static final int STATE_COMPLETED = 2;

	public static final int TYPE_MIN_VALUE = 8;
	public static final int TYPE_TEST_ACTION = TYPE_MIN_VALUE + 1;
	public static final int TYPE_AIRPLANE_MODE = TYPE_MIN_VALUE + 2;
	public static final int TYPE_BLUETOOTH = TYPE_MIN_VALUE + 3;
	public static final int TYPE_BRIGHTNESS = TYPE_MIN_VALUE + 4;
	public static final int TYPE_WALL_PAPER= TYPE_MIN_VALUE + 5;
	public static final int TYPE_WIFI = TYPE_MIN_VALUE + 6;
	public static final int TYPE_WIFI_CONNECT = TYPE_MIN_VALUE + 7;

	/** Action当前状态 */
	public int state;

	/** Action名称 */
	public String name;

	/** Action类型 */
	public int actionType;

	/** Action显示Icon */
	public int iconRes;

	protected Action() {
		// Required empty constructor for serialize/deserialize
	}

	public Action(int actionType, String name, int iconRes) {
		this.actionType = actionType;
		this.name = name;
		this.iconRes = iconRes;
		this.state = STATE_IDLE;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Action)) {
			return false;
		}
		return ((Action) o) == this;
	}

	/**
	 * 执行Action
	 *
	 * @param context Context对象
	 * @return 成功返回true，否则为false
	 */
	public abstract boolean performAction(Context context);

	/**
	 * 回滚Action对系统的修改
	 *
	 * @param context Context对象
	 * @return 成功返回true，否则返回false
	 */
	public boolean rollback(Context context) {
		return true;
	}

	/**
	 * 返回Action的View表示
	 *
	 * @param context
	 * @param parent
	 */
	public abstract View getView(Context context, ViewGroup parent);

	public static List<Action> asList() {
		// TODO - 创建Action的Dialog中显示所有Action的列表
		List<Action> actions = new ArrayList<>();
		actions.add(new AirplaneModeAction(true));
		actions.add(new BluetoothAction(true));
		actions.add(new BrightnessAction(200));
		actions.add(new WallpaperAction(null));
		actions.add(new WifiAction(true));
		actions.add(new WifiConnectAction(1));
		return actions;
	}
}
