package com.genexus.android.core.controls;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.genexus.android.core.base.metadata.enums.MeasureUnit;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.utils.BackgroundOptions;
import com.genexus.android.core.utils.ThemeUtils;

public class GxHorizontalSeparator
{
	private final boolean mIsDefault;
	private final boolean mIsVisible;
	private final Drawable mDrawable;
	private final int mHeight;

	public GxHorizontalSeparator(Context context, LayoutItemDefinition parentItem)
	{
		this(context, parentItem, parentItem.getThemeClass());
	}

	/**
	 * Creates the horizontal separator for a grid or table.
	 * @param parentItem Grid or table layout item definition.
	 * @param parentItemClass Class of the grid or table layout item (can be different from set class when explicitly applying a different one).
	 */
	public GxHorizontalSeparator(Context context, LayoutItemDefinition parentItem, ThemeClassDefinition parentItemClass)
	{
		// Read separator class (and separator visibility) from parent's class.
		boolean isVisible = false;
		String separatorClassName = null;
		if (parentItemClass != null)
		{
			boolean defaultIsVisible = true;
			if (Strings.areEqual(parentItemClass.getRootName(), "GridRow", true))
				defaultIsVisible = false;

			isVisible = parentItemClass.getBooleanProperty("sd_row_horizontal_line_separator", defaultIsVisible);
			separatorClassName = parentItemClass.optStringProperty("ThemeHorizontalLineClassReference");
		}

		// Default separator is in HorizontalLine class.
		if (!Services.Strings.hasValue(separatorClassName))
			separatorClassName = "HorizontalLine";

		mIsVisible = isVisible;
		ThemeClassDefinition separatorClass = Services.Themes.getThemeClass(separatorClassName);

		if (separatorClass != null)
		{
			// Create drawable from background image and color.
			BackgroundOptions backgroundOptions = new BackgroundOptions();
			backgroundOptions.setUseBitmapSize(true);
			mDrawable = ThemeUtils.createBackgroundDrawableFromClass(context, separatorClass, backgroundOptions);

			// Read height: if specified use that one, otherwise use image's height if set, otherwise use 1.
			String strDipHeight = separatorClass.optStringProperty("height");
			Integer dipHeight = Services.Strings.parseMeasureValue(strDipHeight, MeasureUnit.DIP);

			if (mDrawable != null || dipHeight != null)
			{
				mIsDefault = false;

				if (dipHeight != null && dipHeight > 0)
					mHeight = Services.Device.dipsToPixels(dipHeight);
				else if (mDrawable != null && mDrawable.getIntrinsicHeight() > 0)
					mHeight = mDrawable.getIntrinsicHeight();
				else
					mHeight = 1;
			}
			else
			{
				mIsDefault = true;
				mHeight = 1;
			}
		}
		else
		{
			mIsDefault = true;
			mDrawable = null;
			mHeight = 1;
		}
	}

	public boolean isDefault() { return mIsDefault; }
	public boolean isVisible() { return mIsVisible; }
	public Drawable getDrawable() { return mDrawable; }
	public int getHeight() { return mHeight; }
}
