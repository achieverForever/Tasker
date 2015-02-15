package com.wilson.tasker.manager;

import android.util.Log;

import com.wilson.tasker.events.SceneDeactivatedEvent;
import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Scene;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class SceneManager {
	private static final String TAG = "SceneManager";

	private static SceneManager sInstance = new SceneManager();

	private ArrayList<Scene> scenes;

	public static SceneManager getInstance() {
		return sInstance;
	}

	protected SceneManager() {
		scenes = new ArrayList<>(5);
	}

	// Exposed for testing
	public List<Scene> findScenesByEvent(int eventCode) {
		List<Scene> result = new ArrayList<>();
		for (Scene s : scenes) {
			final List<Condition> conditions = s.conditions;
			for (Condition c : conditions) {
				if (c.eventCode == eventCode && result.indexOf(s) == -1) {
					result.add(s);
				}
			}
		}
		return result;
	}

	public void handleSceneDeactivated(Scene scene) {
		// TODO - implements me
	}

	public void addScene(Scene scene) {
		scenes.add(scene);
	}

	public void removeScene(Scene scene) {
		scenes.remove(scene);
	}

	public ArrayList<Scene> getScenes() {
		return scenes;
	}
}
