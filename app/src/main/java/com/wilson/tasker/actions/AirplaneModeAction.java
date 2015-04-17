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
import com.wilson.tasker.manager.AirplaneModeEnabler;
import com.wilson.tasker.manager.FontManager;
import com.wilson.tasker.model.Action;

public class AirplaneModeAction extends Action {
	public boolean on;

	public AirplaneModeAction(boolean on) {
		super(TYPE_AIRPLANE_MODE, "Airplane Mode", R.drawable.ic_airplane_mode);
		this.on = on;
	}

	@Override
	public boolean performAction(Context context) {
		if (on != AirplaneModeEnabler.getInstance(context).isAirplaneModeOn()) {
			AirplaneModeEnabler.getInstance(context).setAirplaneModeOn(on);
		}
		return true;
	}

	@Override
	public View getView(Context context, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.action_switch_common, parent, false);
		ImageView icon = (ImageView) view.findViewById(R.id.icon);
		TextView name = (TextView) view.findViewById(R.id.name);
		final TextView desc = (TextView) view.findViewById(R.id.desc);
		com.gc.materialdesign.views.Switch isOn = (com.gc.materialdesign.views.Switch) view.findViewById(R.id.is_on);

		isOn.setChecked(on);
		isOn.setOncheckListener(new com.gc.materialdesign.views.Switch.OnCheckListener() {
			@Override
			public void onCheck(boolean checked) {
				on = checked;
				desc.setText(String.format("Turn %s Airplane Mode", on ? "on" : "off"));

			}
		});
		if (iconRes != 0) {
			icon.setImageResource(iconRes);
		}
		name.setText("Airplane Mode");
		desc.setText(String.format("Turn %s Airplane Mode", on ? "on" : "off"));
		name.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		desc.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		return view;
	}
}
