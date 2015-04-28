package com.wilson.tasker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.wilson.tasker.events.TimeEvent;
import com.wilson.tasker.utils.Utils;

import de.greenrobot.event.EventBus;

public class TimerEventReceiver extends BroadcastReceiver {
	public TimerEventReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(Utils.LOG_TAG, "TimerEventReceiver id=" + intent.getIntExtra("id", 0));
		int id = intent.getIntExtra("id", 0);
		int type = id > 0 ? TimeEvent.TYPE_START :  TimeEvent.TYPE_END;
		notifyTimerEvent(id, type);
	}


	private void notifyTimerEvent(int id, int type) {
		EventBus.getDefault().post(new TimeEvent(id, type));
	}
}
