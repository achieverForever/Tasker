package com.wilson.tasker.manager;

import com.wilson.tasker.conditions.CallerCondition;
import com.wilson.tasker.conditions.OrientationCondition;
import com.wilson.tasker.conditions.SmsCondition;
import com.wilson.tasker.events.SmsEvent;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Scene;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class SceneManagerTest extends TestCase {

	public void setUp() throws Exception {
		super.setUp();
		SceneManager.getInstance().onStart();
	}

	public void tearDown() throws Exception {
		SceneManager.getInstance().onStop();
	}

	public void testOnEvent() throws Exception {
		SmsEvent smsEvent = new SmsEvent("me", "hi");
		EventBus.getDefault().post(smsEvent);
		SmsEvent received = (SmsEvent) SceneManager.getInstance().getEvent();
		Assert.assertEquals("Event should not change", smsEvent, received);
	}

	public void testFindTasksByEvent() throws Exception {
		Scene scene1 = new Scene("task1");
		List<Condition> conditions1 = new ArrayList<Condition>();
		conditions1.add(new OrientationCondition(scene1, OrientationManager.ORIENTATION_FACE_DOWN));
		conditions1.add(new CallerCondition(scene1, "111"));
		scene1.conditions = conditions1;

		Scene scene2 = new Scene("task2");
		List<Condition> conditions2 = new ArrayList<Condition>();
		conditions2.add(new SmsCondition(scene2, "me", "hi"));
		scene2.conditions = conditions2;

		SceneManager.getInstance().addScene(scene1);
		SceneManager.getInstance().addScene(scene2);

		List<Scene> foundScenes = SceneManager.getInstance().findScenesByEvent(Event.EVENT_CALLER);
		List<Scene> expected = new ArrayList<Scene>();
		expected.add(scene1);
		Assert.assertEquals("Found tasks not match expected", expected, foundScenes);

	}
}