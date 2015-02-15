package com.wilson.tasker.model;

import android.util.Log;

import com.wilson.tasker.events.SceneActivatedEvent;
import com.wilson.tasker.events.SceneDeactivatedEvent;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 表示一个Scene的相关信息
 */
public class Scene implements Condition.ConditionStateChangedListener {
	public static final int STATE_ENABLED = 1;
	public static final int STATE_DISABLED = 2;
	public static final int STATE_ACTIVATED = 4;

	public String name;
	public String desc;
	public int state;
	public List<Condition> conditions = new ArrayList<>();
	public List<Action> actions = new ArrayList<>();

	public Scene(String name, List<Condition> conditions, List<Action> actions) {
		this.name = name;
		this.state = STATE_ENABLED;
		this.conditions = conditions;
		this.actions = actions;
	}

	public void dispatchEvent(Event event) {
		for (Condition c : conditions) {
			if (c.eventCode == event.eventCode) {
				c.doCheckEvent(event);
			}
		}
	}

	// Exposed for testing
	public void performSceneIfNecessary() {
		boolean readyToRunScene = checkIfReadyToRunScene();
		if (readyToRunScene) {
			Log.d("Tasker", "Scene [" + name  + "] activated");
			state = STATE_ACTIVATED;
			notifySceneActivated();
			runScene();
		}
	}

	// Exposed for testing
	public boolean checkIfReadyToRunScene() {
		for (Condition c : conditions) {
			if (c.state == Condition.STATE_UNSATISFIED) {
				return false;
			}
		}
		return true;
	}

	private void runScene() {
		for (Action action : actions) {
			// TODO - Post actions to IntentService to execute
		}
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
			performSceneIfNecessary();
		} else if (state == STATE_ACTIVATED) {
			Log.d("Tasker", "Scene [" + name  + "] deactivated");
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
