package com.wilson.tasker.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Scene;

import java.util.List;

public class ConditionListAdapter extends BaseAdapter {

	private Context context;
	private Scene scene;
	private List<Condition> conditions;

	public ConditionListAdapter(Context context, List<Condition> conditions, Scene scene) {
		this.context = context;
		this.conditions = conditions;
		this.scene = scene;
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

	/**
	 * 处理Condition变化事件(新增、删除、参数变更)
	 *
	 * @param previous 之前的Condition对象，为null表示新增Condition
	 * @param current 当前Condition对象，为null表示减少Condition
	 */
	public void handleConditionChanged(Condition previous, Condition current) {
		if (current != null && current.listener == null) {
			current.listener = scene;
		} else if (previous != null && current == null) {
			previous.listener = null;
		}

		if (previous == null && current != null) {
			conditions.add(current);
		} else if (previous != null && current == null) {
			conditions.remove(previous);
		} else {
			int pos = conditions.indexOf(previous);
			if (pos != -1) {
				conditions.set(pos, current);
			}
		}
		notifyDataSetChanged();
	}
}
