package com.genexus.android.animations;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import com.genexus.android.R;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.metadata.theme.TransformationDefinition;

public class Transformations
{
	public static void apply(View view, ThemeClassDefinition themeClass)
	{
		if (themeClass == null) {
			apply(view, TransformationDefinition.EMPTY, false, 0);
			return;
		}

		TransformationDefinition transformation = themeClass.getTransformation();

		if (transformation != null)
		{
			apply(view, transformation, themeClass.isAnimated(), themeClass.getAnimationDuration());

			// Set mark to indicate that a transformation has been applied (see below).
			view.setTag(R.id.tag_view_has_transformation, true);
		}
		else
		{
			// We need to clear the previous transformation, but only if there was any.
			Boolean hasPreviousTransformation = (Boolean)view.getTag(R.id.tag_view_has_transformation);
			if (hasPreviousTransformation != null)
			{
				apply(view, TransformationDefinition.EMPTY, false, 0);
				view.setTag(R.id.tag_view_has_transformation, null);
			}
		}
	}

	private static void apply(View view, TransformationDefinition transformation, boolean animated, int animationDuration)
	{
		if (transformation != null)
		{
			// Always create an AnimatorSet object to apply transformations.
			// When we don't want to animate, just use 0 as duration.
			AnimatorSet set = new AnimatorSet();
			set.playTogether(getAnimators(view, transformation));

			if (animated)
				set.setDuration(animationDuration);
			else
				set.setDuration(0);

			set.start();
		}
	}

	private static List<Animator> getAnimators(View view, TransformationDefinition transformation)
	{
		ArrayList<Animator> animators = new ArrayList<>();

		// Anchor point.
		Float anchorPointX = TransformationHelper.getAnchorPointX(view, transformation);
		if (anchorPointX != null)
			animators.add(ObjectAnimator.ofFloat(view, "pivotX", anchorPointX));

		Float anchorPointY = TransformationHelper.getAnchorPointY(view, transformation);
		if (anchorPointY != null)
			animators.add(ObjectAnimator.ofFloat(view, "pivotY", anchorPointY));

		// Translation
		Float translationX = TransformationHelper.getTranslationX(view, transformation);
		if (translationX != null)
			animators.add(ObjectAnimator.ofFloat(view, "translationX", translationX));

		Float translationY = TransformationHelper.getTranslationY(view, transformation);
		if (translationY != null)
			animators.add(ObjectAnimator.ofFloat(view, "translationY", translationY));

		// Scaling
		Float scaleX = TransformationHelper.getScaleX(view, transformation);
		if (scaleX != null)
			animators.add(ObjectAnimator.ofFloat(view, "scaleX", scaleX));

		Float scaleY = TransformationHelper.getScaleY(view, transformation);
		if (scaleX != null)
			animators.add(ObjectAnimator.ofFloat(view, "scaleY", scaleY));

		// Resizing
		// TODO: Add resizing.

		// Rotation
		animators.add(ObjectAnimator.ofFloat(view, "rotation", transformation.getRotation()));

		return animators;
	}
}
