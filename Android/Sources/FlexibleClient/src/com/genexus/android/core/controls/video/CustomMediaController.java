package com.genexus.android.core.controls.video;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;

import com.genexus.android.R;

@SuppressLint("ClickableViewAccessibility")
public
class CustomMediaController extends MediaController {

	private ImageButton mFullScreenButton;
	private final OnClickListener mFullScreenClickListener;
	private final OnTouchListener mFullScreenTouchListener;
	private final int mRightMargin;
	private boolean mHideWhenAttached;

	public CustomMediaController(Context context, OnClickListener clickListener, OnTouchListener touchListener, int rightMargin) {
		super(context);
		mFullScreenClickListener = clickListener;
		mFullScreenTouchListener = touchListener;
		mRightMargin = rightMargin;
		mHideWhenAttached = true;
	}

	@Override
	public void setAnchorView(View view) {
		super.setAnchorView(view);

		mFullScreenButton = new ImageButton(getContext());

		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
			LayoutParams.WRAP_CONTENT, Gravity.END);
		params.rightMargin = mRightMargin;
		addView(mFullScreenButton, params);

		mFullScreenButton.setImageResource(R.drawable.gx_ic_fullscreen_grey_36dp);
		mFullScreenButton.setOnClickListener(mFullScreenClickListener);
		mFullScreenButton.setOnTouchListener(mFullScreenTouchListener);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		/* onAttachedToWindow is overridden to hide the MediaController as soon as it is available
		 because when inside an Horizontal Grid, multiple instances sit on top of one another as they
		 are created as root views instead of VideoView's child */
		if (mHideWhenAttached) {
			mHideWhenAttached = false;
			hide();
		}
	}
}
