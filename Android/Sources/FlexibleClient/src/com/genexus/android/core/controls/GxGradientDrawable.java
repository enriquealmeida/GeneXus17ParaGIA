package com.genexus.android.core.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;

import androidx.core.content.ContextCompat;

import com.genexus.android.core.base.metadata.layout.Size;
import com.genexus.android.core.base.metadata.theme.BackgroundImageMode;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeOverrideProperties;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.utils.ThemeUtils;

public class GxGradientDrawable extends GradientDrawable
{
    private Drawable mBackground;
    private BackgroundImageMode mBackgroundImageMode;
	private Integer mBorderWidth;
	private ThemeClassDefinition mThemeClass;

    public GxGradientDrawable()
    {
		super();
    }

    public void setBackground(Drawable background)
    {
    	mBackground = background;
    }

    public void setBackgroundImageMode(BackgroundImageMode imageMode)
    {
    	mBackgroundImageMode = imageMode;
    }

    @Override
    public void draw(Canvas canvas)
    {
    	super.draw(canvas);
    	if (mBackground != null)
    	{
    		if (mBorderWidth == null)
				mBorderWidth = 0;

	    	Rect rect = copyBounds();
			rect.left = rect.left + mBorderWidth;
			rect.top = rect.top + mBorderWidth;
			rect.right = rect.right - mBorderWidth;
			rect.bottom = rect.bottom - mBorderWidth;

			if (mBackground instanceof BitmapDrawable)
			{
				BitmapDrawable backgroundBitmap = (BitmapDrawable)mBackground;
				if (mBackgroundImageMode == BackgroundImageMode.TILE)
					backgroundBitmap.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
				else
					backgroundBitmap.setTileModeXY(null, null);
			}

			mBackground.setBounds(rect);
    		mBackground.draw(canvas);
    	}
    }

	@SuppressWarnings("SuspiciousNameCombination")
	@Override
	public boolean getPadding(Rect padding)
	{
		if (mBackground != null && mBackground.getPadding(padding))
		{
			// Add the class border to the drawable's intrinsic padding (probably a 9-patch).
			if (mThemeClass != null)
			{
				int borderWidth = mThemeClass.getBorderWidth();
				padding.top += borderWidth;
				padding.left += borderWidth;
				padding.right += borderWidth;
				padding.bottom += borderWidth;
			}

			return true;
		}

		return super.getPadding(padding);
	}

	public void setThemeClass(Context context, ThemeClassDefinition themeClass, boolean isHighlighted, Size viewSize, ThemeOverrideProperties overrideProperties)
	{
		if (themeClass == null)
			throw new IllegalArgumentException("themeClass cannot be null.");

		mThemeClass = themeClass;

		String backgroundColor = themeClass.getBackgroundColor();
		if (overrideProperties!=null && Strings.hasValue(overrideProperties.getBackgroundColor()))
			backgroundColor = overrideProperties.getBackgroundColor();

		if (isHighlighted && Strings.hasValue(themeClass.getHighlightedBackgroundColor()))
			backgroundColor = themeClass.getHighlightedBackgroundColor();

		Integer backgroundColorId = ThemeUtils.getColorId(backgroundColor);

		if (backgroundColorId != null)
			setColor(backgroundColorId);
		else
			setColor(ContextCompat.getColor(context, android.R.color.transparent));

		Integer borderColorId = ThemeUtils.getColorId(themeClass.getBorderColor());
		String borderStyle = themeClass.getBorderStyle();

		if (borderColorId != null)
		{
			mBorderWidth = themeClass.getBorderWidth();

			if (borderStyle.equalsIgnoreCase("dotted"))
			{
				int dashWidth = mBorderWidth;
				int dashGap = mBorderWidth * 3;
				setStroke(mBorderWidth, borderColorId, dashWidth, dashGap);
			}

			if (borderStyle.equalsIgnoreCase("dashed"))
			{
				int dashWidth = Services.Device.dipsToPixels(7);
				int dashGap = dashWidth;
				setStroke(mBorderWidth, borderColorId, dashWidth, dashGap);
			}

			if (borderStyle.equalsIgnoreCase("solid") || borderStyle.equalsIgnoreCase("double"))
			{
				setStroke(mBorderWidth, borderColorId);
			}
		}

		// Corner Radius
		if (themeClass.getMaxCornersRadius()>0)
		{
			Integer[] corners = themeClass.getCornersRadius();

			// In API 28 and bellow there is a problem with corners radius when it is too big,
			// if it is larger than half the size it gets cut too much, fix the radius here
			if (viewSize != null && Build.VERSION.SDK_INT <= Build.VERSION_CODES.P)
			{
				int max = Math.min(viewSize.getWidth(), viewSize.getHeight()) / 2;
				for (int n = 0; n < 4; n++)
				{
					if (corners[n] > max)
						corners[n] = max;
				}
			}

			setCornerRadii(new float[] { corners[0],corners[0], corners[1],corners[1],
					corners[2],corners[2], corners[3],corners[3] });
		}

		mBackgroundImageMode = themeClass.getBackgroundImageMode();
	}
}
