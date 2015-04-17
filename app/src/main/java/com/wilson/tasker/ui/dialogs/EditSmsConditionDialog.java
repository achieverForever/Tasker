package com.wilson.tasker.ui.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gc.materialdesign.views.ButtonFlat;
import com.wilson.tasker.R;
import com.wilson.tasker.conditions.SmsCondition;
import com.wilson.tasker.listeners.OnConditionChangedListener;
import com.wilson.tasker.utils.Utils;

public class EditSmsConditionDialog extends DialogFragment implements View.OnClickListener {
	private static final int REQUEST_CODE_PICK_CONTACT = 2;
	private static final String EXTRA_SENDER_NUMBER = "com.wilson.tasker.sender_number";
	private static final String EXTRA_CONTENT = "com.wilson.tasker.content";

	private Button browseBtn;
	private ButtonFlat confirmBtn;
	private EditText senderNumber;
	private EditText content;
	private SmsCondition condition;
	private OnConditionChangedListener listener;

	public static EditSmsConditionDialog newInstance(String senderNumber, String content) {
		Bundle data = new Bundle();
		data.putString(EXTRA_SENDER_NUMBER, senderNumber);
		data.putString(EXTRA_CONTENT, content);
		EditSmsConditionDialog dialog = new EditSmsConditionDialog();
		dialog.setArguments(data);
		return dialog;
	}

	public EditSmsConditionDialog() {
		// Required empty constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_edit_sms, container, false);
		setupViews(view);
		return view;
	}

	private void setupViews(View rootView) {
		getDialog().setTitle("Edit Sms Condition");
		senderNumber = (EditText) rootView.findViewById(R.id.sender_number);
		content = (EditText) rootView.findViewById(R.id.sms_content);
		browseBtn = (Button) rootView.findViewById(R.id.browse_contacts);
		confirmBtn = (ButtonFlat) rootView.findViewById(R.id.confirm);

		confirmBtn.setOnClickListener(this);
		browseBtn.setOnClickListener(new OnBrowseContactsClickListener());
		senderNumber.setText(getArguments().getString(EXTRA_SENDER_NUMBER, ""));
		senderNumber.setSelection(getArguments().getString(EXTRA_SENDER_NUMBER, "").length());
		content.setText(getArguments().getString(EXTRA_CONTENT, ""));
		content.setSelection(getArguments().getString(EXTRA_CONTENT, "").length());
	}

	public void setCondition(SmsCondition condition) {
		this.condition = condition;
	}

	public void setOnConditionChangedListener(OnConditionChangedListener listener) {
		this.listener = listener;
	}

	@Override
	public void onClick(View v) {
		if (listener != null) {
			listener.onConditionChanged(condition,
					new SmsCondition(senderNumber.getText().toString(),
							content.getText().toString()));
		}
		getDialog().dismiss();
	}

	private class OnBrowseContactsClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, REQUEST_CODE_PICK_CONTACT);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PICK_CONTACT) {
			String DEBUG_TAG = "test";
			Cursor cursor = null;
			String phoneNumber = "";
			String contactName = "";
			int phone_column_index = 0;

			try {
				Uri contactData = data.getData();
				Cursor c = getActivity().getContentResolver()
						.query(contactData, null, null, null, null);
				if (c.moveToFirst()) {
					String id = c.getString(c.getColumnIndexOrThrow(
							ContactsContract.Contacts._ID));
					String hasPhone = c.getString(c.getColumnIndex(
							ContactsContract.Contacts.HAS_PHONE_NUMBER));

					if (hasPhone.equalsIgnoreCase("1")) {
						Cursor phones = getActivity().getContentResolver().query(
								ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
								ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
								null, null);
						phones.moveToFirst();
						phoneNumber = phones.getString(phones.getColumnIndex("data1"));
						contactName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					}

				}
				if (!phoneNumber.equals("")) {
					senderNumber.setText(String.format("%s(%s)", phoneNumber, contactName));
				}
			} catch (Exception e) {
				Log.e(Utils.LOG_TAG, "error querying contacts' phone number");
			} finally {
				 if (cursor != null) {
					 cursor.close();
				 }
			}
			Log.d(DEBUG_TAG, data.getData().toString());
		}
	}
}
