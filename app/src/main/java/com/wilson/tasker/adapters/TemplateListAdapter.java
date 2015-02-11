package com.wilson.tasker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wilson.tasker.R;

import java.util.ArrayList;

public class TemplateListAdapter extends BaseAdapter {

	private LayoutInflater inflater;

	public TemplateListAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	static class Template {
		String title;
		String desc;

		Template(String title, String desc) {
			this.title = title;
			this.desc = desc;
		}
	}

	private static ArrayList<Template> templates;
	static {
		templates = new ArrayList<Template>();
		templates.add(new Template("Start from scratch", ""));
		for (int i = 1; i < 5; i++) {
			templates.add(new Template("Title " + i, "Desc " + i));
		}
	}

	@Override
	public int getCount() {
		return templates.size();
	}

	@Override
	public Object getItem(int position) {
		return templates.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_template, parent, false);
		}

		final Template item = (Template) getItem(position);
		TextView titleText = (TextView) convertView.findViewById(R.id.tv_title);
		TextView descText = (TextView) convertView.findViewById(R.id.tv_desc);
		titleText.setText(item.title);
		descText.setText(item.desc);
		return convertView;
	}
}
