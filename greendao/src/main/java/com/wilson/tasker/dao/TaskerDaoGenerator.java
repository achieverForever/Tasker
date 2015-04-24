package com.wilson.tasker.dao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class TaskerDaoGenerator {

	public static void main(String[] args) throws Exception {
		Schema schema = new Schema(100, "com.wilson.tasker.dao");
		addSceneActivity(schema);
		new DaoGenerator().generateAll(schema, "../app/src/main/java");
	}

	private static void addSceneActivity(Schema schema) {
		Entity sceneActivity = schema.addEntity("SceneActivity");
		sceneActivity.addIdProperty().autoincrement().primaryKey();
		sceneActivity.addDateProperty("time");
		sceneActivity.addStringProperty("sceneName");
		sceneActivity.addIntProperty("actionType");
	}
}