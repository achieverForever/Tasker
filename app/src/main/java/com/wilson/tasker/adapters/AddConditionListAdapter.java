package com.wilson.tasker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilson.tasker.R;
import com.wilson.tasker.model.Condition;

import java.util.List;

public class AddConditionListAdapter extends BaseAdapter {

	private List<Condition> conditions;
	private LayoutInflater inflater;

	public AddConditionListAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		conditions = Condition.asList();
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
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_add_condition, parent, false);
		}
		Condition condition = (Condition) getItem(position);
		TextView name = (TextView) convertView.findViewById(R.id.tv_condition_name);
		ImageView icon = (ImageView) convertView.findViewById(R.id.iv_condition_icon);
		name.setText(condition.name);
		if (condition.iconRes != 0) {
			icon.setImageResource(condition.iconRes);
		}
		return convertView;
	}
}
