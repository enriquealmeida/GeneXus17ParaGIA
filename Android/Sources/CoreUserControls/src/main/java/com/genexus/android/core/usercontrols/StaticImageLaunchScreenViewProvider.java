package com.genexus.android.core.usercontrols;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.genexus.android.core.controls.LaunchScreenViewProvider;

/**
 * Shows a static image as launch screen
 *
 * The image file must be named "appwelcomedefault" or "appwelcomedefaultlandscape" and placed in
 * the drawables directory.
 */
public class StaticImageLaunchScreenViewProvider implements LaunchScreenViewProvider {
	private final @DrawableRes int mImageResId;

	public StaticImageLaunchScreenViewProvider(int imageResId) {
		mImageResId = imageResId;
	}

	@Override
	public View createView(@NonNull Context context, @NonNull OnFinishListener onFinishListener) {
		ImageView imageView = new ImageView(context);
		imageView.setBackgroundResource(mImageResId);

		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT
		);
		params.gravity = Gravity.CENTER;
		imageView.setLayoutParams(params);

		onFinishListener.onFinish();

		return imageView;
	}
}
