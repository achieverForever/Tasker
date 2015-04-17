package com.wilson.tasker.events;

import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Scene;

public class RefreshSceneListEvent extends Event {

	public RefreshSceneListEvent() {
		super(Event.EVENT_REFRESH_SCENE_LIST);
	}
}
