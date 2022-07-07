package com.genexus.android.media.cast;

import java.util.List;

import android.content.Context;

import com.genexus.android.media.MediaPlayerModule;
import com.google.android.gms.cast.framework.CastOptions;
import com.google.android.gms.cast.framework.OptionsProvider;
import com.google.android.gms.cast.framework.SessionProvider;

/**
 * Google Cast options provider.
 * Set ups Google Cast receiver application id and other parameters.
 */
public class CastOptionsProvider implements OptionsProvider {

	@Override
	public CastOptions getCastOptions(Context context) {
		return new CastOptions.Builder()
				.setReceiverApplicationId(MediaPlayerModule.getGoogleCastApplicationId())
				.build();
	}

	@Override
	public List<SessionProvider> getAdditionalSessionProviders(Context context) {
		return null;
	}
}
