package com.wilson.tasker.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.wilson.tasker.adapters.TemplateListAdapter;
import com.wilson.tasker.ui.EditTaskActivity;

public class AddTaskDialogFragment extends DialogFragment {
	private static final String TAG = "NewTaskDialogFragment";

	private TemplateListAdapter adapter;

	public AddTaskDialogFragment() {
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		adapter = new TemplateListAdapter(getActivity());
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
			.setTitle("Select a template")
			.setAdapter(adapter, listener);
		return builder.create();
	}

	private DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			Log.d(TAG, "onClick(): " + which);
			if (which == 0) {
				startActivity(new Intent(getActivity(), EditTaskActivity.class));
			}
		}
	};

}
