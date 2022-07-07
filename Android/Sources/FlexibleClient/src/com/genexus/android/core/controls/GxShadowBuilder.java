package com.genexus.android.core.controls;

import android.graphics.Point;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.View.DragShadowBuilder;

import com.genexus.android.core.base.services.Services;

public class GxShadowBuilder extends DragShadowBuilder
{
	private final Point mTouchPoint;
	private static final int SHADOW_VERTICAL_OFFSET = Services.Device.dipsToPixels(10);

	public GxShadowBuilder(@NonNull View v, @Nullable Point p)
	{
		super(v);
		mTouchPoint = p;
	}

	@Override
	public void onProvideShadowMetrics(@NonNull Point shadowSize, @NonNull Point shadowTouchPoint)
	{
		super.onProvideShadowMetrics(shadowSize, shadowTouchPoint);

		if (mTouchPoint != null)
			shadowTouchPoint.set(mTouchPoint.x, mTouchPoint.y + SHADOW_VERTICAL_OFFSET);
	}
}
