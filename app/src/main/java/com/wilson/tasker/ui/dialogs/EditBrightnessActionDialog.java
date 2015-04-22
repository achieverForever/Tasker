package com.wilson.tasker.ui.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.Slider;
import com.wilson.tasker.R;
import com.wilson.tasker.actions.BrightnessAction;
import com.wilson.tasker.conditions.OrientationCondition;
import com.wilson.tasker.listeners.OnActionChangedListener;
import com.wilson.tasker.listeners.OnConditionChangedListener;
import com.wilson.tasker.manager.DisplayManager;
import com.wilson.tasker.manager.OrientationManager;
import com.wilson.tasker.utils.Utils;

public class EditBrightnessActionDialog extends DialogFragment implements View.OnClickListener {
	private static final String EXTRA_BRIGHTNESS = "com.wilson.tasker.brightness";
	private static final int MAX_BRIGHTNESS = 255;
	private static final int MIN_BRIGHTNESS = 64;
	private static final int STEP = 10;

	private ButtonFlat confirmBtn;
	private SeekBar brightness;
	private BrightnessAction action;
	private OnActionChangedListener listener;

	public static EditBrightnessActionDialog newInstance(int targetBrightness) {
		Bundle data = new Bundle();
		data.putInt(EXTRA_BRIGHTNESS, targetBrightness);
		EditBrightnessActionDialog dialog = new EditBrightnessActionDialog();
		dialog.setArguments(data);
		return dialog;
	}

	public EditBrightnessActionDialog() {
		// Required empty constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_edit_brightness, container, false);
		setupViews(view);
		return view;
	}

	private void setupViews(View rootView) {
		getDialog().setTitle("Edit Brightness Action");
		confirmBtn = (ButtonFlat) rootView.findViewById(R.id.confirm);
		brightness = (SeekBar) rootView.findViewById(R.id.brightness);
		brightness.setMax((MAX_BRIGHTNESS - MIN_BRIGHTNESS) / STEP);

		confirmBtn.setOnClickListener(this);
		brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				float value = MIN_BRIGHTNESS + progress * STEP;
				Log.d(Utils.LOG_TAG, "" + value);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});
	}

	public void setAction(BrightnessAction action) {
		this.action = action;
	}

	public void setOnActionChangedListener(OnActionChangedListener listener) {
		this.listener = listener;
	}

	@Override
	public void onClick(View v) {
		if (listener != null) {
			listener.onActionChanged(action,
					new BrightnessAction(MIN_BRIGHTNESS + brightness.getProgress() * STEP));
		}
		getDialog().dismiss();
	}
}
