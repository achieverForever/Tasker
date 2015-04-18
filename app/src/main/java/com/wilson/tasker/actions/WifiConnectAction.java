package com.wilson.tasker.actions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilson.tasker.R;
import com.wilson.tasker.manager.FontManager;
import com.wilson.tasker.manager.WifiManager;
import com.wilson.tasker.model.Action;

public class WifiConnectAction extends Action {
	public int networkId;

	public WifiConnectAction(int networkId) {
		super(TYPE_WIFI_CONNECT, "Connect to Specified AP", R.drawable.ic_connect_to_wifi);
		this.networkId = networkId;
	}

	@Override
	public boolean performAction(Context context) {
		return WifiManager.getsInstance(context).connectToNetwork(networkId);
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
		desc.setText(String.format("Connect to AP named %s", "xx"));
		desc.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		return view;
	}
}
