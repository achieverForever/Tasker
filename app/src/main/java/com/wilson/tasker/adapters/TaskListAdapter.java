package com.wilson.tasker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wilson.tasker.R;
import com.wilson.tasker.ui.wiget.FillableCircleView;

import java.util.ArrayList;

public class TaskListAdapter extends BaseAdapter {

	private LayoutInflater inflater;

	private static ArrayList<String> tasks;
	static {
		tasks = new ArrayList<String>();
		tasks.add("Default");
		for (int i = 1; i < 8; i++) {
			tasks.add("Task #" + i);
		}
	}

	public TaskListAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return tasks.size();
	}

	@Override
	public Object getItem(int position) {
		return tasks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_task, parent, false);
		}
		FillableCircleView label = (FillableCircleView) convertView.findViewById(R.id.tv_label);
		TextView taskText = (TextView) convertView.findViewById(R.id.tv_task_name);
		label.setText(String.valueOf(position));
		taskText.setText((String) getItem(position));
		return convertView;
	}
}

