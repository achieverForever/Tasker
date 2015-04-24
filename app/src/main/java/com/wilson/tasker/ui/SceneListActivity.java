package com.wilson.tasker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.wilson.tasker.R;


public class SceneListActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);

		setupActionBar();

		if (savedInstanceState == null) {
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.content, SceneListFragment.newInstance(), "scene_list")
					.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_new_scene:
				((SceneListFragment) getSupportFragmentManager()
						.findFragmentByTag("scene_list")).createScene();
				return true;
			case R.id.action_view_timeline:
				Intent intent = new Intent(this, TimelineActivity.class);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void setupActionBar() {
		super.setupActionBar();
		ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
	}
}
