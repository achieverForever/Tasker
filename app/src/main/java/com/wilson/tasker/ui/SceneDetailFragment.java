package com.wilson.tasker.ui;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.wilson.tasker.R;
import com.wilson.tasker.adapters.ActionListAdapter;
import com.wilson.tasker.adapters.ConditionListAdapter;
import com.wilson.tasker.events.SceneDetailEvent;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Scene;
import com.wilson.tasker.ui.dialogs.AppListDialog;

import de.greenrobot.event.EventBus;

public class SceneDetailFragment extends Fragment {
	private static final String TAG = "DEBUG";

	private EditText edtSceneName;
	private Button btnSave;
	private ListView conditionList;
	private ListView actionList;
	private Scene scene;


	public static SceneDetailFragment newInstance() {
		return new SceneDetailFragment();
	}

	public SceneDetailFragment() {
		// Required empty constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().registerSticky(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_scene_detail, container, false);
		setupViews(view);
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	private void setupViews(View root) {
		edtSceneName = (EditText) root.findViewById(R.id.edt_scene_name);
		conditionList = (ListView) root.findViewById(R.id.lv_conditions);
		actionList = (ListView) root.findViewById(R.id.lv_actions);
		btnSave = (Button) root.findViewById(R.id.btn_save);

		LayoutInflater inflater = LayoutInflater.from(getActivity());
		edtSceneName.setText(scene.desc);
		conditionList.addFooterView(inflater.inflate(R.layout.condition_list_footer, conditionList, false));
		conditionList.setAdapter(new ConditionListAdapter(getActivity(), scene.conditions));
		actionList.addFooterView(inflater.inflate(R.layout.action_list_footer, conditionList, false));
		actionList.setAdapter(new ActionListAdapter(getActivity(), scene.actions));
		conditionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Condition condition = (Condition) parent.getAdapter().getItem(position);
				switch (condition.eventCode) {
					case Event.EVENT_TOP_APP_CHANGED:
						AppListDialog.newInstance().show(getFragmentManager(), "app_list");
						break;
					default:
						break;
				}
			}
		});
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
	}

	public void onEvent(SceneDetailEvent event) {
		Log.d(TAG, "onEvent [" + Event.eventCodeToString(event.eventCode) + "]");
		scene = event.scene;
	}
}
