package com.wilson.tasker.ui;

import java.io.IOException;

import com.wilson.tasker.R;
import com.wilson.tasker.manager.AirplaneModeEnabler;
import com.wilson.tasker.manager.BatteryLevelMonitor;
import com.wilson.tasker.manager.BluetoothEnabler;
import com.wilson.tasker.manager.DisplayManager;
import com.wilson.tasker.manager.OrientationManager;
import com.wilson.tasker.manager.PhoneCallManager;
import com.wilson.tasker.manager.RingtoneManager;
import com.wilson.tasker.manager.SmsManager;
import com.wilson.tasker.manager.WifiManager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;

public class TestActivity extends Activity {
	private static final String TAG = "DEBUG";

	private Switch wifi;
	private Switch bluetooth;
	private Switch airplaneMode;
	private SeekBar brightness;
	private Button wallpaper;
	private EditText timeOut;
	private Button screenOffTimeOut;
	private Button battery;
	private Switch scheduleOneshot;
	private Switch scheduleFixedRate;
	private Switch orientation;
	private Switch caller;
	private Switch sms;
	private Button ringtone;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		wifi = (Switch) findViewById(R.id.wifi);
		bluetooth = (Switch) findViewById(R.id.bluetooth);
		airplaneMode = (Switch) findViewById(R.id.airplane_mode);
		brightness = (SeekBar) findViewById(R.id.brightness);
		wallpaper = (Button) findViewById(R.id.set_wallpaper);
		timeOut = (EditText) findViewById(R.id.timeout);
		screenOffTimeOut = (Button) findViewById(R.id.screen_off_timeout);
		battery = (Button) findViewById(R.id.battery_level);
		orientation = (Switch) findViewById(R.id.orientation);
		caller = (Switch) findViewById(R.id.caller);
		sms = (Switch) findViewById(R.id.sms);
		ringtone = (Button) findViewById(R.id.ringtone);

		final WifiManager wifiManager = WifiManager.getsInstance(this);
		final BluetoothEnabler bluetoothEnabler = BluetoothEnabler.getsInstance();
		final AirplaneModeEnabler airplaneModeEnabler = AirplaneModeEnabler.getInstance(this);
		final DisplayManager displayManager = DisplayManager.getsInstance(this);
		final BatteryLevelMonitor batteryLevelMonitor = BatteryLevelMonitor.getInstance(this);
		final OrientationManager orientationManager = OrientationManager.getsInstance(this);
		final PhoneCallManager phoneCallManager = PhoneCallManager.getsInstance(this);
		final SmsManager smsManager = SmsManager.getsInstance(this);
		final RingtoneManager ringtoneManager = RingtoneManager.getsInstance(this);

		wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				wifiManager.setWifiEnabled(b);
			}
		});

		bluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				bluetoothEnabler.setBluetoothEnabled(b);
			}
		});

		airplaneMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				airplaneModeEnabler.setAirplaneModeOn(b);
			}
		});

		brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
				int newBrightness = (int) (20 + (255 - 20) * i * 0.01f);
				Log.d(TAG, "value=" + i + ", newBrightness=" + newBrightness);
				displayManager.setBrightness(TestActivity.this, newBrightness);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});

		wallpaper.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				try {
					displayManager.setWallpaper(TestActivity.this.getAssets().open("wallpaper.jpg"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		screenOffTimeOut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				int time = Integer.parseInt(timeOut.getText().toString());
				Log.d(TAG, "timeout=" + time);
				displayManager.setScreenOffTimeout(time);
			}
		});

		battery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				float percentage = batteryLevelMonitor.getCurrentBatteryLevel();
				Log.d(TAG, "current battery level=" + percentage);
			}
		});

		orientation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if (b) {
					orientationManager.register();
				} else {
					orientationManager.unregister();
				}
			}
		});

		caller.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if (b) {
					phoneCallManager.register();
				} else {
					phoneCallManager.unregister();
				}
			}
		});

		sms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if (b) {
					smsManager.register();
				} else {
					smsManager.unregister();
				}
			}
		});

		ringtone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = RingtoneManager.obtainSetSoundIntent(android.media.RingtoneManager.TYPE_RINGTONE);
				TestActivity.this.startActivityForResult(intent, RingtoneManager.REQUEST_CODE_SET_RINGTONE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RingtoneManager.REQUEST_CODE_SET_RINGTONE && resultCode == RESULT_OK) {
			// Set ringtone to the returned Uri
			Uri uri = data.getParcelableExtra(android.media.RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
			if (uri != null) {
				String ringTonePath = uri.toString();
				android.media.RingtoneManager.setActualDefaultRingtoneUri(this,
						android.media.RingtoneManager.TYPE_RINGTONE, uri);
				Log.d(TAG, "ringtone Uri=" + ringTonePath);
			} else {
				Settings.System.putString(this.getContentResolver(), Settings.System.RINGTONE, null);
				Log.d(TAG, "silence selected");
			}
		} else if (requestCode == RingtoneManager.REQUEST_CODE_SET_NOTIFICATION && resultCode == RESULT_OK) {
			// Set notification to the returned Uri
			Uri uri = data.getParcelableExtra(android.media.RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
			if (uri != null) {
				String notiPath = uri.toString();
				android.media.RingtoneManager.setActualDefaultRingtoneUri(this,
						android.media.RingtoneManager.TYPE_NOTIFICATION, uri);
				Log.d(TAG, "ringtone Uri=" + notiPath);
			} else {
				Settings.System.putString(this.getContentResolver(), Settings.System.NOTIFICATION_SOUND, null);
				Log.d(TAG, "silence selected");
			}
		}
	}
}
