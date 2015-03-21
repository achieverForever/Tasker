package com.wilson.tasker.manager;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.wilson.tasker.conditions.OrientationCondition;
import com.wilson.tasker.events.OrientationEvent;

import de.greenrobot.event.EventBus;

public class OrientationManager implements SensorEventListener {

	public static final int ORIENTATION_UNKNOWN = -1;
	public static final int ORIENTATION_FACE_UP = 1;
	public static final int ORIENTATION_FACE_DOWN = 4;

	private int lastOrientation = ORIENTATION_UNKNOWN;
	private int lastReportOrientation = ORIENTATION_UNKNOWN;

	private float[] rotationMatrix = new float[16];
	private float[] orientationVector = new float[3];

	private SensorManager sensorManager;
	private boolean registered;

	private static OrientationManager sInstance;

	private OrientationManager(Context context) {
		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
	}

	public static synchronized OrientationManager getsInstance(Context context) {
		if (sInstance == null) {
			sInstance = new OrientationManager(context.getApplicationContext());
		}
		return sInstance;
	}

	public void register() {
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
				SensorManager.SENSOR_DELAY_NORMAL);
		registered = true;
	}

	public void unregister() {
		sensorManager.unregisterListener(this);
		registered = false;
	}

	public boolean isRegistered() {
		return registered;
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		if (sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
			SensorManager.getRotationMatrixFromVector(rotationMatrix, sensorEvent.values);
			determineOrientation(rotationMatrix);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int i) {
	}

	private void determineOrientation(float[] rotationMatrix) {
		SensorManager.getOrientation(rotationMatrix, orientationVector);
		double azimuth = Math.toDegrees((orientationVector[0]));
		double pitch = Math.toDegrees((orientationVector[1]));
		double roll = Math.toDegrees((orientationVector[2]));

		if (pitch <= 10) {
			if (Math.abs(roll) >= 170) {
				onFaceDown();
			} else if (Math.abs(roll) <= 10) {
				onFaceUp();
			} else {
				lastOrientation = ORIENTATION_UNKNOWN;
			}
		} else {
			lastOrientation = ORIENTATION_UNKNOWN;
		}
	}

	private void onFaceUp() {
		if (lastOrientation != ORIENTATION_FACE_UP && lastReportOrientation != ORIENTATION_FACE_UP) {
			lastOrientation = ORIENTATION_FACE_UP;
			lastReportOrientation = ORIENTATION_FACE_UP;
			EventBus.getDefault().post(new OrientationEvent(ORIENTATION_FACE_UP));
//			Log.d("Tasker", "face up");
		}
	}

	private void onFaceDown() {
		if (lastOrientation != ORIENTATION_FACE_DOWN && lastReportOrientation != ORIENTATION_FACE_DOWN) {
			lastOrientation = ORIENTATION_FACE_DOWN;
			lastReportOrientation = ORIENTATION_FACE_DOWN;
			EventBus.getDefault().post(new OrientationEvent(ORIENTATION_FACE_DOWN));
//			Log.d("Tasker", "face down");
		}
	}
}
