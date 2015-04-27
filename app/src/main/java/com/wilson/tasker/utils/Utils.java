package com.wilson.tasker.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wilson.tasker.model.Action;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Scene;
import com.wilson.tasker.service.WorkerService;

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
		gsonBuilder.registerTypeAdapter(Uri.class, new UriTypeAdapter());
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

	public static boolean isFreshInstall(Context context) {
		SharedPreferences sharedPrefs
				= context.getSharedPreferences(WorkerService.SHARED_PREF_NAME, 0);
		boolean freshInstall = sharedPrefs.getBoolean("is_fresh_install", true);
		if (freshInstall) {
			sharedPrefs.edit().putBoolean("is_fresh_install", false).commit();
		}
		return freshInstall;
	}
}

