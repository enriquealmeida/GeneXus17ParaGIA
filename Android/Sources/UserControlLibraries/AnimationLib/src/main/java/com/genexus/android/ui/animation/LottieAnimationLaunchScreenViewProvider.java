package com.genexus.android.ui.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.genexus.android.core.controls.LaunchScreenViewProvider;

/**
 * Shows a Lottie animation as launch screen
 *
 * The Lottie JSON file must be named "gxlaunch.json" and placed in the assets directory.
 */
class LottieAnimationLaunchScreenViewProvider implements LaunchScreenViewProvider {
	private String mAssetName;

	public LottieAnimationLaunchScreenViewProvider(String assetName) {
		mAssetName = assetName;
	}

	@Override
	public View createView(@NonNull Context context, @NonNull OnFinishListener onFinishListener) {
		LottieAnimationView view = new LottieAnimationView(context);
		view.setAnimation(mAssetName);
		view.setRepeatCount(0);
		view.addAnimatorListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animator) {
				onFinishListener.onFinish();
			}
		});
		view.playAnimation();

		return view;
	}
}
