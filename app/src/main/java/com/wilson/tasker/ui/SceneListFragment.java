package com.wilson.tasker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.wilson.tasker.R;
import com.wilson.tasker.adapters.SceneListAdapter;
import com.wilson.tasker.events.RefreshSceneListEvent;
import com.wilson.tasker.events.SceneDetailEvent;
import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Scene;
import com.wilson.tasker.ui.widget.FillableCircleView;
import com.wilson.tasker.utils.Utils;

import de.greenrobot.event.EventBus;

/**
 * 显示所有Scene List，Tasker的主界面
 */
public class SceneListFragment extends Fragment {

	private ListView sceneList;
	private Button btnNewScene;
	private SceneListAdapter adapter;

	public SceneListFragment() {
		// Required empty constructor
	}

	public static Fragment newInstance() {
		return new SceneListFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		EventBus.getDefault().registerSticky(this);
		View view = inflater.inflate(R.layout.fragment_main, container, false);
		setupViews(view);
		return view;
	}

	private void setupViews(View root) {
		btnNewScene = (Button) root.findViewById(R.id.btn_new_scene);
		sceneList = (ListView) root.findViewById(R.id.lv_scene_list);
		adapter = new SceneListAdapter(getActivity());
		sceneList.setAdapter(adapter);
		sceneList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				FillableCircleView label = (FillableCircleView) view.findViewById(R.id.name);
				label.toggle();
				EventBus.getDefault().postSticky(new SceneDetailEvent((Scene) adapter.getItem(position)));
				startActivity(new Intent(getActivity(), SceneDetailActivity.class));
				getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		btnNewScene.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "new scene clicked.", Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void onEvent(RefreshSceneListEvent event) {
		Log.d(Utils.LOG_TAG, "onEvent [" + Event.eventCodeToString(event.eventCode) + "]");
		adapter.notifyDataSetChanged();
	}
}
