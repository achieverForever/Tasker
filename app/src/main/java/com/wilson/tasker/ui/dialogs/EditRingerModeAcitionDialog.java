package com.wilson.tasker.ui.dialogs;

import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.gc.materialdesign.views.ButtonFlat;
import com.wilson.tasker.R;
import com.wilson.tasker.actions.RingerModeAction;
import com.wilson.tasker.listeners.OnActionChangedListener;

public class EditRingerModeAcitionDialog extends DialogFragment {
	private static final String EXTRA_RINGER_MODE = "com.wilson.tasker.ringer_mode";

	private ButtonFlat confirmBtn;
	private Spinner ringerMode;
	private RingerModeAction action;
	private OnActionChangedListener listener;

	public static EditRingerModeAcitionDialog newInstance(int ringerMode) {
		Bundle data = new Bundle();
		data.putInt(EXTRA_RINGER_MODE, ringerMode);
		EditRingerModeAcitionDialog dialog = new EditRingerModeAcitionDialog();
		dialog.setArguments(data);
		return dialog;
	}

	public EditRingerModeAcitionDialog() {
		// Required empty constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_edit_ringer_mode, container, false);
		setupViews(view);
		return view;
	}

	private void setupViews(View rootView) {
		getDialog().setTitle("Edit Ringer Mode Action");
		confirmBtn = (ButtonFlat) rootView.findViewById(R.id.confirm);
		ringerMode = (Spinner) rootView.findViewById(R.id.ringer_mode);

		confirmBtn.setOnClickListener(confirmClickListener);
		ringerMode.setAdapter(ArrayAdapter.createFromResource(getActivity(),
				R.array.ringer_mode_list, android.R.layout.simple_spinner_dropdown_item));
		ringerMode.setOnItemSelectedListener(itemSelectedListener);
		int selection = findSelection(action.ringerMode);
		if (selection >= 0) {
			ringerMode.setSelection(selection);
		}
	}

	public void setAction(RingerModeAction action) {
		this.action = action;
	}

	public void setOnActionChangedListener(OnActionChangedListener listener) {
		this.listener = listener;
	}

	private View.OnClickListener confirmClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (listener != null) {
				listener.onActionChanged(action, new RingerModeAction(
						(int) stringToRingerMode((String) ringerMode.getSelectedItem())));
			}
			getDialog().dismiss();
		}
	};

	private AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			String s = (String) parent.getAdapter().getItem(position);
			action.ringerMode = stringToRingerMode(s);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};


	private int stringToRingerMode(String value) {
		switch (value) {
			case "Normal":
				return AudioManager.RINGER_MODE_NORMAL;
			case "Silent":
				return AudioManager.RINGER_MODE_SILENT;
			default:
				return AudioManager.RINGER_MODE_VIBRATE;
		}
	}

	private String ringerModeToString(int ringerMode) {
		switch (ringerMode) {
			case AudioManager.RINGER_MODE_NORMAL:
				return "Normal";
			case AudioManager.RINGER_MODE_SILENT:
				return "Silent";
			default:
				return "Vibrate";
		}
	}

	private int findSelection(int ringerMode) {
		String s = ringerModeToString(ringerMode);
		String[] ringerModeStrings = getResources().getStringArray(R.array.ringer_mode_list);
		for (int i = 0; i < ringerModeStrings.length; i++) {
			if (s.equals(ringerModeStrings[i])) {
				return i;
			}
		}
		return -1;
	}
}
