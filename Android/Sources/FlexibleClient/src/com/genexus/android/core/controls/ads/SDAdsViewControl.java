package com.genexus.android.core.controls.ads;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.genexus.android.core.base.controls.IGxControlRuntime;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.usercontrols.IGxUserControl;

public class SDAdsViewControl extends FrameLayout implements IGxUserControl, IGxControlRuntime
{
	private LayoutItemDefinition mDefinition = null;
	private IAdsViewController mController;

	public SDAdsViewControl(Context context, Coordinator coordinator, LayoutItemDefinition layoutItemDefinition)
	{
		super(context);
		ControlInfo info = layoutItemDefinition.getControlInfo();
		String providerId = info.optStringProperty("@SDAdsViewAdsProvider");
		IAdsProvider adsProvider = Ads.getProvider(providerId);

		if (adsProvider != null)
		{
			mController = adsProvider.createViewController(context, layoutItemDefinition);
			View adView = mController.createView();
			if (adView != null)
			{
				addView(adView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

				getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
				{
					@Override
					public void onGlobalLayout()
					{
						if (getWidth() > 0 || getHeight() > 0)
						{
							mController.setViewSize(Services.Device.pixelsToDips(getWidth()), Services.Device.pixelsToDips(getHeight()));
							getViewTreeObserver().removeOnGlobalLayoutListener(this);
						}
					}
				});
			}
		}
	}

	@Override
	public void setPropertyValue(String name, Value value)
	{
		if (mController != null)
			mController.setPropertyValue(name, value);
	}

	@Override
	public Value getPropertyValue(String name)
	{
		if (mController != null)
			return mController.getPropertyValue(name);
		else
			return null;
	}

	@Override
	public Value callMethod(String name, List<Value> parameters)
	{
		if (mController != null)
			return mController.callMethod(name, parameters);
		else
			return null;
	}
}

