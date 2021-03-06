package com.wilson.tasker.ui.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.gc.materialdesign.views.ButtonFlat;
import com.wilson.tasker.R;
import com.wilson.tasker.conditions.BatteryLevelCondition;
import com.wilson.tasker.listeners.OnConditionChangedListener;

public class EditBatteryLevelConditionDialog extends DialogFragment implements View.OnClickListener {
	private static final String EXTRA_BATTERY_LEVEL_TYPE = "com.wilson.tasker.battery_level_type";
	private static final String EXTRA_TARGET_VALUE = "com.wilson.tasker.target_value";

	private Spinner batteryLevelList;
	private Spinner batteryLevelType;
	private ButtonFlat confirmBtn;
	private BatteryLevelCondition condition;
	private OnConditionChangedListener listener;

	public static EditBatteryLevelConditionDialog newInstance(int type, float targetValue) {
		Bundle data = new Bundle();
		data.putInt(EXTRA_BATTERY_LEVEL_TYPE, type);
		data.putFloat(EXTRA_TARGET_VALUE, targetValue);
		EditBatteryLevelConditionDialog dialog = new EditBatteryLevelConditionDialog();
		dialog.setArguments(data);
		return dialog;
	}

	public EditBatteryLevelConditionDialog() {
		// Required empty constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_edit_battery_level, container, false);
		setupViews(view);
		return view;
	}

	private void setupViews(View rootView) {
		getDialog().setTitle("Edit Battery Level Condition");
		batteryLevelList = (Spinner) rootView.findViewById(R.id.battery_level_list);
		batteryLevelType = (Spinner) rootView.findViewById(R.id.battery_level_type);
		confirmBtn = (ButtonFlat) rootView.findViewById(R.id.confirm);
		confirmBtn.setOnClickListener(this);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
			R.array.battery_level_list, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(),
			R.array.battery_level_type, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		batteryLevelList.setAdapter(adapter);
		batteryLevelType.setAdapter(adapter2);
		batteryLevelType.setSelection(getArguments().getInt(EXTRA_BATTERY_LEVEL_TYPE, 0));
		int selection = (int) (getArguments().getFloat(EXTRA_TARGET_VALUE, 0.1f) * 10 - 1);
		batteryLevelList.setSelection(selection);
	}

	public void setCondition(BatteryLevelCondition condition) {
		this.condition = condition;
	}

	public void setOnConditionChangedListener(OnConditionChangedListener listener) {
		this.listener = listener;
	}

	private float getTargetValue(String percentStr) {
		return Float.parseFloat(percentStr.replace("%", "")) / 100f;
	}

	private int getBatteryLevelType(String typeStr) {
		return typeStr.equals("Above") ?
			BatteryLevelCondition.TYPE_ABOVE :
			BatteryLevelCondition.TYPE_BELOW;
	}

	@Override
	public void onClick(View v) {
		final String percentStr = (String) batteryLevelList.getSelectedItem();
		final String typeStr = (String) batteryLevelType.getSelectedItem();
		if (listener != null) {
			listener.onConditionChanged(condition,
				new BatteryLevelCondition(getBatteryLevelType(typeStr),
					getTargetValue(percentStr)));
		}
		getDialog().dismiss();
	}
}
