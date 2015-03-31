package com.wilson.tasker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilson.tasker.R;
import com.wilson.tasker.model.Action;

import java.util.List;

public class AddActionListAdapter extends BaseAdapter {

	private List<Action> actions;
	private LayoutInflater inflater;

	public AddActionListAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		actions = Action.asList();
	}

	@Override
	public int getCount() {
		return actions.size();
	}

	@Override
	public Object getItem(int position) {
		return actions.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_add_action, parent, false);
		}
		Action action = (Action) getItem(position);
		TextView name = (TextView) convertView.findViewById(R.id.name);
		ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
		name.setText(action.name);
		if (action.iconRes != 0) {
			icon.setImageResource(action.iconRes);
		}
		return convertView;
	}
}
