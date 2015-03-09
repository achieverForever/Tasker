package com.wilson.tasker.ui;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.wilson.tasker.R;

public class LocationActivity extends ActionBarActivity implements LocationListener {
	private static final String TAG = "Location";

	TextView locationText;
	LocationManager locationManager;
	Location location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		locationText = (TextView) findViewById(R.id.location);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);

		String bestProvider = locationManager.getBestProvider(criteria, true);
		location = locationManager.getLastKnownLocation(bestProvider);
		updateWithNewLocation(location);
		locationManager.requestLocationUpdates(bestProvider, 10000, 0, this);
	}

	@Override
	public void onLocationChanged(Location location) {
		this.location = location;
		updateWithNewLocation(location);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.d(TAG, provider + " status=" + status);
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d(TAG, provider + " enabled");
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.d(TAG, provider + " disabled");
	}

	private void updateWithNewLocation(Location location) {
		if (location == null) {
			locationText.setText("No location");
		} else {
			locationText.setText(String.format("Lat: %f, Lgt: %f", location.getLatitude(), location.getLongitude()));
		}
	}

}
