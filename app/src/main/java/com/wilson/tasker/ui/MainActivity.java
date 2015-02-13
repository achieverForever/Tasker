package com.wilson.tasker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.wilson.tasker.R;
import com.wilson.tasker.ui.dialogs.AddTaskDialogFragment;


public class MainActivity extends BaseActivity {
	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);

		setupActionBar();

		if (savedInstanceState == null) {
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.content, MainFragment.newInstance())
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
			case R.id.action_add_task:
				new AddTaskDialogFragment().show(getFragmentManager(), "NewTaskDialogFragment");
				return true;
			case R.id.action_priority:
				startActivity(new Intent(MainActivity.this, PriorityActivity.class));
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
