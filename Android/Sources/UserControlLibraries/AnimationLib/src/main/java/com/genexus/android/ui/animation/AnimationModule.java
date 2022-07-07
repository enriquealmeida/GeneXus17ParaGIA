package com.genexus.android.ui.animation;

import android.content.Context;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.LaunchScreenViewProviderFactory;
import com.genexus.android.core.controls.LoadingIndicatorView;
import com.genexus.android.core.controls.ProgressDialogFactory;
import com.genexus.android.core.framework.GenexusModule;
import com.genexus.android.core.usercontrols.UcFactory;
import com.genexus.android.core.usercontrols.UserControlDefinition;

/**
 * Animation support Module. Uses Lottie by Airbnb.
 */
public class AnimationModule implements GenexusModule {
	private static final String LAUNCH_ANIMATION_NAME = "gxlaunch";

	@Override
	public void initialize(Context context) {
		LoadingIndicatorView.registerLoadingViewProvider(new LoadingAnimationProvider());
		ProgressDialogFactory.registerProgressDialogProvider(new ProgressDialogAnimationProvider());
		UcFactory.addControl(new UserControlDefinition(GxAnimationView.USER_CONTROL_NAME, GxAnimationView.class));

		String assetName = Services.Assets.getAnimationFilename(LAUNCH_ANIMATION_NAME);
		if (Services.Assets.hasAsset(assetName)) {
			LaunchScreenViewProviderFactory.setProvider(new LottieAnimationLaunchScreenViewProvider(assetName));
		}
	}
}
