package com.wilson.tasker.ui.wiget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.wilson.tasker.R;

public class FillableCircleView extends TextView {

	private static final int TRANSITION_DURATION_MS = 500;

	private TransitionDrawable fillTransition;
	private Drawable unfillDrawable;

	private boolean isFilled = false;

	public FillableCircleView(Context context) {
		super(context);
		init(context);
	}

	public FillableCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public FillableCircleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		fillTransition = (TransitionDrawable) context.getResources()
			.getDrawable(R.drawable.circle_fill_transition);
		unfillDrawable = context.getResources()
			.getDrawable(R.drawable.colored_circle_icon_unfilled);
		unfill();
	}

	public void fill(boolean animated) {
		setBackgroundDrawable(fillTransition);
		if (!animated) {
			fillTransition.startTransition(0);
		} else {
			fillTransition.startTransition(TRANSITION_DURATION_MS);
			postDelayed(setTextColorRunnable, TRANSITION_DURATION_MS);
		}
	}

	public void unfill() {
		removeCallbacks(setTextColorRunnable);
		setBackgroundDrawable(unfillDrawable);
		setTextColor(getResources().getColor(R.color.light_blue));
	}

	public void toggle() {
		if (isFilled) {
			unfill();
		} else {
			fill(true);
		}
		isFilled = !isFilled;
	}

	private Runnable setTextColorRunnable = new Runnable() {
		@Override
		public void run() {
			setTextColor(Color.WHITE);
		}
	};
}
