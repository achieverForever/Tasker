package com.wilson.tasker.ui;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.wilson.tasker.R;
import com.wilson.tasker.adapters.AppListAdapter;
import com.wilson.tasker.loaders.AppListLoader;

import java.util.List;

/**
 * 显示当前安装的所有App列表
 */
public class AppListFragment extends Fragment
		implements LoaderManager.LoaderCallbacks<List<AppListFragment.AppEntry>>,
		AdapterView.OnItemClickListener {

	private static final int LOADER_APP_LIST = 100;

	private AppListAdapter adapter;

	private ListView appList;


	public static class AppEntry {
		public ApplicationInfo info;
		public Drawable icon;
		public String label;

		public AppEntry(ApplicationInfo info) {
			this.info = info;
		}

		public void loadLabel(Context context) {
			CharSequence label = info.loadLabel(context.getPackageManager()).toString();
			this.label = label == null ? info.packageName : label.toString();
		}

		public void loadIcon(Context context) {
			this.icon = info.loadIcon(context.getPackageManager());
		}
	}

	public static AppListFragment newInstance() {
		AppListFragment fragment = new AppListFragment();
		return fragment;
	}

	public AppListFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_app_list, container, false);
		setupViews(view);
		return view;
	}

	private void setupViews(View rootView) {
		appList = (ListView) rootView.findViewById(R.id.lv_app_list);
		appList.setOnItemClickListener(this);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new AppListAdapter(getActivity());
		appList.setAdapter(adapter);
		getLoaderManager().initLoader(LOADER_APP_LIST, null, this);
		android.app.Fragment f;
		f.getLoaderManager()
	}

	@Override
	public Loader<List<AppEntry>> onCreateLoader(int id, Bundle args) {
		return new AppListLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<AppEntry>> loader, List<AppEntry> data) {
		adapter.setData(data);
	}

	@Override
	public void onLoaderReset(Loader<List<AppEntry>> loader) {
		adapter.setData(null);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		AppEntry app = adapter.getItem(position);
		Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(app.info.packageName);
		Toast.makeText(getActivity(), position + " selected", Toast.LENGTH_SHORT).show();
	}

}
