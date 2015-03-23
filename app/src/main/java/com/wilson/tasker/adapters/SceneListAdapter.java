package com.wilson.tasker.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wilson.tasker.R;
import com.wilson.tasker.manager.FontManager;
import com.wilson.tasker.manager.SceneManager;
import com.wilson.tasker.model.Scene;
import com.wilson.tasker.ui.wiget.FillableCircleView;
import com.wilson.tasker.utils.Utils;

import java.util.List;

public class SceneListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Scene> scenes;
	private Context context;

	public SceneListAdapter(Context context) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.scenes = SceneManager.getInstance().getScenes();
	}

	@Override
	public int getCount() {
		return scenes.size();
	}

	@Override
	public Object getItem(int position) {
		return scenes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_scene, parent, false);
		}
		Scene scene = (Scene) getItem(position);
		FillableCircleView label = (FillableCircleView) convertView.findViewById(R.id.tv_title);
		TextView sceneName = (TextView) convertView.findViewById(R.id.tv_scene_name);
		sceneName.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		label.setText(String.valueOf(position + 1));
		sceneName.setText(scene.getDesc());
		return convertView;
	}
}

