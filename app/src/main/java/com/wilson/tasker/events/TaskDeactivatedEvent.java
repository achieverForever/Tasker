package com.wilson.tasker.events;

import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Task;

public class TaskDeactivatedEvent extends Event {
	public Task task;

	public TaskDeactivatedEvent(Task task) {
		super(Event.EVENT_TASK_DEACTIVATED);
		this.task = task;
	}
}
