package com.wilson.tasker.loaders;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.wilson.tasker.ui.AppListFragment;

import java.util.ArrayList;
import java.util.List;

public class AppListLoader extends AsyncTaskLoader<List<AppListFragment.AppEntry>> {

	private PackageManager pm;

	private List<AppListFragment.AppEntry> apps;

	public AppListLoader(Context context) {
		super(context);
		pm = context.getPackageManager();
	}

	@Override
	public List<AppListFragment.AppEntry> loadInBackground() {
		final Context context = getContext();

		List<ApplicationInfo> allApps = pm.getInstalledApplications(0);

		List<AppListFragment.AppEntry> data = new ArrayList<>(allApps.size());
		for (ApplicationInfo info : allApps) {
			AppListFragment.AppEntry entry = new AppListFragment.AppEntry(info);
			entry.loadLabel(context);
			entry.loadIcon(context);
			data.add(entry);
		}
		return data;
	}

	@Override
	public void deliverResult(List<AppListFragment.AppEntry> data) {
		// 数据已加载完成，准备传递结果时Loader突然被Reset
		if (isReset()) {
			releaseResources(data);
			return;
		}

		// 保证旧数据在传递新结果之前不会被GC
		List<AppListFragment.AppEntry> oldData = apps;
		apps = data;

		// Loader处于Started状态，正常传递结果给Client
		if (isStarted()) {
			super.deliverResult(data);
		}

		// 新数据已传递，释放不再使用的旧数据
		if (oldData != null) {
			releaseResources(oldData);
		}
	}

	@Override
	protected void onStartLoading() {
		if (apps != null) {
			// 如果已有加载完成的数据，直接传递结果
			deliverResult(apps);
		} else {
			// 开始加载新数据
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	@Override
	protected void onReset() {
		onStopLoading();
		if (apps != null) {
			releaseResources(apps);
			apps = null;
		}
	}

	@Override
	public void onCanceled(List<AppListFragment.AppEntry> data) {
		super.onCanceled(data);
		releaseResources(data);
	}

	private void releaseResources(List<AppListFragment.AppEntry> data) {
		// nothing
	}
}
