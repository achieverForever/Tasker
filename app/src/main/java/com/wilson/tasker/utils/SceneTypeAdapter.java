package com.wilson.tasker.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.wilson.tasker.model.Action;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Scene;

import java.lang.reflect.Type;

public class SceneTypeAdapter implements JsonDeserializer<Scene> {

	@Override
	public Scene deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();

		String name = jsonObject.get("name").getAsString();
		int state = jsonObject.get("state").getAsInt();

		Scene scene = new Scene(name, false);
		scene.setState(state);

		JsonArray jsonArray = jsonObject.get("conditions").getAsJsonArray();
		for (int i = 0; i < jsonArray.size(); i++) {
			scene.addCondition(Utils.GSON.fromJson(jsonArray.get(i), Condition.class));
		}

		JsonArray jsonArray2 = jsonObject.get("actions").getAsJsonArray();
		for (int j = 0; j < jsonArray2.size(); j++) {
			scene.addAction(Utils.GSON.fromJson(jsonArray2.get(j), Action.class));
		}

		return scene;
	}
}
