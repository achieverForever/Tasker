package com.wilson.tasker.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.wilson.tasker.model.Action;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Scene;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SceneTypeAdapter implements JsonDeserializer<Scene> {

	@Override
	public Scene deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();

		String name = jsonObject.get("name").getAsString();
		String desc = jsonObject.get("desc").getAsString();
		int state = jsonObject.get("state").getAsInt();
		List<Condition> conditions = new ArrayList<>();
		List<Action> actions = new ArrayList<>();

		JsonArray jsonArray = jsonObject.get("conditions").getAsJsonArray();
		for (int i = 0; i < jsonArray.size(); i++) {
			conditions.add(Utils.GSON.fromJson(jsonArray.get(i), Condition.class));
		}

		JsonArray jsonArray2 = jsonObject.get("actions").getAsJsonArray();
		for (int j = 0; j < jsonArray2.size(); j++) {
			actions.add(Utils.GSON.fromJson(jsonArray2.get(j), Action.class));
		}

		Scene scene = new Scene(name, desc, false);
		scene.setState(state);
		for (Condition c : scene.getConditions()) {
			c.listener = scene;
		}

		return scene;
	}
}
