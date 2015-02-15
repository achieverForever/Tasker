package com.wilson.tasker.model;

import com.wilson.tasker.conditions.BatteryLevelCondition;
import com.wilson.tasker.conditions.SmsCondition;
import com.wilson.tasker.events.BatteryLevelEvent;
import com.wilson.tasker.events.SmsEvent;
import com.wilson.tasker.manager.SceneManager;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class SceneTest extends TestCase {
	private Scene scene;
	private List<Condition> conditions = new ArrayList<Condition>();
	private List<Runnable> actions = new ArrayList<Runnable>();

	@Override
	public void setUp() throws Exception {
		super.setUp();
		scene = new Scene("test");
		conditions.add(new SmsCondition(scene, "me", "hi"));
		conditions.add(new BatteryLevelCondition(scene, BatteryLevelCondition.BatteryLevelType.ABOVE, 0.5f));
		conditions.add(new SmsCondition(scene, "*", "hello"));
		actions.add(new Runnable() {
			@Override
			public void run() {
				// do nothing
			}
		});
		scene.conditions = conditions;
		scene.actions = actions;

		SceneManager.getInstance().addScene(scene);
		SceneManager.getInstance().onStart();
	}

	@Override
	public void tearDown() throws Exception {
		SceneManager.getInstance().onStop();
		super.tearDown();
	}

	public void testDispatchEvent() throws Exception {
		scene.dispatchEvent(new SmsEvent("me", "hi"));
		Assert.assertTrue(scene.conditions.get(0).isSatisfied);

		scene.dispatchEvent(new BatteryLevelEvent(0.2f));
		Assert.assertFalse(scene.conditions.get(1).isSatisfied);
	}

	public void testCheckIfReadyToRunTask() throws Exception {
		// 条件完全满足
		resetTask(scene);
		scene.dispatchEvent(new SmsEvent("blablabla", "hello"));
		scene.dispatchEvent(new BatteryLevelEvent(0.6f));
		scene.dispatchEvent(new SmsEvent("me", "hi"));
		Assert.assertTrue(scene.checkIfReadyToRunScene());

		// 条件全部满足(同类条件部分满足)
		resetTask(scene);
		scene.dispatchEvent(new SmsEvent("blablah", "hello"));
		scene.dispatchEvent(new BatteryLevelEvent(0.6f));
		Assert.assertTrue(scene.checkIfReadyToRunScene());

		// 条件不满足
		resetTask(scene);
		scene.dispatchEvent(new SmsEvent("who", "hello"));
		Assert.assertFalse(scene.checkIfReadyToRunScene());
	}

	private void resetTask(Scene scene) {
		scene.state = Scene.STATE_ENABLED;
		for (Condition c : scene.conditions) {
			c.isSatisfied = false;
		}
	}
}