package com.wilson.tasker.conditions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilson.tasker.R;
import com.wilson.tasker.events.TopAppChangedEvent;
import com.wilson.tasker.manager.FontManager;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;

public class TopAppCondition extends Condition {
	public String targetPkgName;

	public TopAppCondition(String targetPkgName) {
		super(Event.EVENT_TOP_APP_CHANGED, "Current APP");
		this.targetPkgName = targetPkgName;
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		TopAppChangedEvent ev = (TopAppChangedEvent) event;
		if (targetPkgName.equals(ev.pkgName)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public View getView(final Context context, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.condition_top_app, parent, false);
		ImageView appIcon = (ImageView) view.findViewById(R.id.iv_app_icon);
		TextView appLabel = (TextView) view.findViewById(R.id.tv_app_label);
		TextView conditionDesc = (TextView) view.findViewById(R.id.tv_condition_desc);

		PackageManager pm = context.getPackageManager();
		Drawable appIconDrawable = null;
		CharSequence appLabelText = null;
		try {
			ApplicationInfo appInfo = pm.getApplicationInfo(targetPkgName, 0);
			appIconDrawable = appInfo.loadIcon(pm);
			appLabelText = appInfo.loadLabel(pm);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		if (appIconDrawable != null) {
			appIcon.setImageDrawable(appIconDrawable);
		}
		if (appLabelText != null) {
			appLabel.setText(appLabelText);
			appLabel.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
			conditionDesc.setText("Trigger When Current App is " + appLabelText);
			conditionDesc.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		}
		return view;
	}
}
