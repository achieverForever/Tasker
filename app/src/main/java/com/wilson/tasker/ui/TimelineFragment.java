package com.wilson.tasker.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.wilson.tasker.R;
import com.wilson.tasker.adapters.TimelineListAdapter;
import com.wilson.tasker.app.TaskerApplication;
import com.wilson.tasker.dao.SceneActivity;
import com.wilson.tasker.dao.SceneActivityDao;

import java.util.List;

public class TimelineFragment extends Fragment {
	private ListView activityList;

	private SceneActivityDao sceneActivityDao;

	public TimelineFragment() {
	}

	public static TimelineFragment newInstance() {
		return new TimelineFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sceneActivityDao = ((TaskerApplication) getActivity().getApplication())
				.getDaoSession(getActivity()).getSceneActivityDao();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_timeline, container, false);
		setUpViews(view);
		return view;
	}

	private void setUpViews(View rootView) {
		activityList = (ListView) rootView.findViewById(R.id.activities);

		List<SceneActivity> result = sceneActivityDao.queryBuilder()
				.orderDesc(SceneActivityDao.Properties.Time).list();
		activityList.setAdapter(new TimelineListAdapter(getActivity(), result));
	}
}
