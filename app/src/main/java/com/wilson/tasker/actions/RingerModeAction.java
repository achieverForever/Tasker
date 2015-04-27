package com.wilson.tasker.actions;

import android.content.Context;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilson.tasker.R;
import com.wilson.tasker.manager.FontManager;
import com.wilson.tasker.manager.WifiManager;
import com.wilson.tasker.model.Action;

public class RingerModeAction extends Action {
	public int ringerMode;
	public int previousState;

	public RingerModeAction(int ringerMode) {
		super(TYPE_RINGER_MODE, "Ringer Mode", R.drawable.ic_ringer_mode);
		this.ringerMode = ringerMode;
	}

	@Override
	public boolean performAction(Context context) {
		com.wilson.tasker.manager.RingtoneManager mgr = com.wilson.tasker.manager.RingtoneManager
				.getInstance(context);
		previousState = mgr.getRingerMode();
		mgr.setRingerMode(ringerMode);
		return true;
	}

	@Override
	public boolean rollback(Context context) {
		com.wilson.tasker.manager.RingtoneManager.getInstance(context).setRingerMode(previousState);
		return true;
	}

	@Override
	public View getView(Context context, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.list_item_action_common, parent, false);
		TextView name = (TextView) view.findViewById(R.id.name);
		ImageView icon = (ImageView) view.findViewById(R.id.icon);
		final TextView desc = (TextView) view.findViewById(R.id.desc);

		name.setText("Set Ringer Mode");
		name.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		if (iconRes != 0) {
			icon.setImageResource(iconRes);
		}
		desc.setText(String.format("Set ringer mode to %s", getRingerModeString(ringerMode)));
		desc.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		return view;
	}

	private String getRingerModeString(int ringerMode) {
		switch (ringerMode) {
			case AudioManager.RINGER_MODE_NORMAL:
				return "Normal";
			case AudioManager.RINGER_MODE_SILENT:
				return "Silent";
			default:
				return "Vibrate";
		}
	}
}
