package com.wilson.tasker.actions;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilson.tasker.R;
import com.wilson.tasker.manager.DisplayManager;
import com.wilson.tasker.manager.FontManager;
import com.wilson.tasker.model.Action;
import com.wilson.tasker.ui.RefreshBrightnessActivity;

public class BrightnessAction extends Action {
	public int brightness;    // 1~255
	public int previousState;

	public BrightnessAction(int brightness) {
		super(TYPE_BRIGHTNESS, "Brightness", R.drawable.icon_brightness);
		this.brightness = brightness;
	}

	@Override
	public boolean performAction(Context context) {
		previousState = DisplayManager.getsInstance(context).getBrightness();
		Intent intent = new Intent(context, RefreshBrightnessActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(RefreshBrightnessActivity.EXTRA_BRIGHTNESS, brightness);
		context.startActivity(intent);
		return true;
	}

	@Override
	public boolean rollback(Context context) {
		Intent intent = new Intent(context, RefreshBrightnessActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(RefreshBrightnessActivity.EXTRA_BRIGHTNESS, previousState);
		context.startActivity(intent);
		return true;
	}

	@Override
	public View getView(Context context, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.list_item_action_common, parent, false);
		TextView name = (TextView) view.findViewById(R.id.name);
		ImageView icon = (ImageView) view.findViewById(R.id.icon);
		final TextView desc = (TextView) view.findViewById(R.id.desc);

		name.setText("Brightness");
		name.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		if (iconRes != 0) {
			icon.setImageResource(iconRes);
		}
		desc.setText(String.format("Set Brightness to %d", brightness));
		desc.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		return view;
	}
}
