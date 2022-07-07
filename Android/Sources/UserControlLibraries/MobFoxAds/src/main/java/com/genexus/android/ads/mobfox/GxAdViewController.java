package com.genexus.android.ads.mobfox;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import androidx.annotation.Nullable;
import android.view.View;

import com.adsdk.sdk.Ad;
import com.adsdk.sdk.AdListener;
import com.adsdk.sdk.Gender;
import com.adsdk.sdk.banner.AdView;
import com.genexus.android.core.base.controls.IGxControlRuntime;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.controls.ads.IAdsViewController;

/**
 * MobFox AdView controller.
 */
public class GxAdViewController implements AdListener, IGxControlRuntime, IAdsViewController
{
	private ControlInfo mControlInfo;
	private String mKeywords;
	private static final String REQUEST_URL = "http://my.mobfox.com/request.php";
	private AdView mView;
	private Context mContext;

	public GxAdViewController(Context context, ControlInfo info)
	{
		mControlInfo = info;
		mContext = context;
	}

	@Override
	public void setViewSize(int width, int height)
    {
		if (mView == null)
			return;

    	mView.setAdspaceWidth(width);
		mView.setAdspaceHeight(height);
	    // mView.startReloadTimer();
		mView.loadNextAd();
	}

	@Override
	public @Nullable View createView()
    {
		if (mControlInfo == null)
			return null; // TODO: Support creating MobFox ads with no control info (for "automatic" ads).

		mView = new AdView(mContext, REQUEST_URL, mControlInfo.optStringProperty("@SDAdsViewPublisherId"), true, true);
		int userAge = mControlInfo.optIntProperty("@SDAdsViewUserAge");
		if (userAge > 0)
			mView.setUserAge(userAge);
		String gender = mControlInfo.optStringProperty("@SDAdsViewUserGender");
		if (gender.equalsIgnoreCase("female"))
			mView.setUserGender(Gender.FEMALE);
		if (gender.equalsIgnoreCase("male"))
			mView.setUserGender(Gender.MALE);
		mKeywords = mControlInfo.optStringProperty("@SDAdsViewKeywords");
		if (mKeywords.length() > 0)
			mView.setKeywords(Arrays.asList(mKeywords.split(" ")));
		mView.setAdspaceStrict(false); // Optional, tells the server to only supply banner ads that are exactly of the desired size. Without setting it, the server could also supply smaller Ads when no ad of desired size is available.
		mView.setAdListener(this);
		return mView;
	}

	@Override
	public void adClicked() { }

	@Override
	public void adClosed(Ad ad, boolean b) { }

	@Override
	public void adLoadSucceeded(Ad ad) { }

	@Override
	public void adShown(Ad ad, boolean b) { }

	@Override
	public void noAdFound() { }

	@Override
	public void setPropertyValue(String name, Value value)
    {
        if (mView == null)
            return;

		if (name.equalsIgnoreCase("keywords"))
        {
			mKeywords = value.coerceToString();
			if (mKeywords.length() > 0)
				mView.setKeywords(Arrays.asList(mKeywords.split(" ")));
		}
	}

	@Override
	public Value getPropertyValue(String name)
    {
        if (mView == null)
            return null;

		if (name.equalsIgnoreCase("keywords"))
			return Value.newString(mKeywords);

		return null;
	}

	@Override
	public Value callMethod(String name, List<Value> parameters)
    {
        if (mView == null)
            return null;

		if (name.equalsIgnoreCase("requestad"))
			mView.loadNextAd();
		return null;
	}
}
