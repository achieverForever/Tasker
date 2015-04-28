package com.wilson.tasker.model;

import android.content.Context;
import android.util.Log;

import com.wilson.tasker.events.SceneActivatedEvent;
import com.wilson.tasker.events.SceneDeactivatedEvent;
import com.wilson.tasker.utils.Utils;

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
	private String name;

	/** Scene的当前状态 */
	private int state;

	/** 如果为true，当Scene的条件不再满足时，自动回滚所有Actions */
	private boolean isRollbackNeeded;

	/** 线程安全的Scene的条件列表 */
	private List<Condition> conditions;

	/** 线程安全的Scene的动作列表 */
	private List<Action> actions;

	protected Scene() {
		// Required empty constructor for serialize/deserialize
	}

	public Scene(String name, boolean isRollbackNeeded) {
		this.name = name;
		this.isRollbackNeeded = isRollbackNeeded;
		this.state = STATE_ENABLED;
		conditions = new CopyOnWriteArrayList<>();
		actions = new CopyOnWriteArrayList<>();
	}

	public synchronized void dispatchEvent(Event event) {
		for (Condition c : conditions) {
			if (c.eventCode == event.eventCode) {
				c.doCheckEvent(event);
			}
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
		Log.d(Utils.LOG_TAG, String.format(
			"onConditionStateChanged: scene[%s], current state=%d", name, state));

		if (satisfied && state == STATE_ENABLED) {
			boolean readyToRunScene = checkIfReadyToRunScene();
			if (readyToRunScene) {
				Log.d(Utils.LOG_TAG, "Scene [" + name + "] activated");
				state = STATE_ACTIVATED;
				notifySceneActivated();
			}
		} else if (!satisfied && state == STATE_ACTIVATED) {
			Log.d(Utils.LOG_TAG, "Scene [" + name + "] deactivated");
			state = STATE_ENABLED;
			notifySceneDeactivated();
		}
	}

	public void setEnabled(boolean enabled) {
		state = enabled ? STATE_ENABLED : STATE_DISABLED;
	}

	public void addCondition(Condition condition) {
		conditions.add(condition);
		condition.listener = this;
	}

	public void removeCondition(Condition condition) {
		conditions.remove(condition);
		condition.listener = null;
	}

	public void addAction(Action action) {
		actions.add(action);
	}

	public void removeAction(Action action) {
		actions.remove(action);
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


	/* --------------------- Getters and Setters --------------------------- */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public boolean isRollbackNeeded() {
		return isRollbackNeeded;
	}

	public void setIsRollbackNeeded(boolean isRollbackNeeded) {
		this.isRollbackNeeded = isRollbackNeeded;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public List<Action> getActions() {
		return actions;
	}
}
