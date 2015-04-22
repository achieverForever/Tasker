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
import com.wilson.tasker.adapters.AddActionListAdapter;
import com.wilson.tasker.adapters.AddConditionListAdapter;
import com.wilson.tasker.listeners.OnActionChangedListener;
import com.wilson.tasker.model.Action;

public class AddActionDialog extends DialogFragment implements AdapterView.OnItemClickListener {
	private AddActionListAdapter adapter;
	private GridView actionList;
	private OnActionChangedListener listener;

	public static AddActionDialog newInstance() {
		return new AddActionDialog();
	}

	public AddActionDialog() {
		// Required empty constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_action_list, container, false);
		setupViews(view);
		return view;
	}

	private void setupViews(View rootView) {
		getDialog().setTitle("Select an Action");
		actionList = (GridView) rootView.findViewById(R.id.action_list);
		actionList.setOnItemClickListener(this);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new AddActionListAdapter(getActivity());
		actionList.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Action action = (Action) adapter.getItem(position);
		getDialog().dismiss();
		if (listener != null) {
			// 新增Action
			listener.onActionChanged(null, action);
		}
	}

	public void setOnActionChangedListener(OnActionChangedListener listener) {
		this.listener = listener;
	}
}
