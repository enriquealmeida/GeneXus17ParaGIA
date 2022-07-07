package com.genexus.android.controls.maps.google;

import android.content.Context;

import com.genexus.android.core.base.services.OnMapsConnectedCallback;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.maps.GooglePlaceHelper;
import com.genexus.android.core.framework.GenexusModule;

public class GoogleMapsModule implements GenexusModule, OnMapsConnectedCallback {
    @Override
    public void initialize(Context context) {
		Services.Application.getServicesLinker().registerMapsConnectedCallback(this);
    }

	@Override
	public void onMapsConnected() {
		Services.Maps.addProvider(new MapsProvider());
		GooglePlaceHelper.registerPickerPlaces(new GooglePlacePickerHelper() );
	}
}
