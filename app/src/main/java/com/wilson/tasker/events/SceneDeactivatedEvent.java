package com.wilson.tasker.events;

import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Scene;

public class SceneDeactivatedEvent extends Event {
	public Scene scene;

	public SceneDeactivatedEvent(Scene scene) {
		super(Event.EVENT_SCENE_DEACTIVATED);
		this.scene = scene;
	}
}
