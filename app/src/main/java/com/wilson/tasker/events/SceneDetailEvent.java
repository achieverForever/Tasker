package com.wilson.tasker.events;

import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Scene;

public class SceneDetailEvent extends Event {
	public Scene scene;

	public SceneDetailEvent(Scene scene) {
		super(Event.EVENT_SCENE_DETAIL);
		this.scene = scene;
	}
}
