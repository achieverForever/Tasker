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
import com.wilson.tasker.manager.BluetoothEnabler;
import com.wilson.tasker.manager.FontManager;
import com.wilson.tasker.model.Action;


public class BluetoothAction extends Action {
	public boolean on;

	public BluetoothAction(boolean on) {
		super(TYPE_BLUETOOTH, "Bluetooth", R.drawable.icon_bluetooth);
		this.on = on;
	}

	@Override
	public boolean performAction(Context context) {
		if (!BluetoothEnabler.getsInstance().isBluetoothSupported()) {
			return false;
		}
		BluetoothEnabler.getsInstance().setBluetoothEnabled(on);
		return true;
	}

	@Override
	public View getView(Context context, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.action_switch_common, parent, false);
		TextView actionName = (TextView) view.findViewById(R.id.name);
		ImageView actionIcon = (ImageView) view.findViewById(R.id.icon);
		final TextView actionDesc = (TextView) view.findViewById(R.id.desc);
		Switch swEnabled = (Switch) view.findViewById(R.id.is_on);

		actionName.setText("Bluetooth");
		actionName.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		if (iconRes != 0) {
			actionIcon.setImageResource(iconRes);
		}
		actionDesc.setText(String.format("Turn %s Bluetooth", on ? "on" : "off"));
		actionDesc.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		swEnabled.setChecked(on);
		swEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				on = isChecked;
				actionDesc.setText(String.format("Turn %s Bluetooth", on ? "on" : "off"));
			}
		});
		return view;
	}
}
