package com.wilson.tasker.manager;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.wilson.tasker.events.CallerEvent;

import de.greenrobot.event.EventBus;

public class PhoneCallManager extends PhoneStateListener {

	private Context context;
	private TelephonyManager telephonyManager;
	private boolean isRegistered;
	private static PhoneCallManager sInstance;

	private PhoneCallManager(Context context) {
		this.context = context;
		this.telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	}

	public static synchronized PhoneCallManager getsInstance(Context context) {
		if (sInstance == null) {
			sInstance = new PhoneCallManager(context.getApplicationContext());
		}
		return sInstance;
	}

	public void register() {
		if (!isRegistered) {
			telephonyManager.listen(this, PhoneStateListener.LISTEN_CALL_STATE);
			isRegistered = true;
		}
	}

	public void unregister() {
		if (isRegistered) {
			telephonyManager.listen(this, 0);
			isRegistered = false;
		}
	}

	public boolean isRegistered() {
		return isRegistered;
	}

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		super.onCallStateChanged(state, incomingNumber);
		if (state == TelephonyManager.CALL_STATE_RINGING) {
			Log.d("Tasker", "state=" + state + "  incoming number=" + incomingNumber);

			EventBus.getDefault().post(new CallerEvent(incomingNumber));
		}
	}
}
