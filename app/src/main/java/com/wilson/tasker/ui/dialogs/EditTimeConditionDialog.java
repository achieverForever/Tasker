package com.wilson.tasker.ui.dialogs;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.gc.materialdesign.views.ButtonFlat;
import com.wilson.tasker.R;
import com.wilson.tasker.conditions.TimeCondition;
import com.wilson.tasker.listeners.OnConditionChangedListener;

import java.lang.reflect.Field;
import java.util.Calendar;

public class EditTimeConditionDialog extends DialogFragment {

	private ButtonFlat confirmBtn;
	private TextView date;
	private TextView time;
	private TextView duration;
	private TimeCondition condition;
	private OnConditionChangedListener listener;

	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private int durationHours;
	private int durationMins;

	public EditTimeConditionDialog() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_edit_time, container, false);
		initTime();
		setupViews(view);
		return view;
	}

	private void setupViews(View rootView) {
		getDialog().setTitle("Edit Time Condition");
		confirmBtn = (ButtonFlat) rootView.findViewById(R.id.confirm);
		time = (TextView) rootView.findViewById(R.id.time);
		date = (TextView) rootView.findViewById(R.id.date);
		duration = (TextView) rootView.findViewById(R.id.duration);

		confirmBtn.setOnClickListener(onConfirmBtnClickListener);
		time.setOnClickListener(onTimeClickListener);
		date.setOnClickListener(onDateClickListener);
		duration.setOnClickListener(onDurationClickListener);
		date.setText(String.format("%02d/%02d/%02d", year, month+1, day));
		time.setText(String.format("%02d:%02d", hour, minute));
		duration.setText(String.format("%d h %d min", durationHours, durationMins));
	}

	private void initTime() {
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
	}

	public void setCondition(TimeCondition condition) {
		this.condition = condition;
	}

	public void setOnConditionChangedListener(OnConditionChangedListener listener) {
		this.listener = listener;
	}

	private View.OnClickListener onConfirmBtnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Calendar now = Calendar.getInstance();
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(System.currentTimeMillis());

			c.add(Calendar.YEAR, year - now.get(Calendar.YEAR));
			c.add(Calendar.MONTH, month - now.get(Calendar.MONTH));
			c.add(Calendar.DAY_OF_MONTH, day - now.get(Calendar.DAY_OF_MONTH));
			c.add(Calendar.HOUR_OF_DAY, hour - now.get(Calendar.HOUR_OF_DAY));
			c.add(Calendar.MINUTE, minute - now.get(Calendar.MINUTE));
			long startTimeMillis = c.getTimeInMillis();

			c.add(Calendar.HOUR_OF_DAY, durationHours);
			c.add(Calendar.MINUTE, durationMins);
			long endTimeMillis = c.getTimeInMillis();

			if (listener != null) {
				listener.onConditionChanged(condition,
						new TimeCondition(startTimeMillis, endTimeMillis));
			}
			getDialog().dismiss();
		}
	};

	private View.OnClickListener onTimeClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			TimePickerDialog dialog = new TimePickerDialog(getActivity(), onTimeSetListener,
					hour, minute, DateFormat.is24HourFormat(getActivity()));
			dialog.show();
			try {
				Field f = TimePickerDialog.class.getDeclaredField("mTimePicker");
				f.setAccessible(true);
				((TimePicker) f.get(dialog)).setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	};

	private View.OnClickListener onDateClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			DatePickerDialog dialog = new DatePickerDialog(getActivity(), onDateSetListener,
					year, month, day);
			dialog.show();
			dialog.getDatePicker().setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		}
	};

	private View.OnClickListener onDurationClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			TimePickerDialog dialog = new TimePickerDialog(getActivity(), onDurationSetListener,
					0, 0, DateFormat.is24HourFormat(getActivity()));
			dialog.setTitle("Duration");
			dialog.show();
		}
	};

	private TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			EditTimeConditionDialog.this.hour = hourOfDay;
			EditTimeConditionDialog.this.minute = minute;
			time.setText(String.format("%02d:%02d", hourOfDay, minute));
		}
	};

	private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			EditTimeConditionDialog.this.year = year;
			EditTimeConditionDialog.this.month = monthOfYear;
			EditTimeConditionDialog.this.day = dayOfMonth;
			date.setText(String.format("%02d/%02d/%02d", year, monthOfYear+1, dayOfMonth));
		}
	};

	private TimePickerDialog.OnTimeSetListener onDurationSetListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			EditTimeConditionDialog.this.durationHours = hourOfDay;
			EditTimeConditionDialog.this.durationMins = minute;
			duration.setText(String.format("%d h %d min", hourOfDay, minute));
		}
	};
}
