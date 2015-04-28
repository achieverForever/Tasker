package com.wilson.tasker.manager;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.wilson.tasker.conditions.TimeCondition;
import com.wilson.tasker.events.TimeEvent;
import com.wilson.tasker.receiver.TimerEventReceiver;

import de.greenrobot.event.EventBus;

public class TimerManager {

	private static TimerManager sInstance;

	private TimerManager() {
	}

	public static synchronized TimerManager getInstance() {
		if (sInstance == null) {
			sInstance = new TimerManager();
		}
		return sInstance;
	}

	public void scheduleTimer(Context context, TimeCondition condition) {
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, TimerEventReceiver.class);
		intent.putExtra("id", condition.id);
		if (System.currentTimeMillis() < condition.startMillis) {
			am.set(AlarmManager.RTC_WAKEUP, condition.startMillis, PendingIntent.getBroadcast(
					context, condition.id, intent, PendingIntent.FLAG_CANCEL_CURRENT));
			intent.putExtra("id", -condition.id);
			am.set(AlarmManager.RTC_WAKEUP, condition.endMillis, PendingIntent.getBroadcast(
					context, -condition.id, intent, PendingIntent.FLAG_CANCEL_CURRENT));
		}
	}

	public void unscheduleTimer(Context context, TimeCondition condition) {
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, TimerEventReceiver.class);
		am.cancel(PendingIntent.getBroadcast(context, condition.id, intent,
				PendingIntent.FLAG_CANCEL_CURRENT));
		am.cancel(PendingIntent.getBroadcast(context, -condition.id, intent,
				PendingIntent.FLAG_CANCEL_CURRENT));
	}

}

