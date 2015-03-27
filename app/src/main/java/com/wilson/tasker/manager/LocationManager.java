package com.wilson.tasker.manager;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDGeofence;
import com.baidu.location.BDLocationStatusCodes;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.wilson.tasker.events.AddGeofenceEvent;
import com.wilson.tasker.events.LocationEvent;

import de.greenrobot.event.EventBus;

public class LocationManager {
	public static final String TAG = "LocationManager";
	public static final int SECOND = 1000;
	public static final int MINUTE = 60 * SECOND;
	public static final int HOUR = 60 * MINUTE;

	/** 单例 */
	private static LocationManager sInstance;

	/** 百度定位SDK接口 */
	private LocationClient locationClient;

	/** 百度地理围栏服务接口 */
	private GeofenceClient geofenceClient;

	private LocationManager(Context context) {
		this.locationClient = new LocationClient(context.getApplicationContext());
		this.geofenceClient = new GeofenceClient(context.getApplicationContext());
	}

	public static synchronized LocationManager getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new LocationManager(context.getApplicationContext());
		}
		return sInstance;
	}

	public void register(Context context) {
		// No-op
	}

	public void unregister(Context context) {
		// 关闭定位
		stopLocationClient();
		// 关闭地理围栏服务
		stopGeofenceClient();
	}

	public void requestLocation() {
		locationClient.requestLocation();
	}

	public void handleAddGeofenceEvent(AddGeofenceEvent event) {
		BDGeofence geoFence = new BDGeofence.Builder()
			.setGeofenceId(event.geofenceId)
			.setCoordType(BDGeofence.COORD_TYPE_BD09LL)
			.setCircularRegion(event.longitude, event.latitude, BDGeofence.RADIUS_TYPE_SMALL)
			.setExpirationDruation(12 * HOUR)
			.build();

		geofenceClient.addBDGeofence(geoFence, new AddGeofencesResultListener());
		geofenceClient.registerGeofenceTriggerListener(new GeofenceTriggerListener());
	}

	private void stopLocationClient() {
		if (locationClient != null && locationClient.isStarted()) {
			locationClient.stop();
		}
	}

	private void stopGeofenceClient() {
		if (geofenceClient != null && geofenceClient.isStarted()) {
			geofenceClient.stop();
		}
	}

	public static class GeofenceTriggerListener implements GeofenceClient.OnGeofenceTriggerListener {
		@Override
		public void onGeofenceExit(String geofenceId) {
			EventBus.getDefault().post(new LocationEvent(geofenceId, LocationEvent.GEOFENCE_EXIT));
		}

		@Override
		public void onGeofenceTrigger(String geofenceId) {
			EventBus.getDefault().post(new LocationEvent(geofenceId, LocationEvent.GEOFENCE_ENTER));
		}
	}

	public static class AddGeofencesResultListener implements GeofenceClient.OnAddBDGeofencesResultListener {
		@Override
		public void onAddBDGeofencesResult(int statusCode, String geofenceId) {
			if (statusCode == BDLocationStatusCodes.SUCCESS) {
				Log.d(TAG, String.format("add geofence[id=%s] success", geofenceId));
			} else {
				Log.e(TAG, String.format("add geofence[id=%s] fail, statusCode=%d", geofenceId, statusCode));
			}
		}
	}

	public LocationClient getLocationClient() {
		return locationClient;
	}

	public GeofenceClient getGeofenceClient() {
		return geofenceClient;
	}
}
