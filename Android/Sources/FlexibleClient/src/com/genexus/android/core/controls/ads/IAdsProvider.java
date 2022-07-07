package com.genexus.android.core.controls.ads;

import android.content.Context;
import androidx.annotation.NonNull;

import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;

/**
 * Ads Provider interface.
 */
public interface IAdsProvider
{
	/***
	 * Get the id for this provider. ie: "mobfox", "admob", "smartadserver", etc
	 */
	@NonNull String getId();

	/***
	 * Create a view controller for a banner
	 */
	@NonNull IAdsViewController createViewController(Context context, LayoutItemDefinition layoutItemDefinition);

	/***
	 * Get the controller for interstitial or null if it doesn't support it
	 */
	IAdsInterstitialController getInterstitialController();
}
