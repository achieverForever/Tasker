package com.wilson.tasker.conditions;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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

//CHECK
public class TopAppCondition extends Condition {
	public String targetPkgName;

	public TopAppCondition(String targetPkgName) {
		super(Event.EVENT_TOP_APP_CHANGED, "Current APP", R.drawable.ic_app, false);
		this.targetPkgName = targetPkgName;
	}

	@Override
	public boolean performCheckEvent(Event event) {
		super.performCheckEvent(event);
		TopAppChangedEvent ev = (TopAppChangedEvent) event;
		return targetPkgName.equals(ev.pkgName);
	}

	@Override
	public View getView(final Context context, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.list_item_condition, parent, false);
		ImageView appIcon = (ImageView) view.findViewById(R.id.iv_icon);
		TextView appLabel = (TextView) view.findViewById(R.id.tv_title);
		TextView conditionDesc = (TextView) view.findViewById(R.id.tv_desc);

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
		} else {
			appIcon.setImageResource(R.drawable.ic_app);
		}
		if (appLabelText == null) {
			appLabelText = "-";
		}
		appLabel.setText(appLabelText);
		appLabel.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		conditionDesc.setText("Trigger When Current App is " + appLabelText);
		conditionDesc.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		return view;
	}
}
