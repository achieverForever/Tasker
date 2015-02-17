package com.wilson.tasker.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.wilson.tasker.events.SmsEvent;

import de.greenrobot.event.EventBus;

public class SmsManager {
	private Context context;
	private boolean registered;
	private static SmsManager sInstance;

	private SmsManager(Context context) {
		this.context = context;
	}

	public static synchronized SmsManager getsInstance(Context context) {
		if (sInstance == null) {
			sInstance = new SmsManager(context.getApplicationContext());
		}
		return sInstance;
	}

	public void register() {
		IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		context.registerReceiver(smsReceiver, intentFilter);
		registered = true;
	}

	public void unregister() {
		context.unregisterReceiver(smsReceiver);
		registered = false;
	}

	public boolean isRegistered() {
		return registered;
	}

	public BroadcastReceiver smsReceiver = new BroadcastReceiver() {
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
					Log.d("Tasker", "msg_from=" + msgFrom + ", msg_body=" + sb.toString());
					EventBus.getDefault().post(new SmsEvent(msgFrom, sb.toString()));
				}
			}
		}
	};
}
