package com.wilson.tasker.ui;

import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wilson.tasker.R;
import com.wilson.tasker.adapters.TaskListAdapter;
import com.wilson.tasker.ui.wiget.FillableCircleView;

/**
 * 显示所有Task的列表，Tasker的主界面
 */
public class MainFragment extends Fragment {

	private ListView taskList;

	public MainFragment() {
	}

	public static Fragment newInstance() {
		return new MainFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);
		taskList = (ListView) view.findViewById(R.id.lv_task_list);
		setupViews();
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		taskList.setAdapter(new TaskListAdapter(getActivity()));
	}

	private void setupViews() {
		taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				FillableCircleView label = (FillableCircleView) view.findViewById(R.id.tv_label);
				label.toggle();
			}
		});
	}
}
