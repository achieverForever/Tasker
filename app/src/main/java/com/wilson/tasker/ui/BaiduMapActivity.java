package com.wilson.tasker.ui;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDGeofence;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDLocationStatusCodes;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.wilson.tasker.R;
import com.wilson.tasker.app.TaskerApplication;
import com.wilson.tasker.events.AddGeofenceEvent;
import com.wilson.tasker.service.WorkerService;
import com.wilson.tasker.ui.dialogs.EditMarkerNameDialog;

import de.greenrobot.event.EventBus;

public class BaiduMapActivity extends ActionBarActivity
		implements EditMarkerNameDialog.OnMarkerNameEnteredListener {

	public static final String TAG = "Location";
	public static final int HOUR = 60 * 60 * 1000;
	public static final String KEY_LATITUDE = "latitude";
	public static final String KEY_LONGITUDE = "longitude";
	public static final String KEY_MARKER_NAME = "marker_name";

	private MapView mapView;
	private Button cancelBtn;
	private Button confirmBtn;

	private LocationClient locationClient;
	private BaiduMap baiduMap;
	private BitmapDescriptor markerIcon;
	private GeoCoder geoCoder;
	private Marker marker;
	private String markerName;
	private boolean isFirstLoc = true;
	private boolean hasCustomMarkerName = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baidu_map);

		mapView = (MapView) findViewById(R.id.baidu_map_view);
		cancelBtn = (Button) findViewById(R.id.cancel);
		confirmBtn = (Button) findViewById(R.id.confirm);

		baiduMap = mapView.getMap();
		locationClient = WorkerService.getLocationClient(getApplicationContext());
		geoCoder = GeoCoder.newInstance();

		hideZoomControls();
		setUpBaiduMap();
		setUpLocationClient();
		setUpViews();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
		// 回收Bitmap资源
		markerIcon.recycle();
		// 销毁地理编码器
		geoCoder.destroy();

	}

	private void setUpViews() {
		confirmBtn.setEnabled(false);
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});

		confirmBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addGeofence(marker.getPosition().longitude, marker.getPosition().latitude, markerName);

				Intent data = new Intent();
				data.putExtra(KEY_LATITUDE, marker.getPosition().latitude);
				data.putExtra(KEY_LONGITUDE, marker.getPosition().longitude);
				data.putExtra(KEY_MARKER_NAME, markerName);
				setResult(RESULT_OK, data);
				finish();
			}
		});
	}

	private void setUpBaiduMap() {
		// 开启定位图层
		baiduMap.setMyLocationEnabled(true);
		markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.map_marker);
		baiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
			@Override
			public void onMapLongClick(LatLng latLng) {
				clearOverlay();
				OverlayOptions options = new MarkerOptions().position(latLng)
						.icon(markerIcon).zIndex(9).draggable(true);
				marker = (Marker) (baiduMap.addOverlay(options));
				confirmBtn.setEnabled(true);

				handleLocationSelected(marker.getPosition());
			}
		});

		baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(final Marker marker) {
				Log.d(TAG, "marker click");
				EditMarkerNameDialog dialog = new EditMarkerNameDialog().newInstance(markerName);
				dialog.registerOnMarkerNameEnteredListener(BaiduMapActivity.this);
				dialog.show(getSupportFragmentManager(), "dialog");
				return true;
			}
		});

		baiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
			@Override
			public void onMarkerDragStart(Marker marker) {
			}

			@Override
			public void onMarkerDrag(Marker marker) {
			}

			@Override
			public void onMarkerDragEnd(Marker marker) {
				handleLocationSelected(marker.getPosition());
			}
		});
	}

	private void setUpLocationClient() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);            // 打开GPS
		option.setCoorType("bd09ll");       // 设置坐标类型
		option.setScanSpan(10000);          // 设置定位间隔
		option.setNeedDeviceDirect(false);  // 返回的定位结果包含手机机头的方向
		option.setIsNeedAddress(true);
		locationClient.setLocOption(option);

		locationClient.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation location) {
				// MapView销毁后不再处理新接收的位置
				if (location == null || mapView == null) {
					return;
				}
				MyLocationData locData = new MyLocationData.Builder()
						.accuracy(location.getRadius())
						.direction(100) // 此处设置开发者获取到的方向信息，顺时针0-360
						.latitude(location.getLatitude())
						.longitude(location.getLongitude())
						.build();
				baiduMap.setMyLocationData(locData);
				if (isFirstLoc) {
					isFirstLoc = false;
					LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
					baiduMap.animateMapStatus(u);
				}
			}
		});

		if (!locationClient.isStarted()) {
			locationClient.start();
		}
		locationClient.requestLocation();
	}

	private void clearOverlay() {
		baiduMap.clear();
	}

	private void hideZoomControls() {
		for (int i = 0; i < mapView.getChildCount(); i++) {
			View child = mapView.getChildAt(i);
			if (child instanceof ZoomControls) {
				child.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onMarkerNameEntered(final String markerNameStr) {
		hasCustomMarkerName = true;
		markerName = markerNameStr;
		showInfoWindow();
	}

	private void handleLocationSelected(LatLng location) {
		if (!hasCustomMarkerName) {
			OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
				@Override
				public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
				}

				@Override
				public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
					if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
						// 没有找到检索结果
						markerName = "";
					} else {
						// 获取反地理编码结果
						markerName = result.getAddress();
					}
					Log.d(TAG, "reverse geocode result: " + result.getAddress());
					showInfoWindow();
				}
			};

			reverseGeoCode(marker.getPosition(), listener);
		} else {
			showInfoWindow();
		}
	}

	/**
	 * 显示Marker Info弹出框
	 */
	private void showInfoWindow() {
		View markerInfoView = getLayoutInflater().inflate(R.layout.marker_info_view, null);
		TextView markerNameView = (TextView) markerInfoView.findViewById(R.id.marker_name);
		markerNameView.setText(markerName);

		InfoWindow.OnInfoWindowClickListener listener =
				new InfoWindow.OnInfoWindowClickListener() {
					@Override
					public void onInfoWindowClick() {
						EditMarkerNameDialog dialog = new EditMarkerNameDialog().newInstance(markerName);
						dialog.registerOnMarkerNameEnteredListener(BaiduMapActivity.this);
						dialog.show(getSupportFragmentManager(), "dialog");
					}
				};

		InfoWindow infoWindow =
				new InfoWindow(BitmapDescriptorFactory.fromView(markerInfoView),
						marker.getPosition(), -120, listener);
		baiduMap.showInfoWindow(infoWindow);
	}

	/**
	 * 异步获取反地理编码结果
	 *
	 * @param location 地理位置点
	 * @param listener 反地理编码结果监听器
	 */
	private void reverseGeoCode(LatLng location, OnGetGeoCoderResultListener listener) {
		geoCoder.setOnGetGeoCodeResultListener(listener);
		geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(location));
	}

	private void addGeofence(double longitude, double latitude, String geofenceId) {
		EventBus.getDefault().post(new AddGeofenceEvent(longitude, latitude, geofenceId));
	}
}
