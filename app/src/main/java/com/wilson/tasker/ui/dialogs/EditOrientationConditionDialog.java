package com.wilson.tasker.ui.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gc.materialdesign.views.ButtonFlat;
import com.wilson.tasker.R;
import com.wilson.tasker.conditions.ChargerCondition;
import com.wilson.tasker.conditions.OrientationCondition;
import com.wilson.tasker.listeners.OnConditionChangedListener;
import com.wilson.tasker.manager.OrientationManager;

public class EditOrientationConditionDialog extends DialogFragment implements View.OnClickListener {
	private static final String EXTRA_IS_FACING_DOWN = "com.wilson.tasker.is_facing_down";

	private ButtonFlat confirmBtn;
	private com.gc.materialdesign.views.Switch isFacingDown;
	private OrientationCondition condition;
	private OnConditionChangedListener listener;

	public static EditOrientationConditionDialog newInstance(int orientation) {
		Bundle data = new Bundle();
		data.putInt(EXTRA_IS_FACING_DOWN, orientation);
		EditOrientationConditionDialog dialog = new EditOrientationConditionDialog();
		dialog.setArguments(data);
		return dialog;
	}

	public EditOrientationConditionDialog() {
		// Required empty constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_edit_orientation, container, false);
		setupViews(view);
		return view;
	}

	private void setupViews(View rootView) {
		getDialog().setTitle("Edit Charger Condition");
		confirmBtn = (ButtonFlat) rootView.findViewById(R.id.confirm);
		isFacingDown = (com.gc.materialdesign.views.Switch) rootView.findViewById(R.id.is_facing_down);

		confirmBtn.setOnClickListener(this);
		isFacingDown.setChecked(getArguments()
				.getInt(EXTRA_IS_FACING_DOWN) == OrientationManager.ORIENTATION_FACE_DOWN);
	}

	public void setCondition(OrientationCondition condition) {
		this.condition = condition;
	}

	public void setOnConditionChangedListener(OnConditionChangedListener listener) {
		this.listener = listener;
	}

	@Override
	public void onClick(View v) {
		if (listener != null) {
			listener.onConditionChanged(condition,
					new OrientationCondition(isFacingDown.isCheck() ?
							OrientationManager.ORIENTATION_FACE_DOWN
							: OrientationManager.ORIENTATION_FACE_UP));
		}
		getDialog().dismiss();
	}
}
