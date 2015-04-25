package com.wilson.tasker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wilson.tasker.R;
import com.wilson.tasker.dao.SceneActivity;
import com.wilson.tasker.manager.FontManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimelineListAdapter extends BaseAdapter {

	private List<SceneActivity> activities;
	private Context context;
	private LayoutInflater inflater;


	public TimelineListAdapter(Context context, List<SceneActivity> activities) {
		this.context = context;
		this.activities = activities;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return activities.size();
	}

	@Override
	public Object getItem(int position) {
		return activities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SceneActivity item = (SceneActivity) getItem(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_activity, parent, false);
		}
		TextView time = (TextView) convertView.findViewById(R.id.time);
		TextView info = (TextView) convertView.findViewById(R.id.info);
		TextView detailInfo = (TextView) convertView.findViewById(R.id.detail_info);

		String activated = item.getActionType() == 0 ? "activated" : "deactivated";
		if (isRecentTime(item.getTime())) {
			time.setText(new SimpleDateFormat("EEE H:m").format(item.getTime()));
		} else {
			long days = TimeUnit.DAYS.convert(new Date().getTime() - item.getTime().getTime(),
					TimeUnit.MILLISECONDS);
			time.setText(String.format("%d天前 ", days) +
					new SimpleDateFormat("H:m").format(item.getTime()));
		}
		info.setText("Scene " + activated);
		detailInfo.setText("Scene " + item.getSceneName()+ " is " + activated);
		detailInfo.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));

		return convertView;
	}

	private boolean isRecentTime(Date time) {
		Calendar calendar = Calendar.getInstance();
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		long days = TimeUnit.DAYS.convert(calendar.getTime().getTime() - time.getTime(), TimeUnit.MILLISECONDS);
		return days < dayOfWeek;
	}
}
