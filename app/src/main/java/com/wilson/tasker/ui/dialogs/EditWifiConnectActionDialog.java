package com.wilson.tasker.ui.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wilson.tasker.R;
import com.wilson.tasker.actions.WifiConnectAction;
import com.wilson.tasker.listeners.OnActionChangedListener;

import java.util.ArrayList;
import java.util.List;

public class EditWifiConnectActionDialog extends DialogFragment {
	private static final String EXTRA_NETWORK_ID = "com.wilson.tasker.network_id";
	private static final String EXTRA_SSID = "com.wilson.tasker.ssid";

	private ListView networkList;
	private WifiConnectAction action;
	private OnActionChangedListener listener;

	private List<NetworkItem> networks = new ArrayList<>();
	private WifiManager wifiManager;

	private static class NetworkItem {
		public int networkId;
		public String ssid;

		private NetworkItem(int networkId, String ssid) {
			this.networkId = networkId;
			this.ssid = ssid;
		}

		@Override
		public String toString() {
			return ssid.replace("\"", "");
		}
	}

	public static EditWifiConnectActionDialog newInstance(int networkId, String ssid) {
		Bundle data = new Bundle();
		data.putInt(EXTRA_NETWORK_ID, networkId);
		data.putString(EXTRA_SSID, ssid);
		EditWifiConnectActionDialog dialog = new EditWifiConnectActionDialog();
		dialog.setArguments(data);
		return dialog;
	}

	public EditWifiConnectActionDialog() {
		// Required empty constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
		for (WifiConfiguration conf : wifiManager.getConfiguredNetworks()) {
			networks.add(new NetworkItem(conf.networkId, conf.SSID));
		}

		setCancelable(false);
		View view = inflater.inflate(R.layout.dialog_edit_wifi_connect, container, false);
		setupViews(view);
		return view;
	}

	private void setupViews(View rootView) {
		getDialog().setTitle("Connect to Specified Wifi");
		networkList = (ListView) rootView.findViewById(R.id.network_list);

		networkList.setAdapter(new ArrayAdapter<NetworkItem>(getActivity(),
				android.R.layout.simple_list_item_1, networks));

		networkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				NetworkItem selected = (NetworkItem) parent.getAdapter().getItem(position);
				if (listener != null) {
					listener.onActionChanged(action,
							new WifiConnectAction(selected.networkId, selected.ssid));
				}
				getDialog().dismiss();
			}
		});
	}

	public void setAction(WifiConnectAction action) {
		this.action = action;
	}

	public void setOnActionChangedListener(OnActionChangedListener listener) {
		this.listener = listener;
	}
}
