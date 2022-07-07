package com.genexus.android.core.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.ui.Coordinator;

/**
 * Control used for standalone images (i.e. not inside DataBoundControl).
 */
public class GxImageViewStatic extends GxImageViewBase implements IGxThemeable {
	private ThemedViewHelper mThemedHelper;

	public GxImageViewStatic(Context context, AttributeSet attrs) {
		super(context, attrs);
		mThemedHelper = new ThemedViewHelper(this, null);
	}

	public GxImageViewStatic(Context context, Coordinator coordinator, LayoutItemDefinition definition) {
		super(context, coordinator, definition);
		mThemedHelper = new ThemedViewHelper(this, definition);
	}

	@Override
	public void setThemeClass(ThemeClassDefinition themeClass) {
		mThemedHelper.setThemeClass(themeClass);
		applyControlClass(themeClass);
	}

	@Override
	public ThemeClassDefinition getThemeClass() {
		return mThemedHelper.getThemeClass();
	}

	@Override
	public void applyClass(ThemeClassDefinition themeClass) {
		if (themeClass != null) {
			mThemedHelper.applyClass(themeClass);
			applyControlClass(themeClass);
		}
	}

	private void applyControlClass(ThemeClassDefinition themeClass) {
		// Scale type and custom size.
		setImagePropertiesFromThemeClass(themeClass);
	}

	@Override
	public void setLayoutParams(ViewGroup.LayoutParams params) {
		super.setLayoutParams(params);
		mThemedHelper.setLayoutParams(params);
	}
}
