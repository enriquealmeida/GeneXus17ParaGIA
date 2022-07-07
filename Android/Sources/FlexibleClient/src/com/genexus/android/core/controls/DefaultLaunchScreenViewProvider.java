package com.genexus.android.core.controls;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

/**
 * Shows a large circular and indeterminate {@link ProgressBar} as launch screen
 */
public class DefaultLaunchScreenViewProvider implements LaunchScreenViewProvider {

	@Override
	public View createView(@NonNull Context context, @NonNull OnFinishListener onFinishListener) {
		ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleLarge);
		progressBar.setIndeterminate(true);

		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT
		);
		params.gravity = Gravity.CENTER;
		progressBar.setLayoutParams(params);

		onFinishListener.onFinish();

		return progressBar;
	}
}
