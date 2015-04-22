package com.wilson.tasker.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.wilson.tasker.R;
import com.wilson.tasker.manager.DisplayManager;

/**
 * 没有界面的Activity，用于刷新屏幕亮度
 */
public class RefreshBrightnessActivity extends Activity {
	public static final String EXTRA_BRIGHTNESS = "com.wilson.tasker.brightness";

	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refresh_brightness);

		DisplayManager.getsInstance(this)
				.setBrightness(this, getIntent().getIntExtra(EXTRA_BRIGHTNESS, 0));
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				finish();
			}
		}, 500);
	}
}
