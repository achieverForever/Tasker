package com.wilson.tasker.actions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.wilson.tasker.R;
import com.wilson.tasker.manager.FontManager;
import com.wilson.tasker.manager.WifiManager;
import com.wilson.tasker.model.Action;

public class WifiAction extends Action {
	public boolean on;
	public boolean previousState;

	public WifiAction(boolean on) {
		super(TYPE_WIFI, "Wi-Fi", R.drawable.ic_signal_wifi_3_bar_grey600_48dp);
		this.on = on;
	}

	@Override
	public boolean performAction(Context context) {
		previousState = WifiManager.getsInstance(context).isWifiEnabled();
		return WifiManager.getsInstance(context).setWifiEnabled(on);
	}

	@Override
	public boolean rollback(Context context) {
		WifiManager.getsInstance(context).setWifiEnabled(previousState);
		return true;
	}

	@Override
	public View getView(Context context, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.action_switch_common, parent, false);
		TextView actionName = (TextView) view.findViewById(R.id.name);
		ImageView actionIcon = (ImageView) view.findViewById(R.id.icon);
		final TextView actionDesc = (TextView) view.findViewById(R.id.desc);
		com.gc.materialdesign.views.Switch swEnabled
				= (com.gc.materialdesign.views.Switch) view.findViewById(R.id.is_on);

		if (iconRes != 0) {
			actionIcon.setImageResource(iconRes);
		}
		actionName.setText("Wifi");
		actionName.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		actionDesc.setText(String.format("Turn %s Wifi", on ? "on" : "off"));
		actionDesc.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		swEnabled.setChecked(on);
		swEnabled.setOncheckListener(new com.gc.materialdesign.views.Switch.OnCheckListener() {
			@Override
			public void onCheck(boolean b) {
				on = b;
				actionDesc.setText(String.format("Turn %s Wifi", on ? "on" : "off"));
			}
		});
		return view;
	}
}
