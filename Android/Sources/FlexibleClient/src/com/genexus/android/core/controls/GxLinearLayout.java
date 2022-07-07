package com.genexus.android.core.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.utils.BackgroundOptions;
import com.genexus.android.core.utils.ThemeUtils;

public class GxLinearLayout extends android.widget.LinearLayout implements IGxThemeable
{
	protected ThemedViewHelper mThemedHelper;

	public GxLinearLayout(Context context) {
		super(context);
		mThemedHelper = new ThemedViewHelper(this, null);
	}

	public GxLinearLayout(Context context, LayoutItemDefinition layoutItem) {
		super(context);
		mThemedHelper = new ThemedViewHelper(this, layoutItem);
	}

	public GxLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mThemedHelper = new ThemedViewHelper(this, null);
	}

	@Override
	public void setLayoutParams(ViewGroup.LayoutParams params) {
		mThemedHelper.setLayoutParams(params);
		super.setLayoutParams(params);
	}

	@Override
	public void setThemeClass(ThemeClassDefinition themeClass) {
		mThemedHelper.setThemeClass(themeClass);
	}

	@Override
	public ThemeClassDefinition getThemeClass() {
		return mThemedHelper.getThemeClass();
	}

	protected void setBackgroundBorderProperties(ThemeClassDefinition themeClass)
	{
		ThemeUtils.setBackgroundBorderProperties(this, themeClass, BackgroundOptions.DEFAULT);
	}

	@Override
	public void applyClass(ThemeClassDefinition themeClass) {
		mThemedHelper.applyClass(themeClass);
	}
}
