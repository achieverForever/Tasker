package com.wilson.tasker.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wilson.tasker.model.Action;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Scene;

public class Utils {

	public static final Gson GSON;
	static {
		GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
		gsonBuilder.registerTypeAdapter(Condition.class, new ConditionTypeAdapter());
		gsonBuilder.registerTypeAdapter(Action.class, new ActionTypeAdapter());
		gsonBuilder.registerTypeAdapter(Scene.class, new SceneTypeAdapter());
		GSON = gsonBuilder.create();
	}
}
