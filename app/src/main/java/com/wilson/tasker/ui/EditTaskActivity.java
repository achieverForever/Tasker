package com.wilson.tasker.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;

import com.wilson.tasker.R;

public class EditTaskActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

	    if (savedInstanceState == null) {
		    getSupportFragmentManager()
				    .beginTransaction()
				    .replace(R.id.content, EditTaskFragment.newInstance())
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
}
