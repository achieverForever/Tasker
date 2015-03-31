package com.wilson.tasker.ui.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.wilson.tasker.R;
import com.wilson.tasker.adapters.AddConditionListAdapter;
import com.wilson.tasker.listeners.OnConditionChangedListener;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;

public class AddConditionDialog extends DialogFragment implements AdapterView.OnItemClickListener {
	private AddConditionListAdapter adapter;
	private GridView conditionList;
	private OnConditionChangedListener listener;

	public static AddConditionDialog newInstance() {
		return new AddConditionDialog();
	}

	public AddConditionDialog() {
		// Required empty constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_condition_list, container, false);
		setupViews(view);
		return view;
	}

	private void setupViews(View rootView) {
		getDialog().setTitle("Select a Condition");
		conditionList = (GridView) rootView.findViewById(R.id.gv_condition_list);
		conditionList.setOnItemClickListener(this);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new AddConditionListAdapter(getActivity());
		conditionList.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Condition condition = (Condition) adapter.getItem(position);
		getDialog().dismiss();
		if (listener != null) {
			// 新增Condition
			listener.onConditionChanged(null, condition);
		}
	}

	public void setOnConditionChangedListener(OnConditionChangedListener listener) {
		this.listener = listener;
	}
}
