package com.wilson.tasker.events;

import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Scene;

public class RunSceneEvent extends Event {
	public Scene scene;

	public RunSceneEvent(Scene scene) {
		super(Event.EVENT_RUN_SCENE);
		this.scene = scene;
	}
}
