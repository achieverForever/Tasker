package com.wilson.tasker.ui;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.wilson.tasker.R;

/**
 * 提供回退等基本功能的Activity基类
 */
public class BaseActivity extends ActionBarActivity {
	/**
	 * 派生类一定要调用super的实现
	 */
	protected void setupActionBar() {
		setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
