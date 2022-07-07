package com.genexus.android.core.controls.maps.common;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Function;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.maps.GxMapViewDefinition;
import com.genexus.android.core.ui.Coordinator;

@SuppressWarnings("rawtypes")
public abstract class IMapViewFactory<MapViewT extends View> {

	private boolean mIsInitialized = false;

	public MapViewT getProviderMapView(Activity activity, IMapOptions options) {
		return createProviderMapView(activity, options);
	}

	public IGxMapView getGxMapView(Activity activity, Coordinator coordinator, GxMapViewDefinition definition) {
		return createGxMapView(activity, coordinator, definition);
	}

	protected <ViewT> ViewT createInstance(Activity activity, Function<Void, ViewT> creator) {
		if (!Strings.hasValue(getApiKey(activity))) {
			Services.Log.error("No key was provided for selected Maps Provider API");
			return null;
		}

		if (!mIsInitialized && !initializeSDK(activity)) {
			Services.Log.error("Maps Provider SDK cannot be initialized");
			return null;
		}

		try {
			mIsInitialized = true;
			return creator.run(null);
		} catch (@SuppressWarnings("checkstyle:IllegalCatch") Exception e) {
			Services.Log.error("Could not create Maps control instance", e);
			mIsInitialized = false;
			return null;
		}
	}

	public abstract IGxMapView createGxMapView(Activity activity, Coordinator coordinator, GxMapViewDefinition definition);
	public abstract MapViewT createProviderMapView(Activity activity, IMapOptions options);
	public abstract void afterAddView(IGxMapView view);
	protected abstract String getApiKey(@NonNull Activity activity);
	protected abstract boolean initializeSDK(@NonNull Activity activity);
}
