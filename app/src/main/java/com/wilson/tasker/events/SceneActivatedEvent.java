package com.wilson.tasker.events;

import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Scene;

public class SceneActivatedEvent extends Event {
	public Scene scene;

	public SceneActivatedEvent(Scene scene) {
		super(Event.EVENT_SCENE_DEACTIVATED);
		this.scene = scene;
	}
}
