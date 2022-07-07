package com.genexus.android.core.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import androidx.core.graphics.drawable.DrawableCompat;
import android.widget.TextView;

import com.genexus.android.R;
import com.genexus.android.core.base.metadata.enums.Alignment;

public class DrawableUtils
{
	private static final PorterDuff.Mode DEFAULT_TINT_MODE = PorterDuff.Mode.SRC_IN;

	public static Drawable newStateListDrawable(Drawable normal, Drawable selected)
	{
		if (normal == null && selected == null)
			return null;
		
		if (normal != null && selected == null)
			return normal;

		//noinspection ConstantConditions
		if (normal == null && selected != null)
			return selected;
		
		StateListDrawable stateDrawable = new StateListDrawable();
		stateDrawable.addState(new int[] { android.R.attr.state_selected }, selected);
		stateDrawable.addState(new int[] { android.R.attr.state_pressed }, selected);
		stateDrawable.addState(new int[] { }, normal);

		return stateDrawable;
	}

	/**
	 * Applies a TintList to the supplied drawable. Note that this does NOT create a COPY of the
	 * supplied drawable. Returning a Drawable object is necessary because in pre-Lollipop
	 * a wrapper must be installed over the drawable to support this.
	 */
	public static Drawable applyStateTint(Drawable drawable, int normalColor, int selectedColor)
	{
		if (drawable == null)
			return null;

		drawable = DrawableCompat.wrap(drawable);

		ColorStateList colors = new ColorStateList(
			new int[][]	{ new int [] { android.R.attr.state_selected }, new int[0], },
			new int[] { selectedColor, normalColor });

		DrawableCompat.setTintMode(drawable, DEFAULT_TINT_MODE);
		DrawableCompat.setTintList(drawable, colors);
		return drawable;
	}

	/**
	 * Applies a tint to the supplied drawable. Note that this does NOT create a COPY of the
	 * drawable! Returning a Drawable object is necessary because in pre-Lollipop a wrapper
	 * must be installed over the drawable to support this.
	 *
	 * PorterDuff.Mode.SRC_IN is used as TintMode.
	 *
	 * @param drawable Drawable to be tinted.
	 * @param tintColor Tint color.
	 * @return The tinted drawable.
	 */
	public static Drawable applyTint(Drawable drawable, int tintColor)
	{
		return applyTint(drawable, tintColor, DEFAULT_TINT_MODE);
	}

	/**
	 * Applies a tint to the supplied drawable. Note that this does NOT create a COPY of the
	 * drawable! Returning a Drawable object is necessary because in pre-Lollipop a wrapper
	 * must be installed over the drawable to support this.
	 *
	 * @param drawable Drawable to be tinted.
	 * @param tintColor Tint color.
	 * @param tintMode Tint mode.
	 * @return The tinted drawable.
	 */
	public static Drawable applyTint(Drawable drawable, int tintColor, PorterDuff.Mode tintMode)
	{
		Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
		DrawableCompat.setTintMode(wrappedDrawable, tintMode);
		DrawableCompat.setTint(wrappedDrawable, tintColor);
		return wrappedDrawable;
	}

	/**
	 * Applies the accent color as a tint to the supplied drawable. Note that this does NOT create
	 * a COPY of the drawable! Returning a Drawable object is necessary because in pre-Lollipop
	 * a wrapper must be installed over the drawable to support this.
	 *
	 * PorterDuff.Mode.SRC_IN is used as TintMode.
	 *
	 * @param context Context.
	 * @param drawable Drawable to be tinted.
	 * @return The tinted drawable.
	 */
	public static Drawable applyAccentTint(Context context, Drawable drawable)
	{
		Integer accentColor = ThemeUtils.getAndroidThemeColorId(context, R.attr.colorAccent);
		if (accentColor != null)
			return applyTint(drawable, accentColor);
		else
			return drawable;
	}

	/**
	 * Removes any tint color previously applied to this drawable.
	 * @param drawable The (possibly tinted) drawable.
	 * @return The original drawable without any tinting.
	 */
	public static Drawable removeTint(Drawable drawable)
	{
		Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
		DrawableCompat.setTintList(wrappedDrawable, null);
		return wrappedDrawable;
	}

	public static void setCompoundDrawableWithIntrinsicBounds(TextView view, Drawable drawable, int position)
	{
		view.setCompoundDrawablesWithIntrinsicBounds(
			position == Alignment.START ? drawable : null,
			position == Alignment.TOP ? drawable : null,
			position == Alignment.END ? drawable : null,
			position == Alignment.BOTTOM ? drawable : null);		
	}
}
