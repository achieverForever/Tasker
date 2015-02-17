package com.wilson.tasker.actions;

import android.content.Context;

import com.wilson.tasker.model.Action;

public class WallpaperAction extends Action {
	public WallpaperAction(String type) {
		super(type);
	}

	@Override
	public boolean performAction(Context context) {
		return false;
	}
}
