package com.genexus.android.beacons;

import android.content.Context;

import com.genexus.android.core.externalapi.ExternalApiDefinition;
import com.genexus.android.core.externalapi.ExternalApiFactory;
import com.genexus.android.core.framework.GenexusModule;

public class BeaconsModule implements GenexusModule {
	@Override
	public void initialize(Context context) {
		ExternalApiFactory.addApi(new ExternalApiDefinition(BeaconsAPI.OBJECT_NAME, BeaconsAPI.class, false));
	}
}
