package com.wilson.tasker.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wilson.tasker.R;

/**
 * 负责显示和调整Task的相对优先级
 */
public class PriorityFragment extends Fragment {
	public PriorityFragment() {
	}

	public static PriorityFragment newInstance() {
		return new PriorityFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_priority, container, false);
		return view;
	}
}
