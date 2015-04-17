package com.wilson.tasker.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.wilson.tasker.R;
import com.wilson.tasker.adapters.AppListAdapter;
import com.wilson.tasker.conditions.TopAppCondition;
import com.wilson.tasker.listeners.OnConditionChangedListener;
import com.wilson.tasker.loaders.AppListLoader;

import java.util.List;

public class EditMarkerNameDialog extends DialogFragment {
	private static final String KEY_ADDRESS = "address";

	private OnMarkerNameEnteredListener listener;
	private String defaultAddr;

	public interface OnMarkerNameEnteredListener {
		void onMarkerNameEntered(String markerName);
	}

	public EditMarkerNameDialog() {
		// Required empty constructor
	}

	public static EditMarkerNameDialog newInstance(String address) {
		EditMarkerNameDialog dialog = new EditMarkerNameDialog();
		Bundle args = new Bundle();
		args.putString(KEY_ADDRESS, address);
		dialog.setArguments(args);
		return dialog;
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		defaultAddr = getArguments().getString(KEY_ADDRESS);
		final View view = LayoutInflater.from(getActivity())
				.inflate(R.layout.dialog_marker_name, null, false);
		final EditText markerName = (EditText) view.findViewById(R.id.marker_name);

		markerName.setText(defaultAddr);
		markerName.setSelection(defaultAddr.length());

		return new AlertDialog.Builder(getActivity())
				.setTitle("请输入地标名称")
				.setView(view)
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (listener != null) {
							listener.onMarkerNameEntered(defaultAddr);
						}
					}
				})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						EditText markerName = (EditText) view.findViewById(R.id.marker_name);
						listener.onMarkerNameEntered(markerName.getText().toString());
					}
				}).create();
	}

	public void registerOnMarkerNameEnteredListener(OnMarkerNameEnteredListener listener) {
		this.listener = listener;
	}

	public void unregisterOnMarkerNameEnteredListener() {
		this.listener = null;
	}
}
