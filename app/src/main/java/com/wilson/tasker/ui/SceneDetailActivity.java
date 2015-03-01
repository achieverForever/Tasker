package com.wilson.tasker.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.wilson.tasker.R;

public class SceneDetailActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);
		if (savedInstanceState == null) {
			getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.content, SceneDetailFragment.newInstance())
				.commit();
		}
		setupActionBar();
	}

	@Override
	protected void setupActionBar() {
		super.setupActionBar();
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
}
