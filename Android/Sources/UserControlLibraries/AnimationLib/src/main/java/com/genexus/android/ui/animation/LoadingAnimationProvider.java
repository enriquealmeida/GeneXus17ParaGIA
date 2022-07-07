package com.genexus.android.ui.animation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.LoadingIndicatorView;

/**
 * Loading Animation provider using the Lottie library.
 */
class LoadingAnimationProvider implements LoadingIndicatorView.ViewProvider {

	@Override
	public String getType() {
		return "idLottie";
	}

	@Override
	public View newLoadingView(@NonNull Context context, @NonNull LoadingIndicatorView.Style style, @NonNull RelativeLayout.LayoutParams layoutParams, String animationName) {
		// Get file name in the format is copied to assets folder.
		String assetName = Services.Assets.getAnimationFilename(animationName);
		if (!Services.Assets.hasAsset(assetName))
			return null;

		// Since this animation will be used everywhere in the app, use "strong" cache.
		LottieAnimationView view = new LottieAnimationView(context);

		view.setAnimation(assetName); // Will throw exception if animation is not in assets.

		// if small scale down animation at quarter than original
		if (style == LoadingIndicatorView.Style.SMALL)
			view.setScale(0.25f);

		view.setRepeatCount(ValueAnimator.INFINITE);

		view.playAnimation();

		return view;
	}
}
