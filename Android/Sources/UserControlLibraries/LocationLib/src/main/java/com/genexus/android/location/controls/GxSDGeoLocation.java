package com.genexus.android.location.controls;

import android.content.Context;
import android.view.View;

import androidx.annotation.Nullable;

import com.genexus.android.core.base.metadata.enums.ImageScaleType;
import com.genexus.android.core.base.metadata.layout.CellDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.GeoFormats;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.GxImageViewDataCustom;
import com.genexus.android.core.controls.IGxEdit;
import com.genexus.android.core.controls.IHandleSemanticDomain;
import com.genexus.android.core.ui.Coordinator;

import java.util.Locale;

public class GxSDGeoLocation extends GxImageViewDataCustom implements IHandleSemanticDomain
{
	private final LayoutItemDefinition mDefinition;
	private String mMapType;
	private int mMapZoom;
	private String mValue;
	private boolean mShouldReloadImage;
	private final Coordinator mCoordinator;

	private View mMapLiteCustomView = null;
	private OnClickListener mOnClickListener = null;

	public GxSDGeoLocation(Context context, Coordinator coordinator, LayoutItemDefinition def)
	{
		super(context, def);
		mDefinition = def;
		mCoordinator = coordinator;
		mShouldReloadImage = true;

		mMapType = Strings.toLowerCase(def.getControlInfo().optStringProperty("@SDGeoLocationMapType"));
		mMapZoom = def.getControlInfo().optIntProperty("@SDGeoLocationMapZoom", 15);
	}

	@Override
	public String getGxValue()
	{
		return mValue;
	}

	@Override
	public void setGxValue(String value)
	{
		mValue = value;
		mShouldReloadImage = true;
		setImageScaleType(ImageScaleType.FIT);
		loadMapImage(false);

		// create lite map view, if provider do it
		// create it here only, and NOT in OnLayout , because in onLayout will fail, because change control childs.
		// also the size calculate in onLayout are not necessary for this custom view (Map Lite)
		String geolocation = GeoFormats.convertToInternal(mValue, mDefinition.getDataItem());
		mMapLiteCustomView = Services.Maps.getMapLiteView(geolocation, mMapType, mMapZoom);
		if (mMapLiteCustomView!=null) {
			this.setCustomView(mMapLiteCustomView);
			// set click listener to inner mapView
			mMapLiteCustomView.setOnClickListener(mOnClickListener);
		}

	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	{
		super.onLayout(changed, left, top, right, bottom);
		if (!Services.Application.isLiveEditingEnabled()) {
			// Disable loading the map on every layout since it creates an infinite loop when using LiveInspector's
			// Auto-Refresh (since it uses onGlobalLayoutChange callback to detect changes).
			loadMapImage(true);
		}
	}

    private void loadMapImage(final boolean isInLayoutPass)
    {
    	if (mValue == null || !mShouldReloadImage)
    		return;
    	
        int width = getWidth();
        int height = getHeight();

        boolean isFullyWrapContent = getLayoutParams() != null &&
                getLayoutParams().height == LayoutParams.WRAP_CONTENT &&
                getLayoutParams().width == LayoutParams.WRAP_CONTENT;

        if (width == 0 && height == 0 && !isFullyWrapContent)
            return;

        if (width == 0)
        	width = ((CellDefinition) mDefinition.getParent()).getAbsoluteWidth();

        if (height == 0)
        	height = ((CellDefinition) mDefinition.getParent()).getAbsoluteHeight();

        if (width > 1980) { //Max static map image width is 1980
			height = (1980 * height) / width;
			width = 1980;
		}

        if (height > 1024) { //Max static map image height is 1024
			width = (1024 * width) / height;
			height = 1024;
		}

		String geolocation = GeoFormats.convertToInternal(mValue, mDefinition.getDataItem());
        String language = Services.Language.getCurrentLanguageProperty("ISOLangCode");
        if (Strings.hasValue(language)) language = language.toLowerCase(Locale.ROOT);
		String requestMapUri = Services.Maps.getMapImageUrl(geolocation, width, height, mMapType, mMapZoom, language);
		if (Strings.hasValue(requestMapUri)) {
			mShouldReloadImage = false;
			super.setGxValue(requestMapUri);
		}
    }

	@Override
	public IGxEdit getViewControl() { return this; }

	@Override
	public IGxEdit getEditControl()
	{
		GxLocationEdit edit = new GxLocationEdit(getContext(), mCoordinator, mDefinition);
		edit.setShowMap(true);
		return edit;
	}

	@Override
	public void setOnClickListener(@Nullable OnClickListener onClickListener) {
		super.setOnClickListener(onClickListener);
		// set click listener to inner mapView
		if (mMapLiteCustomView!=null)
			mMapLiteCustomView.setOnClickListener(onClickListener);
		mOnClickListener = onClickListener;
	}
}
