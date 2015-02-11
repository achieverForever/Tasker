package com.wilson.tasker.manager;

import android.util.Log;

import com.wilson.tasker.events.TaskDeactivatedEvent;
import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Task;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class TaskManager {
	private static final String TAG = "TaskManager";

	private static TaskManager sInstance = new TaskManager();

	private ArrayList<Task> tasks;

	private Task defaultTask = new Task("default");

	// Just for test
	private Event event;

	public static TaskManager getInstance() {
		return sInstance;
	}

	protected TaskManager() {
		tasks = new ArrayList<Task>(5);
	}

	public void onStart() {
		EventBus.getDefault().register(this);
	}

	public void onStop() {
		EventBus.getDefault().unregister(this);
	}

	public void onEvent(Event event) {
		this.event = event;
		Log.d(TAG, "onEvent [" + event.eventCodeToString() + "]");
		// 处理TASK_DEACTIVATED事件
		if (event.eventCode == Event.EVENT_TASK_DEACTIVATED) {
			onTaskDeactivated(((TaskDeactivatedEvent) event).task);
			return;
		}

		List<Task> interestedTasks = findTasksByEvent(event.eventCode);
		if (interestedTasks.size() <= 0) {
			return;
		}

		for (Task t : interestedTasks) {
			if (t.state == Task.STATE_DISABLED) {
				continue;
			}
			t.dispatchEvent(event);
		}
	}

	// Exposed for testing
	public List<Task> findTasksByEvent(int eventCode) {
		List<Task> result = new ArrayList<Task>();
		for (Task t : tasks) {
			final List<Condition> conditions = t.conditions;
			for (Condition c : conditions) {
				if (c.eventCode == eventCode && result.indexOf(t) == -1) {
					result.add(t);
				}
			}
		}
		return result;
	}

	private void onTaskDeactivated(Task task) {
		Log.d(TAG, String.format("Task [%s] deactivated", task.name));
		Log.d(TAG, "Task [default] activated");
		for (Runnable action : defaultTask.actions) {
			JobScheduler.getInstance().runImmediate(action);
		}
	}

	public void setDefaultActions(List<Runnable> actions) {
		defaultTask.actions = actions;
	}

	public void addTask(Task task) {
		tasks.add(task);
	}

	public void removeTask(Task task) {
		tasks.remove(task);
	}

	// Just for test
	public Event getEvent() {
		return event;
	}
}
