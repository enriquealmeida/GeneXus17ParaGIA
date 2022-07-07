package com.genexus.android.twitter;

import android.content.Context;

import com.genexus.android.core.externalapi.ExternalApiDefinition;
import com.genexus.android.core.externalapi.ExternalApiFactory;
import com.genexus.android.core.framework.GenexusModule;


public class TwitterModule implements GenexusModule {

	@Override
	public void initialize(Context context) {
		ExternalApiFactory.addApi(new ExternalApiDefinition(TwitterApi.OBJECT_NAME, TwitterApi.class));
	}
}
