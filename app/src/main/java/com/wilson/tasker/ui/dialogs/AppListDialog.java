package com.wilson.tasker.ui.dialogs;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.wilson.tasker.R;
import com.wilson.tasker.adapters.AppListAdapter;
import com.wilson.tasker.loaders.AppListLoader;

import java.util.List;

public class AppListDialog extends DialogFragment
	implements LoaderManager.LoaderCallbacks<List<AppListDialog.AppEntry>>, AdapterView.OnItemClickListener {
		private static final int LOADER_APP_LIST = 100;

		private AppListAdapter adapter;
		private GridView appList;

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

	public static AppListDialog newInstance() {
		AppListDialog fragment = new AppListDialog();
		return fragment;
	}

	public AppListDialog() {
		// Required empty constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_app_list, container, false);
		setupViews(view);
		return view;
	}

	private void setupViews(View rootView) {
		getDialog().setTitle("Select a Trigger App");
		appList = (GridView) rootView.findViewById(R.id.gv_app_list);
		appList.setOnItemClickListener(this);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new AppListAdapter(getActivity());
		appList.setAdapter(adapter);
		getLoaderManager().initLoader(LOADER_APP_LIST, null, this);
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
