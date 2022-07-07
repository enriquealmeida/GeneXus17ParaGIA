package com.genexus.android.core.controls;

import android.app.Activity;
import android.view.View;

import com.genexus.android.R;
import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.common.ExecutionContext;
import com.genexus.android.core.fragments.IInspectableComponent;
import com.genexus.android.core.utils.Cast;

public class ApplicationBarControl implements IGxControl, IInspectableComponent
{
	public static final String CONTROL_NAME = "ApplicationBar";
	private final Activity mActivity;

	public ApplicationBarControl(Activity activity)
	{
		mActivity = activity;
	}

	@Override
	public String getName()
	{
		return CONTROL_NAME;
	}

	@Override
	public LayoutItemDefinition getDefinition()
	{
		return null;
	}

	@Override
	public boolean isVisible()
	{
		return ActivityHelper.hasActionBar(mActivity);
	}

	@Override
	public void setVisible(boolean visible)
	{
		ActivityHelper.setActionBarVisibility(mActivity, visible);
	}

	@Override
	public ThemeClassDefinition getThemeClass()
	{
		return ActivityHelper.getActionBarThemeClass(mActivity);
	}

	@Override
	public void setThemeClass(ThemeClassDefinition themeClass)
	{
		ActivityHelper.setActionBarThemeClass(mActivity, themeClass);
	}

	@Override
	public void setExecutionContext(ExecutionContext context) { }

	@Override
	public boolean isEnabled() { return true; }

	@Override
	public String getCaption() { return null; }

	@Override
	public void setEnabled(boolean enabled) { }

	@Override
	public void setFocus(boolean showKeyboard) { }

	@Override
	public void setCaption(String caption) { }

	@Override
	public void requestLayout() { }

	@Override
	public Object getInterface(Class c) {
		return Cast.as(c, this);
	}

	@Override
	public View getRootView() {
		return mActivity.findViewById(R.id.action_bar_toolbar);
	}

	@Override
	public View getView() {
		return null;
	}
}
