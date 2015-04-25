package com.wilson.tasker.events;

import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;

import java.util.List;

public class SceneChangedEvent extends Event {
	public List<Condition> removedConditions;
	public List<Condition> newConditions;

	public SceneChangedEvent(List<Condition> removedConditions, List<Condition> newConditions) {
		super(Event.EVENT_SCENE_CHANGED);
		this.removedConditions = removedConditions;
		this.newConditions = newConditions;
	}
}
