package com.genexus.android.controls.maps.gaode;

import android.content.Context;

import com.genexus.android.core.base.services.OnMapsConnectedCallback;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.framework.GenexusModule;

public class GaodeMapsModule implements GenexusModule, OnMapsConnectedCallback {
	@Override
	public void initialize(Context context) {
		Services.Application.getServicesLinker().registerMapsConnectedCallback(this);
	}

	@Override
	public void onMapsConnected() {
		Services.Maps.addProvider(new MapsProvider());
	}
}
