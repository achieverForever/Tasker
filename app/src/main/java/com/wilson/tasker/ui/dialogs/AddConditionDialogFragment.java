package com.wilson.tasker.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.wilson.tasker.adapters.ConditionListAdapter2;
import com.wilson.tasker.ui.EditConditionActivity;

public class AddConditionDialogFragment extends DialogFragment {
	private ConditionListAdapter2 adapter;

	public AddConditionDialogFragment() {
		adapter = new ConditionListAdapter2(getActivity());
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
			.setTitle("Select a condition")
			.setAdapter(adapter, listener);
		return builder.create();
	}

	private DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			int eventCode = ((ConditionListAdapter2.ConditionItem) adapter.getItem(which)).eventCode;
			Intent intent = new Intent(getActivity(), EditConditionActivity.class);
			intent.putExtra("", eventCode);
			startActivity(intent);
		}
	};
}
