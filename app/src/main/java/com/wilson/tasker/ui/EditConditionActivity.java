package com.wilson.tasker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.wilson.tasker.R;
import com.wilson.tasker.model.Condition;

public class EditConditionActivity extends BaseActivity
		implements BaseFragment.OnConditionSaveListener {

	public static final String EXTRA_EVENT_CODE = "com.wilson.tasker.ui.EVENT_CODE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);

		Intent intent = getIntent();
		if (intent == null) {
			finish();
		}

		int eventCode = intent.getIntExtra(EXTRA_EVENT_CODE, -1);
		if (eventCode == -1) {
			finish();
		}

		Fragment fragment = getSelectedFragment(eventCode);

		if (savedInstanceState == null) {
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.content, fragment)
					.commit();
		}

		setupActionBar();
	}

	private Fragment getSelectedFragment(int eventCode) {
		//TODO - implements me
		return null;
	}


	@Override
	public void onSaveCondition(Condition condition) {
		//TODO
		Intent data = new Intent();
		setResult(RESULT_OK, data);
		finish();
	}

	@Override
	public void onDontSaveCondition(Condition condition) {
		//TODO
		Intent data = new Intent();
		setResult(RESULT_OK, data);
		finish();
	}

	@Override
	protected void setupActionBar() {
		super.setupActionBar();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
}
