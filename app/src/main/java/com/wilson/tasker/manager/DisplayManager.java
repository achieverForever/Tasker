package com.wilson.tasker.manager;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

import com.wilson.tasker.utils.Utils;

import java.io.IOException;
import java.io.InputStream;


public class DisplayManager {

	private Context context;
	private static DisplayManager sInstance;

	private DisplayManager(Context context) {
		this.context = context;
	}

	public static synchronized DisplayManager getsInstance(Context context) {
		if (sInstance == null) {
			sInstance = new DisplayManager(context.getApplicationContext());
		}
		return sInstance;
	}

	/**
	 * 设置亮度
	 *
	 * @param value 亮度值，1~255
	 */
	public void setBrightness(Activity activity, int value) {
		setBrightnessMode(android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

		android.provider.Settings.System.putInt(context.getContentResolver(),
				android.provider.Settings.System.SCREEN_BRIGHTNESS, value);

		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		float brightness = value / 255f;
		brightness = Math.max(Math.min(brightness, 1f), 0.1f);
		lp.screenBrightness = brightness;
		activity.getWindow().setAttributes(lp);
	}

	public void getBrightness() {
		try {
			int b = android.provider.Settings.System.getInt(context.getContentResolver(),
					android.provider.Settings.System.SCREEN_BRIGHTNESS);
			Log.d(Utils.LOG_TAG, "brightness=" + b);
		} catch (Settings.SettingNotFoundException e) {
			e.printStackTrace();
			Log.d(Utils.LOG_TAG, "brightness=" + -1);
		}
	}

	/**
	 * 设置亮度模式
	 *
	 * @param mode 亮度模式，SCREEN_BRIGHTNESS_MODE_AUTOMATIC 或 SCREEN_BRIGHTNESS_MODE_MANUAL
	 */
	public void setBrightnessMode(int mode) {
		android.provider.Settings.System.putInt(context.getContentResolver(),
				android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE, mode);
	}

	/**
	 * 设置壁纸
	 *
	 * @param in 壁纸的图片文件输入流
	 */
	public void setWallpaper(InputStream in) {
		WallpaperManager wm = WallpaperManager.getInstance(context);
		final int minHeight = wm.getDesiredMinimumHeight();
		final int minWidth = wm.getDesiredMinimumWidth() * 2;

		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, opts);
		final int width = opts.outWidth;
		final int height = opts.outHeight;
		opts.inSampleSize = Math.min(width / minWidth, height / minHeight);

		opts.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeStream(in, null, opts);
		if (bitmap != null) {
			try {
				wm.setBitmap(bitmap);
			} catch (IOException e) {
				Log.e(Utils.LOG_TAG, "Error on decoding bitmap");
			}
		}
	}

	/**
	 * 设置屏幕关闭的超时时间
	 *
	 * @param time 超时时间(毫秒)
	 */
	public void setScreenOffTimeout(int time) {
		android.provider.Settings.System.putInt(context.getContentResolver(),
				android.provider.Settings.System.SCREEN_OFF_TIMEOUT, time);
	}

}
