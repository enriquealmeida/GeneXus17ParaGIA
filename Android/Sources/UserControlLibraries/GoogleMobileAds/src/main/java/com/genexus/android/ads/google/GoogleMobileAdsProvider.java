package com.genexus.android.ads.google;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.controls.IGxControlRuntime;

import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.ads.IAdsInterstitialController;
import com.genexus.android.core.controls.ads.IAdsProvider;
import com.genexus.android.core.controls.ads.IAdsViewController;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.genexus.android.core.base.services.Services;

/**
 * GoobleMobileAds Provider. See https://developers.google.com/ad-manager/mobile-ads-sdk/ for further details on how to get AdUnitsIds and setup the sdk.
 */
public class GoogleMobileAdsProvider implements IAdsProvider
{
	@Override
	public @NonNull String getId()
	{
		return "googlemobileads";
	}

	@Override
	public @NonNull IAdsViewController createViewController(Context context, LayoutItemDefinition layoutItemDefinition)
	{
		return new AdsViewController(context, layoutItemDefinition);
	}

	@Override
	public IAdsInterstitialController getInterstitialController() {
		return GoogleMobileAdsInterstitialController.INSTANCE;
	}

	private static class AdsViewController implements IAdsViewController, IGxControlRuntime
	{
		private static final String TEST_UNIT_ID = "/6499/example/banner";
		private final Context mContext;
		private final LayoutItemDefinition mDefinition;
		private String mUnitId = "";
		private RelativeLayout mRelativeLayout;
		private int mWidth;
		private int mHeight;
		private boolean adRequested;

		private AdsViewController(Context context, LayoutItemDefinition def)
		{
			mContext = context;
			mDefinition = def;
			mUnitId = mDefinition.getControlInfo().optStringProperty("@SDAdsViewAdUnitId");
			mDefinition.setUnusableForReuse();
			initAds();
		}

		private void initAds()
		{
			if (mUnitId.equals(TEST_UNIT_ID)) {
				List<String> testDevices = new ArrayList<>();
				testDevices.add(AdRequest.DEVICE_ID_EMULATOR);

				RequestConfiguration requestConfiguration
					= new RequestConfiguration.Builder()
					.setTestDeviceIds(testDevices)
					.build();
				MobileAds.setRequestConfiguration(requestConfiguration);
			}
		}

		@Override
		public @Nullable View createView()
		{
			mRelativeLayout = new RelativeLayout(mContext);
			return mRelativeLayout;
		}

		@Override
		public void setPropertyValue(String name, Value value) {
			if (name.toLowerCase(java.util.Locale.US).endsWith("adunitid")) {
				mUnitId = value.coerceToString().trim();
			}
		}

		private void requestAdView(boolean forceRequest) {
			if (mWidth > 0 && mHeight > 0
				&& (!adRequested || forceRequest) )
			{
				// Initiate a request to load it with an ad
				PublisherAdRequest.Builder adRequestBuilder = new PublisherAdRequest.Builder();
				PublisherAdRequest adRequest = adRequestBuilder.build();
				mRelativeLayout.removeAllViews();
				// Create the view.
				PublisherAdView adView = new PublisherAdView(mContext);
				AdSize size = new AdSize(mWidth, mHeight);
				adView.setAdSizes(AdSize.SMART_BANNER, AdSize.BANNER, size);

				if (Strings.hasValue(mUnitId))
				{
					adView.setAdUnitId(mUnitId);
					adView.loadAd(adRequest);
				}
				mRelativeLayout.addView(adView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
				adRequested = true;
			}
		}

		@Override
		public Value callMethod(String name, List<Value> parameters) {
			//LoadAd
			if (Strings.hasValue(mUnitId)) {
				// Initiate a request to load it with an ad
				requestAdView(true);
			}
			else
			{
				Services.Log.warning("GoogleMobileAds", "adUnitId is empty!");
			}
			return null;
		}

		@Override
		public void setViewSize(int width, int height) {
			mWidth = width;
			mHeight = height;
			requestAdView( false);
		}
	}
}
