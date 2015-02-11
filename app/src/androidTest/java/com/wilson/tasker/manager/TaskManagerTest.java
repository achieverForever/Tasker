package com.wilson.tasker.manager;

import com.wilson.tasker.conditions.CallerCondition;
import com.wilson.tasker.conditions.OrientationCondition;
import com.wilson.tasker.conditions.SmsCondition;
import com.wilson.tasker.events.SmsEvent;
import com.wilson.tasker.manager.OrientationManager;
import com.wilson.tasker.manager.TaskManager;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Task;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class TaskManagerTest extends TestCase {

	public void setUp() throws Exception {
		super.setUp();
		TaskManager.getInstance().onStart();
	}

	public void tearDown() throws Exception {
		TaskManager.getInstance().onStop();
	}

	public void testOnEvent() throws Exception {
		SmsEvent smsEvent = new SmsEvent("me", "hi");
		EventBus.getDefault().post(smsEvent);
		SmsEvent received = (SmsEvent) TaskManager.getInstance().getEvent();
		Assert.assertEquals("Event should not change", smsEvent, received);
	}

	public void testFindTasksByEvent() throws Exception {
		Task task1 = new Task("task1");
		List<Condition> conditions1 = new ArrayList<Condition>();
		conditions1.add(new OrientationCondition(task1, OrientationManager.ORIENTATION_FACE_DOWN));
		conditions1.add(new CallerCondition(task1, "111"));
		task1.conditions = conditions1;

		Task task2 = new Task("task2");
		List<Condition> conditions2 = new ArrayList<Condition>();
		conditions2.add(new SmsCondition(task2, "me", "hi"));
		task2.conditions = conditions2;

		TaskManager.getInstance().addTask(task1);
		TaskManager.getInstance().addTask(task2);

		List<Task> foundTasks = TaskManager.getInstance().findTasksByEvent(Event.EVENT_CALLER);
		List<Task> expected = new ArrayList<Task>();
		expected.add(task1);
		Assert.assertEquals("Found tasks not match expected", expected, foundTasks);

	}
}