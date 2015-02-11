package com.wilson.tasker.model;

import com.wilson.tasker.conditions.BatteryLevelCondition;
import com.wilson.tasker.conditions.SmsCondition;
import com.wilson.tasker.conditions.TimeCondition;
import com.wilson.tasker.events.BatteryLevelEvent;
import com.wilson.tasker.events.SmsEvent;
import com.wilson.tasker.manager.TaskManager;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class TaskTest extends TestCase {
	private Task task;
	private List<Condition> conditions = new ArrayList<Condition>();
	private List<Runnable> actions = new ArrayList<Runnable>();

	@Override
	public void setUp() throws Exception {
		super.setUp();
		task = new Task("test");
		conditions.add(new SmsCondition(task, "me", "hi"));
		conditions.add(new BatteryLevelCondition(task, BatteryLevelCondition.BatteryLevelType.ABOVE, 0.5f));
		conditions.add(new SmsCondition(task, "*", "hello"));
		actions.add(new Runnable() {
			@Override
			public void run() {
				// do nothing
			}
		});
		task.conditions = conditions;
		task.actions = actions;

		TaskManager.getInstance().addTask(task);
		TaskManager.getInstance().onStart();
	}

	@Override
	public void tearDown() throws Exception {
		TaskManager.getInstance().onStop();
		super.tearDown();
	}

	public void testDispatchEvent() throws Exception {
		task.dispatchEvent(new SmsEvent("me", "hi"));
		Assert.assertTrue(task.conditions.get(0).isSatisfied);

		task.dispatchEvent(new BatteryLevelEvent(0.2f));
		Assert.assertFalse(task.conditions.get(1).isSatisfied);
	}

	public void testCheckIfReadyToRunTask() throws Exception {
		// 条件完全满足
		resetTask(task);
		task.dispatchEvent(new SmsEvent("blablabla", "hello"));
		task.dispatchEvent(new BatteryLevelEvent(0.6f));
		task.dispatchEvent(new SmsEvent("me", "hi"));
		Assert.assertTrue(task.checkIfReadyToRunTask());

		// 条件全部满足(同类条件部分满足)
		resetTask(task);
		task.dispatchEvent(new SmsEvent("blablah", "hello"));
		task.dispatchEvent(new BatteryLevelEvent(0.6f));
		Assert.assertTrue(task.checkIfReadyToRunTask());

		// 条件不满足
		resetTask(task);
		task.dispatchEvent(new SmsEvent("who", "hello"));
		Assert.assertFalse(task.checkIfReadyToRunTask());
	}

	private void resetTask(Task task) {
		task.state = Task.STATE_ENABLED;
		for (Condition c : task.conditions) {
			c.isSatisfied = false;
		}
	}
}