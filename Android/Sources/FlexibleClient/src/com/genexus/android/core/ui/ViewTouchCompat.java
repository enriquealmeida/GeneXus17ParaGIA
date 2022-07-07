package com.genexus.android.core.ui;

import java.lang.ref.WeakReference;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Emulates View.onTouch(), which is not fired when a View has a TouchListener set.
 * The behavior is extracted and (partly) replicated from Android 2.3 View class.
 */
public class ViewTouchCompat
{
	private final WeakReference<View> mViewReference;

	// State
	private boolean mPrePressed;
	private PointF mPressPoint;
	private final DelayedPress mDelayedPress;
	private final UnsetPressedState mUnsetPressedState;

	private ViewTouchCompat(View view)
	{
		mViewReference = new WeakReference<>(view);
		mDelayedPress = new DelayedPress();
		mUnsetPressedState = new UnsetPressedState();
	}

	public static ViewTouchCompat get(View view)
	{
		final int tag = com.genexus.android.R.id.tag_view_touch_compat;

		// Create a single object per view.
		ViewTouchCompat touchCompat = (ViewTouchCompat)view.getTag(tag);
		if (touchCompat == null)
		{
			touchCompat = new ViewTouchCompat(view);
			view.setTag(tag, touchCompat);
		}

		return touchCompat;
	}

	private View getView()
	{
		return mViewReference.get();
	}

	public void onTouchEvent(MotionEvent event)
	{
		View view = getView();
		if (view == null)
			return;

		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			// Finger down.
			// Show feedback, but only after a slight time has passed (the user may just be scrolling).
			mPrePressed = true;
			mPressPoint = new PointF(event.getX(), event.getY());
			view.postDelayed(mDelayedPress, ViewConfiguration.getTapTimeout());
		}
		else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			// Finger up.
			// If feedback hasn't been shown, show it. Otherwise cancel it.
			if (mPrePressed)
			{
				view.drawableHotspotChanged(event.getX(), event.getY());

				view.setPressed(true);
				view.postDelayed(mUnsetPressedState, ViewConfiguration.getPressedStateDuration());
			}
			else
			{
				if (!view.post(mUnsetPressedState))
					mUnsetPressedState.run();
			}

			// If a previous down posted a delayed press, don't run it. It's already finished.
			cancelDelayedPress();
		}
		else if (event.getAction() == MotionEvent.ACTION_CANCEL)
		{
			// Cancelled.
			cancelDelayedPress();
			view.setPressed(false);
		}
	}

	private void cancelDelayedPress()
	{
		View view = getView();
		if (view == null)
			return;

		mPrePressed = false;
		view.removeCallbacks(mDelayedPress);
	}

	private final class DelayedPress implements Runnable
	{
		@Override
		public void run()
		{
			View view = getView();
			if (view == null)
				return;

			if (mPressPoint != null)
				view.drawableHotspotChanged(mPressPoint.x, mPressPoint.y);

			mPrePressed = false;
			mPressPoint = null;

			view.setPressed(true);
		}
	}


	private final class UnsetPressedState implements Runnable
	{
		@Override
		public void run()
		{
			View view = getView();
			if (view == null)
				return;

			view.setPressed(false);
		}
	}
}
