package com.wilson.tasker.model;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.wilson.tasker.events.TaskDeactivatedEvent;
import com.wilson.tasker.manager.JobScheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.greenrobot.event.EventBus;

/**
 * 表示一个Task的相关信息
 */
public class Task implements Condition.ConditionStateChangedListener {
	public static final int STATE_ENABLED = 1;
	public static final int STATE_DISABLED = 2;
	public static final int STATE_ACTIVATED = 4;

	public String name;
	public String desc;
	public Drawable conditionIcon;
	public Drawable actionIcon;
	public int state;
	public List<Condition> conditions = new ArrayList<Condition>();
	public List<Runnable> actions = new ArrayList<Runnable>();

	public Task(String name) {
		this.name = name;
		this.state = STATE_ENABLED;
	}

	public void dispatchEvent(Event event) {
		for (Condition c : conditions) {
			if (c.eventCode == event.eventCode) {
				c.doCheckEvent(event);
			}
		}
	}

	// Exposed for testing
	public void performTaskIfNecessary() {
		boolean readyToRunTask = checkIfReadyToRunTask();
		if (readyToRunTask) {
			state = STATE_ACTIVATED;
			Log.d("Tasker", "Task [" + name  + "] activated");
			runTask();
		}
	}

	// Exposed for testing
	public boolean checkIfReadyToRunTask() {
		Set<Condition> set = new HashSet<Condition>(conditions);
		for (Condition c : set) {
			int count = Collections.frequency(conditions, c);
			// 同类的Condition是OR的关系
			if (count == 1 && !c.isSatisfied) {
				return false;
			} else {
				List<Condition> list = findAll(conditions, c.eventCode);
				boolean atLeaseOne = false;
				for (Condition each : list) {
					if (each.isSatisfied) {
						atLeaseOne = true;
						break;
					}
				}
				if (!atLeaseOne) {
					return false;
				}
			}
		}
		return true;
	}

	private void runTask() {
		for (Runnable action : actions) {
			JobScheduler.getInstance().runImmediate(action);
		}
	}

	private static List<Condition> findAll(List<Condition> list, int eventCode) {
		List<Condition> result = new LinkedList<Condition>();
		for (Condition c : list) {
			if (c.eventCode == eventCode) {
				result.add(c);
			}
		}
		return result;
	}

	private void notifyTaskDeactivated() {
		EventBus.getDefault().post(new TaskDeactivatedEvent(this));
	}

	@Override
	public void onConditionStateChanged(Condition condition, boolean satisfied) {
		if (satisfied) {
			performTaskIfNecessary();
		} else if (state == STATE_ACTIVATED) {
			state = STATE_ENABLED;
			notifyTaskDeactivated();
		}
	}

	public void addCondition(Condition condition) {
		this.conditions.add(condition);
	}

	public void removeCondition(Condition condition) {
		this.conditions.remove(condition);
	}

	public void addAction(Runnable action) {
		this.actions.add(action);
	}

	public void removeAction(Runnable action) {
		this.actions.remove(action);
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
		if (!(o instanceof Task)) {
			return false;
		}
		Task t = (Task) o;
		return this.name.equals(t.name);
	}
}
