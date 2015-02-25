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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddConditionListAdapter extends BaseAdapter {

	public static class ConditionItem {
		public int eventCode;
		public String name;
		public int icon;

		public ConditionItem(int eventCode, String name) {
			this.eventCode = eventCode;
			this.name = name;
			this.icon = 0;
		}
	}

	private List<ConditionItem> items;
	private LayoutInflater inflater;

	public AddConditionListAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		for (Condition condition : Condition.asList()) {
			if (items == null) {
				items = new ArrayList<>();
			}
			items.add(new ConditionItem(condition.eventCode, condition.name));
		}
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_condition, parent, false);
		}
		ConditionItem item = (ConditionItem) getItem(position);
		TextView name = (TextView) convertView.findViewById(R.id.tv_condition_name);
		ImageView icon = (ImageView) convertView.findViewById(R.id.iv_condition_icon);
		name.setText(item.name);
		if (item.icon != 0) {
			icon.setImageResource(item.icon);
		}
		return convertView;
	}
}
