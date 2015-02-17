package com.wilson.tasker.manager;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

/**
 * 设置响铃
 */
public class RingtoneManager {
	public static final int REQUEST_CODE_SET_RINGTONE = 100;
	public static final int REQUEST_CODE_SET_NOTIFICATION = 200;

	private AudioManager audioManager;
	private static RingtoneManager sInstance;

	private RingtoneManager(Context context) {
		audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	}

	public static RingtoneManager getsInstance(Context context) {
		if (sInstance == null) {
			sInstance = new RingtoneManager(context.getApplicationContext());
		}
		return sInstance;
	}

	/**
	 * 设置响铃模式
	 *
	 * @param ringerMode 响铃模式，取值为RINGER_MODE_NORMAL, RINGER_MODE_SILENT或者RINGER_MODE_VIBRATE.
	 */
	public void setRingerMode(int ringerMode) {
		audioManager.setRingerMode(ringerMode);
	}

	//TODO - fixme (require the Activity to call startActivityForResult() and get the returned URI in onActivityResult())
	public static Intent obtainSetSoundIntent(int type) {
		Intent intent = new Intent(android.media.RingtoneManager.ACTION_RINGTONE_PICKER);
		intent.putExtra(android.media.RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);
		intent.putExtra(android.media.RingtoneManager.EXTRA_RINGTONE_TITLE, "铃声");
		intent.putExtra(android.media.RingtoneManager.EXTRA_RINGTONE_TYPE, type);
		return intent;
	}
}
