package com.genexus.android.layout;

import android.content.Context;
import androidx.core.widget.NestedScrollView;
import android.util.AttributeSet;

import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.controls.IGxThemeable;
import com.genexus.android.core.utils.BackgroundOptions;
import com.genexus.android.core.utils.ThemeUtils;

/***
 * This view scroll vertically only if the absolute scroll on Y is greater than the absolute scroll y X
 */
public class ScrollViewThemeable extends NestedScrollView implements IGxThemeable
{
	private ThemeClassDefinition mThemeClass;

	public ScrollViewThemeable(Context context) {
		super(context);
	}
	
	public ScrollViewThemeable(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}

	@Override
	public void setThemeClass(ThemeClassDefinition themeClass)
	{
		mThemeClass = themeClass;
		applyClass(themeClass);
	}

	@Override
	public ThemeClassDefinition getThemeClass()
	{
		return mThemeClass;
	}

	@Override
	public void applyClass(ThemeClassDefinition themeClass)
	{
		// Background and Border.
		ThemeUtils.setBackgroundBorderProperties(this, themeClass, BackgroundOptions.DEFAULT);
	}
}
