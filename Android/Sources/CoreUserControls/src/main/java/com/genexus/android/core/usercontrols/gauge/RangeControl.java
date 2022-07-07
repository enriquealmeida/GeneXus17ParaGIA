package com.genexus.android.core.usercontrols.gauge;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.genexus.android.core.base.controls.IGxEditThemeable;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.controls.IGxEdit;

@SuppressLint("ViewConstructor")
public class RangeControl extends ChartSurface implements IGxEdit, IGxEditThemeable
{
	public static final String NAME = "SD LinearGauge";
	private String mTag;

	public RangeControl(Context context, LayoutItemDefinition def) 
	{
		super(context);
	}

	@Override
	public String getGxValue()
	{
		return null;
	}

	@Override
	public void setGxValue(String value)
	{
		GaugeSpecification spec = new GaugeSpecification();
		spec.deserialize(value);
		setSpec(spec);
		this.postInvalidate();
	}

	@Override
	public String getGxTag()
	{
		return mTag;
	}

	@Override
	public void setGxTag(String tag)
	{
		mTag = tag;
	}

	@Override
	public void setValueFromIntent(Intent data) { }

	@Override
	public IGxEdit getViewControl() 
	{
		setEnabled(false);
		return this;
	}

	@Override
	public IGxEdit getEditControl() 
	{
		return this;
	}

	@Override
	public boolean isEditable()
	{
		return false; // Never editable.
	}

	@Override
	public void applyEditClass(@NonNull ThemeClassDefinition themeClass)
	{
		super.mThemeClass = themeClass;
		if (mThemeClass.isAnimated()) {
			ValueAnimator animator = ObjectAnimator.ofFloat(this, "percentage", 0f, 1f);
			animator.setDuration(mThemeClass.getAnimationDuration());
			animator.addUpdateListener(updatedAnimation -> this.invalidate());
			animator.start();
		}
		postInvalidate();
	}
}
