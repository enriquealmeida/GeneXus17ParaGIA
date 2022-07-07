package com.genexus.android.core.controls;

import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.genexus.android.core.base.controls.IGxOverrideThemeable;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeOverrideProperties;
import com.genexus.android.core.controls.common.IAdjustContentSizeText;
import com.genexus.android.core.controls.common.TextViewUtils;
import com.genexus.android.core.utils.ThemeUtils;

public class GxTextBlockTextView extends AppCompatTextView implements IGxThemeable, IGxLocalizable, IGxOverrideThemeable, IAdjustContentSizeText
{
	private LayoutItemDefinition mDefinition;
	private ThemedOverrideViewHelper mThemedHelper;

	public GxTextBlockTextView(Context context) {
		super(context);
		mThemedHelper = new ThemedOverrideViewHelper(this);
	}

	public GxTextBlockTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mThemedHelper = new ThemedOverrideViewHelper(this);
	}

	public GxTextBlockTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mThemedHelper = new ThemedOverrideViewHelper(this);
	}

	public GxTextBlockTextView(Context context, LayoutItemDefinition definition) {
		super(context);
		mDefinition = definition;
		mThemedHelper = new ThemedOverrideViewHelper(this, definition);

		TextViewUtils.configureBreakStrategy(this);

	}

	public void setCaption(String caption)
	{
		TextViewUtils.setText(this, caption, mDefinition);
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
		mThemedHelper.applyClass(themeClass);
		applyControlClass(themeClass);
	}

	private void applyControlClass(ThemeClassDefinition themeClass) {
		ThemeUtils.setFontProperties(this, themeClass);
	}

	@Override
	public void setLayoutParams(ViewGroup.LayoutParams params) {
		super.setLayoutParams(params);
		mThemedHelper.setLayoutParams(params);
	}

	@Override
	public void onTranslationChanged() {
		if (mDefinition != null) {
			mDefinition.getControlType();
			TextViewUtils.setText(this, mDefinition.getCaption(), mDefinition);
		}
	}

	// needed for override textBlock backColor and foreColor
	@Override
	public void setOverride(String propertyName, String propertyValue) {
		mThemedHelper.setOverride(propertyName, propertyValue);
	}

	@Override
	public ThemeOverrideProperties getThemeOverrideProperties() {
		return mThemedHelper.getThemeOverrideProperties();
	}

	@Override
	public void adjustContentSize() {
	}
}
