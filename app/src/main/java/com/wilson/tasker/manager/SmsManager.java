package com.wilson.tasker.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.wilson.tasker.events.SmsEvent;
import com.wilson.tasker.utils.Utils;

import de.greenrobot.event.EventBus;

public class SmsManager {
	private Context context;
	private boolean isRegistered;
	private static SmsManager sInstance;

	private SmsManager(Context context) {
		this.context = context;
	}

	public static synchronized SmsManager getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new SmsManager(context.getApplicationContext());
		}
		return sInstance;
	}

	public synchronized void register() {
		if (!isRegistered) {
			Log.d(Utils.LOG_TAG, "register SmsManager");

			IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
			context.registerReceiver(smsReceiver, intentFilter);
			isRegistered = true;
		}
	}

	public synchronized void unregister() {
		if (isRegistered) {
			Log.d(Utils.LOG_TAG, "unregister SmsManager");

			context.unregisterReceiver(smsReceiver);
			isRegistered = false;
		}
	}

	public boolean isRegistered() {
		return isRegistered;
	}

	public void sendSms(String number, String content) {
		android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
		smsManager.sendTextMessage(number, null, content, null, null);
	}

	private BroadcastReceiver smsReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			SmsMessage[] msgs = null;
			String msgFrom = "";
			if (bundle != null) {
				Object[] pdus = (Object[]) bundle.get("pdus");
				if (pdus != null) {
					msgs = new SmsMessage[pdus.length];
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < msgs.length; i++) {
						msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
						msgFrom = msgs[i].getOriginatingAddress();
						String msgBody = msgs[i].getMessageBody();
						sb.append(msgBody);
					}
					Log.d(Utils.LOG_TAG, "msg_from=" + msgFrom + ", msg_body=" + sb.toString());

					EventBus.getDefault().post(new SmsEvent(msgFrom, sb.toString()));
				}
			}
		}
	};
}
