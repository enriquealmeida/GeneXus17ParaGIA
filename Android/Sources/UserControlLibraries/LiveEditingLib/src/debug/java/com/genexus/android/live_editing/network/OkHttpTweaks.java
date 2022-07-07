package com.genexus.android.live_editing.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;

/**
 * Set up a Stetho interceptor for debugging Live Editing.
 */
final class OkHttpTweaks
{
	static void configure(OkHttpClient.Builder builder)
	{
		builder.addNetworkInterceptor(new StethoInterceptor());
	}
}
