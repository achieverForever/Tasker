package com.wilson.tasker.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.wilson.tasker.R;
import com.wilson.tasker.model.Condition;

public class BaseFragment extends Fragment {
	protected ImageView ivSave;
	protected ImageView ivDontSave;
	protected Condition condition;
	private OnConditionSaveListener listener;

	public BaseFragment() {
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ivSave = (ImageView) view.findViewById(R.id.iv_save);
		ivDontSave = (ImageView) view.findViewById(R.id.iv_dont_save);

		ivSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (listener != null) {
					listener.onSaveCondition(onPreSaveCondition());
				}
			}
		});

		ivDontSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (listener != null) {
					listener.onDontSaveCondition(condition);
				}
			}
		});
	}

	protected Condition onPreSaveCondition() {
		return condition;
	}

	public interface OnConditionSaveListener {
		public void onSaveCondition(Condition condition);
		public void onDontSaveCondition(Condition condition);
	}

	public void setOnConditionSaveListener(OnConditionSaveListener listener) {
		this.listener = listener;
	}
}
