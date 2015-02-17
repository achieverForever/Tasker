package com.wilson.tasker.manager;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class JobScheduler {
	private static final String TAG = "Tasker";
	private static final JobScheduler sInstance = new JobScheduler();
	private final ScheduledExecutorService pool;
	private final Map<Runnable, ScheduledFuture<?>> actionsMap;

	private JobScheduler() {
		pool = Executors.newScheduledThreadPool(2);
		actionsMap = new HashMap<Runnable, ScheduledFuture<?>>(4);
	}

	public static JobScheduler getInstance() {
		return sInstance;
	}

	public Future<?> runImmediate(Runnable action) {
		return pool.submit(action);
	}

	public ScheduledFuture<?> schedule(Runnable action, long delay, TimeUnit timeUnit) {
		ScheduledFuture<?> sf = pool.schedule(action, delay, timeUnit);
		actionsMap.put(action, sf);
		return sf;
	}

	public ScheduledFuture<?> scheduleAtFixRate(Runnable action, long period, TimeUnit timeUnit) {
		ScheduledFuture<?> sf = null;
		try {
			sf = pool.scheduleAtFixedRate(action, 0, period, timeUnit);
		} catch (RejectedExecutionException ex) {
			Log.e(TAG, "Failed to schedule job [Rejected]" + ex.getMessage());
		}
		if (sf != null) {
			actionsMap.put(action, sf);
		}
		return sf;
	}

	public boolean removeCallback(Runnable action) {
		ScheduledFuture<?> sf = actionsMap.get(action);
		if (sf != null) {
			return sf.cancel(true);
		}
		return false;
	}

	public void cancelAllTasks() {
		if (!pool.isShutdown()) {
			pool.shutdown();
		}
	}
}
