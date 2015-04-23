package com.wilson.tasker.actions;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilson.tasker.R;
import com.wilson.tasker.manager.FontManager;
import com.wilson.tasker.manager.SmsManager;
import com.wilson.tasker.model.Action;
import com.wilson.tasker.ui.RefreshBrightnessActivity;

public class SendSmsAction extends Action {
	public String smsTo;
	public String content;

	public SendSmsAction(String smsTo, String content) {
		super(TYPE_SEND_SMS, "Send SMS", R.drawable.ic_send_sms);
		this.smsTo = smsTo;
		this.content = content;
	}

	@Override
	public boolean performAction(Context context) {
		SmsManager.getInstance(context).sendSms(smsTo, content);
		return true;
	}

	@Override
	public View getView(Context context, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.list_item_action_common, parent, false);
		TextView name = (TextView) view.findViewById(R.id.name);
		ImageView icon = (ImageView) view.findViewById(R.id.icon);
		final TextView desc = (TextView) view.findViewById(R.id.desc);

		name.setText("Send SMS");
		name.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		if (iconRes != 0) {
			icon.setImageResource(iconRes);
		}
		desc.setText(String.format("Send SMS to %s", smsTo));
		desc.setTypeface(FontManager.getsInstance().loadFont(context, "fonts/Roboto-Light.ttf"));
		return view;
	}
}
