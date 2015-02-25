package com.wilson.tasker.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wilson.tasker.model.Condition;

import java.util.List;

public class ConditionListAdapter extends BaseAdapter {

	private Context context;
	private List<Condition> conditions;

	public ConditionListAdapter(Context context, List<Condition> conditions) {
		this.context = context;
		this.conditions = conditions;
	}

	@Override
	public int getCount() {
		return conditions.size();
	}

	@Override
	public Object getItem(int position) {
		return conditions.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Condition condition = (Condition) getItem(position);
		return condition.getView(context, parent);
	}

	public void updateCondition(Condition previous, Condition current) {
		if (previous == null) {
			conditions.add(current);
		} else {
			int pos = conditions.indexOf(previous);
			if (pos != -1) {
				conditions.set(pos, current);
			}
		}
		notifyDataSetChanged();
	}
}
