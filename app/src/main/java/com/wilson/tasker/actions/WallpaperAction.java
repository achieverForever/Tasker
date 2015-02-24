package com.wilson.tasker.actions;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.wilson.tasker.model.Action;

public class WallpaperAction extends Action {
	public WallpaperAction(String type) {
		super(type);
	}

	@Override
	public boolean performAction(Context context) {
		return false;
	}

	@Override
	public View getView(Context context, ViewGroup parent) {
		return null;
	}
}
