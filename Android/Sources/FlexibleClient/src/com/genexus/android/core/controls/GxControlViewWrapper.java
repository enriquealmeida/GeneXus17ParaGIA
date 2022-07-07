package com.genexus.android.core.controls;

import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;

import com.genexus.android.layout.GxTheme;
import com.genexus.android.layout.LayoutTag;
import com.genexus.android.core.base.controls.IGxControlRuntime;
import com.genexus.android.core.base.controls.IGxControlRuntimeContext;
import com.genexus.android.core.base.controls.IGxOverrideThemeable;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeOverrideProperties;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.ExecutionContext;
import com.genexus.android.core.utils.Cast;
import com.genexus.android.core.utils.KeyboardUtils;

/**
 * Wrapper class to treat generic Views as an IGxControl. This is a stopgap measure.
 * Actually, DataBoundControl, GxLayout, GxImageView, user controls, &c should implement IGxControl.
 */
public class GxControlViewWrapper implements IGxControl, IGxOverrideThemeable
{
	private final View mView;

	public GxControlViewWrapper(View view)
	{
		mView = view;
	}

	@Override
	public String getName()
	{
		LayoutItemDefinition definition = (LayoutItemDefinition) mView.getTag(LayoutTag.CONTROL_DEFINITION);
		return (definition != null ? definition.getName() : "<Unknown>");
	}

	@Override
	public LayoutItemDefinition getDefinition()
	{
		return (LayoutItemDefinition)mView.getTag(LayoutTag.CONTROL_DEFINITION);
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		mView.setEnabled(enabled);
	}

	@Override
	public void setThemeClass(ThemeClassDefinition themeClass)
	{
		if (mView instanceof IGxThemeable)
			GxTheme.applyStyle((IGxThemeable)mView, themeClass);
	}

	@Override
	public void setVisible(boolean visible)
	{
		if (getThemeClass() != null && getThemeClass().isAnimated()) {
			if (visible) {
				// Fade in
				mView.setAlpha(0);
				mView.setVisibility(View.VISIBLE);
				mView.animate()
						.alpha(1)
						.setDuration(getThemeClass().getAnimationDuration())
						.setListener(new AnimatorListenerAdapter() {}); // To clear the one set in fade out
			}
			else if (getDefinition().getKeepSpace()) {
				// Fade out
				mView.animate()
						.alpha(0)
						.setDuration(getThemeClass().getAnimationDuration())
						.setListener(new AnimatorListenerAdapter() {
							@Override
							public void onAnimationEnd(Animator animation) {
								super.onAnimationEnd(animation);
								mView.setVisibility(View.GONE);
							}
						});
			}
			else {
				mView.setVisibility(View.GONE);
			}

			if (!getDefinition().getKeepSpace()) {
				// Collapse Space, make animation in the layout
				TransitionManager.beginDelayedTransition((ViewGroup)mView.getRootView(), new AutoTransition().setDuration(getThemeClass().getAnimationDuration()));
			}
		}
		else {
			mView.setAlpha(1);
			mView.setVisibility(visible ? View.VISIBLE : View.GONE);
		}
	}

	@Override
	public void requestLayout()
	{
		mView.requestLayout();
	}

	@Override
	public Object getInterface(Class c) {
		if (mView instanceof IGxControl)
			return ((IGxControl)mView).getInterface(c);
		else
			return Cast.as(c, mView);
	}

	@Override
	public void setFocus(boolean showKeyboard)
	{
		// Focus & show keyboard.
		mView.requestFocus();

		if (showKeyboard)
		{
			View viewForInput = mView;
			if (mView instanceof DataBoundControl && ((DataBoundControl) mView).getAttribute() != null)
				viewForInput = ((DataBoundControl)mView).getAttribute();

			KeyboardUtils.showKeyboard(viewForInput);
		}
	}

	@Override
	public void setCaption(String caption)
	{
		// For now only set caption to GxButton and TextBlock controls, should work in other controls?
		if (mView instanceof GxButton)
			((GxButton)mView).setCaption(caption);
		else if (mView instanceof GxTextBlockTextView)
			((GxTextBlockTextView)mView).setCaption(caption);
		else if (mView instanceof DataBoundControl)
			((DataBoundControl)mView).setCaption(caption);
	}

	@Override
	public boolean isEnabled()
	{
		if (mView instanceof IGxEdit)
			return ((IGxEdit)mView).isEditable();
		else
			return mView.isEnabled();
	}

	@Override
	public ThemeClassDefinition getThemeClass()
	{
		if (mView instanceof IGxThemeable)
			return ((IGxThemeable)mView).getThemeClass();
		else
			return (ThemeClassDefinition)mView.getTag(LayoutTag.CONTROL_THEME_CLASS);
	}

	@Override
	public boolean isVisible()
	{
		return (mView.getVisibility() == View.VISIBLE);
	}

	@Override
	public String getCaption()
	{
		// For now only set caption to GxButton and TextBlock controls, should work in other controls?
		if (mView instanceof GxButton)
			((GxButton)mView).getCaption();

		if (mView instanceof GxTextBlockTextView)
			return ((GxTextBlockTextView)mView).getText().toString();

		if (mView instanceof DataBoundControl)
		{
			GxTextView dataLabel = ((DataBoundControl)mView).getLabel();
			if (dataLabel != null)
				return dataLabel.getText().toString();
		}

		return Strings.EMPTY;
	}

	@Override
	public void setExecutionContext(ExecutionContext context)
	{
		if (mView instanceof IGxControlRuntimeContext)
			((IGxControlRuntimeContext)mView).setExecutionContext(context);
	}

	@Override
	public Value getPropertyValue(String name)
	{
		if (mView instanceof IGxControlRuntime)
			return ((IGxControlRuntime)mView).getPropertyValue(name);
		else
			return null;
	}

	@Override
	public void setPropertyValue(String name, Value value)
	{
		if (mView instanceof IGxControlRuntime)
			((IGxControlRuntime)mView).setPropertyValue(name, value);
	}

	@Override
	public Value callMethod(String name, List<Value> parameters)
	{
		if (mView instanceof IGxControlRuntime)
			return ((IGxControlRuntime)mView).callMethod(name, parameters);
		else
			return null;
	}

	@Override
	public void setOverride(String propertyName, String propertyValue) {
		if (mView instanceof IGxOverrideThemeable)
			((IGxOverrideThemeable)mView).setOverride(propertyName, propertyValue);
	}

	@Override
	public ThemeOverrideProperties getThemeOverrideProperties()
	{
		if (mView instanceof IGxOverrideThemeable)
			return ((IGxOverrideThemeable)mView).getThemeOverrideProperties();
		return null;
	}

	@Override
	public View getView() {
		return mView;
	}
}
