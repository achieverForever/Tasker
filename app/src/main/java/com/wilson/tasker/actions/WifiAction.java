package com.wilson.tasker.actions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.wilson.tasker.R;
import com.wilson.tasker.manager.FontManager;
import com.wilson.tasker.manager.WifiManager;
import com.wilson.tasker.model.Action;

public class WifiAction extends Action {
	public boolean on;

	public WifiAction(boolean on) {
		super("wifi");
		this.on = on;
	}

	@Override
	public boolean performAction(Context context) {
		return WifiManager.getsInstance(context).setWifiEnabled(on);
	}

	@Override
	public View getView(Context context, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.action_switch, parent, false);
		TextView actionName = (TextView) view.findViewById(R.id.tv_action_name);
		final TextView actionDesc = (TextView) view.findViewById(R.id.tv_action_desc);
		Switch swEnabled = (Switch) view.findViewById(R.id.sw_enabled);
		actionName.setText("Wifi");
		actionName.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		actionDesc.setText(String.format("Turn %s Wifi", on ? "on" : "off"));
		actionDesc.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		swEnabled.setChecked(on);
		swEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				on = isChecked;
				actionDesc.setText(String.format("Turn %s Wifi", on ? "on" : "off"));
			}
		});
		return view;
	}
}
