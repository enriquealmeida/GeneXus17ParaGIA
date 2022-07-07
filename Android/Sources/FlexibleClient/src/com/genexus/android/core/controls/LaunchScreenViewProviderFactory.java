package com.genexus.android.core.controls;

import androidx.annotation.NonNull;

import com.genexus.diagnostics.Log;

public class LaunchScreenViewProviderFactory {
	private static LaunchScreenViewProvider sProvider;

	public static void setProvider(@NonNull LaunchScreenViewProvider provider) {
		sProvider = provider;
	}

	public static LaunchScreenViewProvider getLaunchScreenViewProvider() {
		if (sProvider == null) {
			Log.debug("Using the default launch screen provider as there was none registered");
			sProvider = new DefaultLaunchScreenViewProvider();
		}

		return sProvider;
	}
}
