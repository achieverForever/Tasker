package com.wilson.tasker.ui;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.wilson.tasker.R;
import com.wilson.tasker.conditions.BatteryLevelCondition;
import com.wilson.tasker.model.Condition;

public class BatteryConditionFragment extends BaseFragment {
	private Spinner spAboveOrBelow;
	private SeekBar sbBatteryLevel;
	private TextView tvBatteryLevelPct;

	public BatteryConditionFragment() {
		condition = new BatteryLevelCondition(BatteryLevelCondition.BatteryLevelType.ABOVE, 0.0f);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_edit_condition_battery, container, false);
		spAboveOrBelow = (Spinner) view.findViewById(R.id.sp_below_or_above);
		sbBatteryLevel = (SeekBar) view.findViewById(R.id.sb_battery_level);
		tvBatteryLevelPct = (TextView) view.findViewById(R.id.tv_battery_level);

		initUI();

		return view;
	}

	private void initUI() {
		spAboveOrBelow.setAdapter(adapter);
		spAboveOrBelow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				if (i == 0) {
					((BatteryLevelCondition) condition).type = BatteryLevelCondition.BatteryLevelType.ABOVE;
				} else {
					((BatteryLevelCondition) condition).type = BatteryLevelCondition.BatteryLevelType.BELOW;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {
			}
		});

		sbBatteryLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
				if (b) {
					((BatteryLevelCondition) condition).targetValue
							= (float) i / (float) sbBatteryLevel.getMax();
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});
	}

	@Override
	protected Condition onPreSaveCondition() {
		BatteryLevelCondition c = (BatteryLevelCondition) condition;
		String aboveOrBelow = c.type == BatteryLevelCondition.BatteryLevelType.ABOVE ?
				"Above" : "Below";
		return c;
	}

	private SpinnerAdapter adapter = new SpinnerAdapter() {
		private String[] text = new String[] {
				"Above", "Below"
		};

		@Override
		public View getDropDownView(int i, View view, ViewGroup viewGroup) {
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			textView.setText(text[i]);
			return textView;
		}

		@Override
		public void registerDataSetObserver(DataSetObserver dataSetObserver) {
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
		}

		@Override
		public int getCount() {
			return text.length;
		}

		@Override
		public Object getItem(int i) {
			return text[i];
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			textView.setText(text[i]);
			return textView;
		}

		@Override
		public int getItemViewType(int i) {
			return 0;
		}

		@Override
		public int getViewTypeCount() {
			return 0;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}
	};
}
