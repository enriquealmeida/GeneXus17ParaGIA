package com.genexus.android.ui.animation;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;

import com.airbnb.lottie.LottieAnimationView;
import com.genexus.android.core.base.controls.IGxControlRuntime;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.usercontrols.IGxUserControl;

import java.util.List;

/**
 * User Control used to display an animation
 */
@SuppressLint("ViewConstructor")
public class GxAnimationView extends LottieAnimationView implements IGxUserControl, IGxControlRuntime {
	static final String USER_CONTROL_NAME = "AnimationView";
	private static final String METHOD_PLAY = "Play";
	private static final String METHOD_PAUSE = "Pause";
	private static final String METHOD_SET_ANIMATION = "SetAnimation";
	private static final String METHOD_SET_PROGRESS = "SetProgress";

	private final Context mContext;
	private final LayoutItemDefinition mDefinition;

	public GxAnimationView(Context context, Coordinator coordinator, LayoutItemDefinition definition) {
		super(context);
		mContext = context;
		mDefinition = definition;
	}

	@Override
	public Value callMethod(String name, List<Value> parameters) {
		if (METHOD_PLAY.equalsIgnoreCase(name)) {
			// Play FromTo
			if (parameters.size() >= 2) {
				setProgress((float) parameters.get(0).coerceToDouble(0));
				setMaxProgress((float) parameters.get(1).coerceToDouble(1));
			}
			// Play To
			if (parameters.size() >= 1) {
				setProgress(0f);
				setMaxProgress((float) parameters.get(0).coerceToDouble(1));
			}
			// Just Play
			playAnimation();
		} else if (METHOD_PAUSE.equalsIgnoreCase(name)) {
			pauseAnimation();
		} else if (METHOD_SET_ANIMATION.equalsIgnoreCase(name) && parameters.size() >= 2) {
			String animationName = parameters.get(0).coerceToString();
			setGxAnimation(animationName, parameters.get(1).coerceToBoolean());
		} else if (METHOD_SET_PROGRESS.equalsIgnoreCase(name) && parameters.size() >= 1) {
			setProgress((float) parameters.get(0).coerceToDouble(0));
		}
		return null;
	}

	private void setGxAnimation(String animationName, boolean looping) {
		// if there is an error, log it and continue without animation
		ThemeClassDefinition classDefinition = Services.Themes.getCurrentTheme().getClass(animationName);
		if (classDefinition == null) {
			Services.Log.error("Animation class not found: " + animationName);
		} else if (classDefinition.optStringProperty("idAnimationType").equals("idNative")) {
			Services.Log.error("Animation class can't be Native: " + animationName);
		} else {
			// animation should be a .json file in assets
			String fileName = Services.Assets.getAnimationFilename(animationName);
			if (!Services.Assets.hasAsset(fileName)) {
				// throw a cleaner exception than the one we get if we continue
				throw new IllegalArgumentException("Lottie file was not found: " + fileName);
			}

			setAnimation(fileName);

			Services.Log.debug("setAnimation " + animationName);
			Services.Log.debug("getProgress " + getProgress());

			// set progress to 0 in a setAnimation.
			setProgress(0f);

			// default scale properties
			setScale(0.5f);
			setScaleType(ScaleType.FIT_CENTER);

			setRepeatCount(looping ? ValueAnimator.INFINITE : 0);
		}
	}
}
