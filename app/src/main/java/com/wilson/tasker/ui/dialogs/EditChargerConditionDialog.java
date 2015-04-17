package com.wilson.tasker.ui.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gc.materialdesign.views.ButtonFlat;
import com.wilson.tasker.R;
import com.wilson.tasker.conditions.ChargerCondition;
import com.wilson.tasker.listeners.OnConditionChangedListener;

public class EditChargerConditionDialog extends DialogFragment implements View.OnClickListener {
	private static final String EXTRA_IS_CHARGING = "com.wilson.tasker.is_charging";

	private ButtonFlat confirmBtn;
	private com.gc.materialdesign.views.Switch isCharging;
	private ChargerCondition condition;
	private OnConditionChangedListener listener;

	public static EditChargerConditionDialog newInstance(boolean charging) {
		Bundle data = new Bundle();
		data.putBoolean(EXTRA_IS_CHARGING, charging);
		EditChargerConditionDialog dialog = new EditChargerConditionDialog();
		dialog.setArguments(data);
		return dialog;
	}

	public EditChargerConditionDialog() {
		// Required empty constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_edit_charger, container, false);
		setupViews(view);
		return view;
	}

	private void setupViews(View rootView) {
		getDialog().setTitle("Edit Charger Condition");
		confirmBtn = (ButtonFlat) rootView.findViewById(R.id.confirm);
		isCharging = (com.gc.materialdesign.views.Switch) rootView.findViewById(R.id.charging);

		confirmBtn.setOnClickListener(this);
		isCharging.setChecked(getArguments().getBoolean(EXTRA_IS_CHARGING, true));
	}

	public void setCondition(ChargerCondition condition) {
		this.condition = condition;
	}

	public void setOnConditionChangedListener(OnConditionChangedListener listener) {
		this.listener = listener;
	}

	@Override
	public void onClick(View v) {
		if (listener != null) {
			listener.onConditionChanged(condition,
					new ChargerCondition(isCharging.isCheck()));
		}
		getDialog().dismiss();
	}
}
