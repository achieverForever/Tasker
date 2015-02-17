package com.wilson.tasker.actions;

import android.content.Context;

import com.wilson.tasker.manager.DisplayManager;
import com.wilson.tasker.model.Action;

public class BrightnessAction extends Action {
	public float brightness;    // 1~255

	public BrightnessAction(float brightness) {
		super("brightness");
		this.brightness = brightness;
	}

	@Override
	public boolean performAction(Context context) {
		// TODO - 接口不一致~
//		DisplayManager.getsInstance(context).setBrightness();
		return false;
	}
}
