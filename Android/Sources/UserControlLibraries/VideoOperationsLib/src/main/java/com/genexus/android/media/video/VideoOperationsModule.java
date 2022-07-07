package com.genexus.android.media.video;

import android.content.Context;

import com.genexus.android.core.externalapi.ExternalApiDefinition;
import com.genexus.android.core.externalapi.ExternalApiFactory;
import com.genexus.android.core.framework.GenexusModule;

public class VideoOperationsModule implements GenexusModule {

	@Override
	public void initialize(Context context) {
		ExternalApiFactory.addApi(new ExternalApiDefinition(VideoOperationsAPI.OBJECT_NAME, VideoOperationsAPI.class, false));
	}
}
