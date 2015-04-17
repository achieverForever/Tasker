package com.wilson.tasker.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wilson.tasker.model.Action;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Scene;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

	public static final String LOG_TAG = "Tasker";

	public static final Gson GSON;

	public static final String DOT_ALL = ".*";
	public static final Pattern CLEAN_NUMBER_REGEX = Pattern.compile("^(\\d+)\\D*");

	static {
		GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
		gsonBuilder.registerTypeAdapter(Condition.class, new ConditionTypeAdapter());
		gsonBuilder.registerTypeAdapter(Action.class, new ActionTypeAdapter());
		gsonBuilder.registerTypeAdapter(Scene.class, new SceneTypeAdapter());
		GSON = gsonBuilder.create();
	}

	public static String cleanNumber(String callerNumber) {
		String cleanNumber = "";
		Matcher matcher = Utils.CLEAN_NUMBER_REGEX.matcher(callerNumber);
		if (matcher.matches()) {
			cleanNumber = matcher.group(1);
		}
		return cleanNumber;
	}
}
