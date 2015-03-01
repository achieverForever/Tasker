package com.wilson.tasker.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wilson.tasker.model.Action;
import com.wilson.tasker.model.Scene;

import java.util.List;

public class ActionListAdapter extends BaseAdapter {

	private Context context;
	private List<Action> actions;
	private Scene scene;

	public ActionListAdapter(Context context, List<Action> actions, Scene scene) {
		this.context = context;
		this.actions = actions;
		this.scene = scene;
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
		Action action = (Action) getItem(position);
		return action.getView(context, parent);
	}
}
