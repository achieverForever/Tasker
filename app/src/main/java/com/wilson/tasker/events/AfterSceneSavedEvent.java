package com.wilson.tasker.events;

import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;

import java.util.List;

public class AfterSceneSavedEvent extends Event {
	public List<Condition> removedConditions;
	public List<Condition> newConditions;

	public AfterSceneSavedEvent(List<Condition> removedConditions, List<Condition> newConditions) {
		super(Event.EVENT_AFTER_SCENE_SAVED);
		this.removedConditions = removedConditions;
		this.newConditions = newConditions;
	}
}
