package com.wilson.tasker.app;

import android.app.Application;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.wilson.tasker.actions.BluetoothAction;
import com.wilson.tasker.conditions.TopAppCondition;
import com.wilson.tasker.manager.SceneManager;
import com.wilson.tasker.model.Action;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskerApplication extends Application {

	public LocationClient locationClient;
	public GeofenceClient geofenceClient;

	@Override
	public void onCreate() {
		Log.d("TaskerApplication", "onCreate()");
		super.onCreate();

		locationClient = new LocationClient(getApplicationContext());
		geofenceClient = new GeofenceClient(getApplicationContext());

		setUpLocationClient();

//		startService(new Intent(this, WorkerService.class));
		initDefaultScenes();
	}



	private void initDefaultScenes() {
		// TODO - 默认的几个Scene
		List<Condition> emptyConditions = new ArrayList<>();
		List<Action> emptyActions = new ArrayList<>();
		List<Condition> batteryConditions = new ArrayList<>();
		List<Action> batteryActions = new ArrayList<>();
		batteryConditions.add(new TopAppCondition("com.android.chrome"));
		batteryActions.add(new BluetoothAction(true));
		Scene batteryScene = new Scene("battery", "Battery", batteryConditions, batteryActions);
		Scene homeScene = new Scene("home", "Home", emptyConditions, emptyActions);
		Scene sleepScene = new Scene("sleep", "Sleep", emptyConditions, emptyActions);
		SceneManager.getInstance().addScene(batteryScene, this);
		SceneManager.getInstance().addScene(homeScene, this);
		SceneManager.getInstance().addScene(sleepScene, this);
	}

	private BDLocationListener locationListener = new BDLocationListener() {
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

	private void setUpLocationClient() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy); //设置定位模式
		option.setCoorType("bd09ll"); //返回的定位结果是百度经纬度
		option.setScanSpan(5000); //设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true); //返回的定位结果包含地址信息
		option.setNeedDeviceDirect(false); //返回的定位结果包含手机机头的方向
		locationClient.setLocOption(option);
	}
}
