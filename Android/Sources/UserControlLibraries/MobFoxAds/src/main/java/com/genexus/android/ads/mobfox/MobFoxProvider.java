package com.genexus.android.ads.mobfox;

import android.content.Context;
import androidx.annotation.NonNull;

import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.controls.ads.IAdsInterstitialController;
import com.genexus.android.core.controls.ads.IAdsProvider;
import com.genexus.android.core.controls.ads.IAdsViewController;

/**
 * MobFox Ads Provider.
 */
public class MobFoxProvider implements IAdsProvider
{
	@Override
	public @NonNull String getId()
	{
		return "mobfox";
	}

	@Override
	public @NonNull IAdsViewController createViewController(Context context, LayoutItemDefinition definition)
	{
		if (definition != null)
			return new GxAdViewController(context, definition.getControlInfo());
		else
			return new GxAdViewController(context, null);
	}

	@Override
	public IAdsInterstitialController getInterstitialController() {
		return null;
	}
}
