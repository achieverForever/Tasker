package com.wilson.tasker.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.baidu.location.BDGeofence;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.wilson.tasker.R;
import com.wilson.tasker.app.TaskerApplication;

public class LocationActivity extends ActionBarActivity {
	private static final String TAG = "Location";

	public static final int SECOND = 1000;

	TextView locationText;
	LocationClient locationClient;
	GeofenceClient geofenceClient;
	BDLocation location;
	BDGeofence geofence;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		locationText = (TextView) findViewById(R.id.location);
		locationClient = ((TaskerApplication) getApplication()).locationClient;
		geofenceClient = ((TaskerApplication) getApplication()).geofenceClient;
		// TODO - 支持地图上选点划定Geofence
		geofence = new BDGeofence.Builder()
			.setGeofenceId("geo-1")
			.setCircularRegion(0, 0, BDGeofence.RADIUS_TYPE_SMALL)
			.setExpirationDruation(3600 * SECOND)
			.setCoordType("bd09ll")
			.build();
	}

	@Override
	protected void onStart() {
		super.onStart();
		geofenceClient.start();
	}

	@Override
	protected void onStop() {
		super.onStop();
		geofenceClient.stop();
	}

	public class LocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\ndirection : ");
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				//运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
			}
			Log.d("Location", sb.toString());
		}
	};

	public class AddGeofenceListener implements GeofenceClient.OnAddBDGeofencesResultListener {
		@Override
		public void onAddBDGeofencesResult(int statusCode, String geofenceId) {
			Log.d("Location", "add geofence result: status=" + statusCode +
				", geofenceId=" + geofenceId);
		}
	}

	public class RemoveGeofenceListener implements GeofenceClient.OnRemoveBDGeofencesResultListener {
		@Override
		public void onRemoveBDGeofencesByRequestIdsResult(int statusCode, String[] geofenceIds) {
			Log.d("Location", "add geofence result: status=" + statusCode +
				", geofenceIds=" + geofenceIds);
		}
	}

	public class GeofenceEnterListener implements GeofenceClient.OnGeofenceTriggerListener {
		@Override
		public void onGeofenceTrigger(String geofenceId) {
			Log.d("Location", "enter geofence: " + geofenceId);
		}

		@Override
		public void onGeofenceExit(String geofenceId) {
			Log.d("Location", "exit geofence: " + geofenceId);
		}
	}




}
