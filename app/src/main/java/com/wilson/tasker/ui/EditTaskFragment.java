package com.wilson.tasker.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wilson.tasker.R;

/**
 * 显示Task编辑界面
 */
public class EditTaskFragment extends Fragment {

	private Button btnAddCondition;

	private Button btnAddAction;

	public static EditTaskFragment newInstance() {
		return new EditTaskFragment();
	}

	public EditTaskFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_edit_task, container, false);
		setupViews();
		return view;
	}

	private void setupViews() {
		btnAddCondition = (Button) getView().findViewById(R.id.btn_add_condition);
		btnAddAction = (Button) getView().findViewById(R.id.btn_add_condition);

		btnAddCondition.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});

		btnAddAction.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});
	}
}
