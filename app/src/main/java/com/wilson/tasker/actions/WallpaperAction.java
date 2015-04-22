package com.wilson.tasker.actions;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilson.tasker.R;
import com.wilson.tasker.manager.DisplayManager;
import com.wilson.tasker.manager.FontManager;
import com.wilson.tasker.model.Action;

public class WallpaperAction extends Action {
	public Uri imageUri;

	public WallpaperAction(Uri imageUri) {
		super(TYPE_WALL_PAPER, "Wallpaper", R.drawable.icon_wallpaper);
		this.imageUri = imageUri;
	}

	@Override
	public boolean performAction(Context context) {
		DisplayManager.getsInstance(context).setWallpaper(imageUri);
		return true;
	}

	@Override
	public View getView(Context context, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.list_item_action_common, parent, false);
		TextView name = (TextView) view.findViewById(R.id.name);
		ImageView icon = (ImageView) view.findViewById(R.id.icon);
		final TextView desc = (TextView) view.findViewById(R.id.desc);

		name.setText("Set Wallpaper");
		name.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		if (iconRes != 0) {
			icon.setImageResource(iconRes);
		}
		desc.setText("Change Wallpaper");
		desc.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		return view;
	}
}
