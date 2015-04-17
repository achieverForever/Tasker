package com.wilson.tasker.actions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wilson.tasker.R;
import com.wilson.tasker.model.Action;

public class TestAction extends Action {

	public TestAction() {
		super(TYPE_TEST_ACTION, "Test Action", 0);
	}

	@Override
	public boolean performAction(Context context) {
		Toast.makeText(context, "Test action activated!", Toast.LENGTH_LONG).show();
		return true;
	}

	@Override
	public View getView(Context context, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.list_item_action_common, parent, false);
		TextView name = (TextView) view.findViewById(R.id.name);
		name.setText("Test Action");
		return view;
	}
}
