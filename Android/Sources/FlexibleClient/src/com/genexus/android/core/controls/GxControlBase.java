package com.genexus.android.core.controls;

import android.view.View;

import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.ExecutionContext;
import com.genexus.android.core.utils.Cast;

/**
 * A convenience class to extend when writing an IGxControl that provides only a subset of the interface methods.
 * For the others, stores passed values (e.g. enabled, visible) and returns them.
 */
public abstract class GxControlBase implements IGxControl
{
	private ThemeClassDefinition mThemeClass;
	private boolean mVisible = true;
	private boolean mEnabled = true;
	private String mCaption = Strings.EMPTY;

	@Override
	public LayoutItemDefinition getDefinition()
	{
		return null;
	}

	@Override
	public void setExecutionContext(ExecutionContext context)
	{
	}

	@Override
	public boolean isEnabled()
	{
		return mEnabled;
	}

	@Override
	public ThemeClassDefinition getThemeClass()
	{
		return mThemeClass;
	}

	@Override
	public boolean isVisible()
	{
		return mVisible;
	}

	@Override
	public String getCaption()
	{
		return mCaption;
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		mEnabled = enabled;
	}

	@Override
	public void setFocus(boolean showKeyboard)
	{
	}

	@Override
	public void setThemeClass(ThemeClassDefinition themeClass)
	{
		mThemeClass = themeClass;
	}

	@Override
	public void setVisible(boolean visible)
	{
		mVisible = visible;
	}

	@Override
	public void setCaption(String caption)
	{
		mCaption = caption;
	}

	@Override
	public void requestLayout() {
	}

	@Override
	public Object getInterface(Class c) {
		return Cast.as(c, this);
	}

	@Override
	public View getView() {
		return null;
	}
}
