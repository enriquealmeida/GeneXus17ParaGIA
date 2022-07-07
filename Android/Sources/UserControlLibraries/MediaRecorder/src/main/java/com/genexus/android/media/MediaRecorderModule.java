package com.genexus.android.media;

import android.content.Context;

import com.genexus.android.core.externalapi.ExternalApiDefinition;
import com.genexus.android.core.externalapi.ExternalApiFactory;
import com.genexus.android.core.framework.GenexusModule;
import com.genexus.android.media.audio.AudioRecorderApi;

/**
 * Media Recorder Module
 */
public class MediaRecorderModule implements GenexusModule
{
	@Override
	public void initialize(Context context)
	{
		ExternalApiFactory.addApi(new ExternalApiDefinition(AudioRecorderApi.OBJECT_NAME, AudioRecorderApi.class));
	}
}
