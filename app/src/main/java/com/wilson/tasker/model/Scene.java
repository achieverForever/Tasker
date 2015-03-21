package com.wilson.tasker.model;

import android.content.Context;
import android.util.Log;

import com.wilson.tasker.events.SceneActivatedEvent;
import com.wilson.tasker.events.SceneDeactivatedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.greenrobot.event.EventBus;

/**
 * 表示一个Scene的相关信息
 */
public class Scene implements Condition.ConditionStateChangedListener {
	public static final int STATE_ENABLED = 1;
	public static final int STATE_DISABLED = 2;
	public static final int STATE_ACTIVATED = 4;

	/** Scene的名称 */
	public String name;

	/** Scene的简短文字描述 */
	public String desc;

	/** Scene的当前状态 */
	public int state;

	/** 如果为true，当Scene的条件不再满足时，自动回滚所有Actions */
	public boolean isRollbackNeeded;

	/** 线程安全的Scene的条件列表 */
	public List<Condition> conditions = new CopyOnWriteArrayList<>();

	/** 线程安全的Scene的动作列表 */
	public List<Action> actions = new CopyOnWriteArrayList<>();

	protected Scene() {
		// Required empty constructor for serialize/deserialize
	}

	public Scene(String name, String desc, List<Condition> conditions, List<Action> actions) {
		this.name = name;
		this.desc = desc;
		this.state = STATE_ENABLED;
		this.conditions = conditions;
		this.actions = actions;
		for (Condition c : this.conditions) {
			c.listener = this;
		}
	}

	public synchronized void dispatchEvent(Event event) {
		for (Condition c : conditions) {
			if (c.eventCode == event.eventCode) {
				c.doCheckEvent(event);
			}
		}
	}

	// Exposed for testing
	public void scheduleToRunScene() {
		boolean readyToRunScene = checkIfReadyToRunScene();
		if (readyToRunScene) {
			Log.d("Tasker", "Scene [" + name + "] activated");
			state = STATE_ACTIVATED;
			notifySceneActivated();
		}
	}

	// Exposed for testing
	public synchronized boolean checkIfReadyToRunScene() {
		for (Condition c : conditions) {
			if (c.state == Condition.STATE_UNSATISFIED) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 执行Scene的所有Actions
	 *
	 * @param context Context对象
	 * @return 成功返回true，否则为false
	 */
	public synchronized boolean runScene(Context context) {
		boolean success = true;
		for (Action action : actions) {
			success &= action.performAction(context);
		}
		return success;
	}

	/**
	 * 当Scene的条件不再满足时，回滚该Scene的系统设置修改
	 *
	 * @param context Context对象
	 * @return 成功返回true，否则为false
	 */
	public synchronized boolean rollback(Context context) {
		boolean success = true;
		for (Action action : actions) {
			success &= action.rollback(context);
		}
		return success;
	}

	private void notifySceneActivated() {
		EventBus.getDefault().post(new SceneActivatedEvent(this));
	}

	private void notifySceneDeactivated() {
		EventBus.getDefault().post(new SceneDeactivatedEvent(this));
	}

	@Override
	public void onConditionStateChanged(Condition condition, boolean satisfied) {
		if (satisfied) {
			scheduleToRunScene();
		} else if (state == STATE_ACTIVATED) {
			Log.d("Tasker", "Scene [" + name + "] deactivated");
			state = STATE_ENABLED;
			notifySceneDeactivated();
		}
	}

	public void setEnabled(boolean enabled) {
		state = enabled ? STATE_ENABLED : STATE_DISABLED;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Scene)) {
			return false;
		}
		Scene other = (Scene) o;
		return this.name.equals(other.name);
	}
}
