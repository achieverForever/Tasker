package com.wilson.tasker.ui;


import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wilson.tasker.R;

import java.util.ArrayList;
import java.util.List;

public class WifiFragment extends Fragment {
	private List<NetworkItem> networks;
	private WifiManager wifiManager;
	private ListView networkList;

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

	public static WifiFragment newInstance() {
		WifiFragment fragment = new WifiFragment();
		return fragment;
	}

	public WifiFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
		for (WifiConfiguration conf : wifiManager.getConfiguredNetworks()) {
			if (networks == null) {
				networks = new ArrayList<>();
			}
			networks.add(new NetworkItem(conf.networkId, conf.SSID));
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_wifi, container, false);
		initView(view);
		return view;
	}

	public void initView(View root) {
		networkList = (ListView) root.findViewById(R.id.lv_network_list);
		networkList.setAdapter(new ArrayAdapter<NetworkItem>(getActivity(),
			android.R.layout.simple_list_item_1, networks));
		networkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				NetworkItem selected = networks.get(position);
				wifiManager.disconnect();
				boolean ret = wifiManager.enableNetwork(selected.networkId, true);
				Log.d("WifiFragment", "enable network " + selected.ssid + " returned " + ret);
			}
		});
	}


}
