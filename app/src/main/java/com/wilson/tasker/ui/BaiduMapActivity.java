package com.wilson.tasker.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
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
import com.wilson.tasker.R;
import com.wilson.tasker.app.TaskerApplication;

public class BaiduMapActivity extends ActionBarActivity {
	public static final String TAG = "Location";

	private MapView mapView;
	private LocationClient locationClient;
	private BaiduMap baiduMap;
	private BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
	private Marker marker;
	private boolean isFirstLoc = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baidu_map);

		mapView = (MapView) findViewById(R.id.baidu_map_view);
		baiduMap = mapView.getMap();
		locationClient = ((TaskerApplication) getApplication()).locationClient;
		setUpBaiduMap();
		setUpLocationClient();
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
		markerIcon.recycle();   // 回收Bitmap资源
		locationClient.stop();
	}

	private void setUpBaiduMap() {
		// 开启定位图层
		baiduMap.setMyLocationEnabled(true);

		baiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener(){
			@Override
			public void onMapLongClick(LatLng latLng) {
				baiduMap.clear();
				OverlayOptions options = new MarkerOptions().position(latLng)
					.icon(markerIcon).zIndex(9).draggable(true);
				marker = (Marker) (baiduMap.addOverlay(options));
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
				Log.d(TAG, "拖拽结束，新位置：" + marker.getPosition().latitude + ", "
					+ marker.getPosition().longitude);
			}

		});

		baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				Log.d(TAG, "marker click");
				EditText markerName = new EditText(getApplicationContext());
				markerName.setText("Home");
				markerName.setBackgroundResource(R.drawable.popup);
				InfoWindow.OnInfoWindowClickListener listener =
					new InfoWindow.OnInfoWindowClickListener() {
						@Override
						public void onInfoWindowClick() {
						}
				};
				InfoWindow infoWindow =
					new InfoWindow(BitmapDescriptorFactory.fromView(markerName),
						marker.getPosition(), -47, listener);
				baiduMap.showInfoWindow(infoWindow);
				return true;
			}
		});
	}

	private void setUpLocationClient() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);        // 打开GPS
		option.setCoorType("bd09ll");   // 设置坐标类型
		option.setScanSpan(1000);       // 设置定位间隔
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
	}

}
