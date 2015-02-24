package com.wilson.tasker.actions;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.wilson.tasker.manager.WifiManager;
import com.wilson.tasker.model.Action;

public class WifiConnectAction extends Action {
	public int networkId;

	public WifiConnectAction(int networkId) {
		super("wifi_connect");
		this.networkId = networkId;
	}

	@Override
	public boolean performAction(Context context) {
		return WifiManager.getsInstance(context).connectToNetwork(networkId);
	}

	@Override
	public View getView(Context context, ViewGroup parent) {
		return null;
	}
}
