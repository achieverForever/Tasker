package com.wilson.tasker.manager;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

public class FontManager {
	private static FontManager sInstance = new FontManager();
	private Map<String, Typeface> fonts;

	private FontManager() {
		fonts = new HashMap<>();
	}

	public static FontManager getsInstance() {
		return sInstance;
	}

	public Typeface loadFont(Context context, String path) {
		Typeface font = fonts.get(path);
		if (font == null) {
			font = Typeface.createFromAsset(context.getAssets(), path);
			fonts.put(path, font);
		}
		return font;
	}
}
