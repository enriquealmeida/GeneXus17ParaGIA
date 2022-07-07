package com.genexus.android.controls.maps.google;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.genexus.android.controls.maps.googlev2.R;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Function;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.maps.GxMapViewDefinition;
import com.genexus.android.core.controls.maps.common.IGxMapView;
import com.genexus.android.core.controls.maps.common.IMapOptions;
import com.genexus.android.core.controls.maps.common.IMapViewFactory;
import com.genexus.android.core.ui.Coordinator;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;
import com.google.android.libraries.places.api.Places;

class MapViewFactory extends IMapViewFactory<MapView> implements OnMapsSdkInitializedCallback {

	@Override
	public IGxMapView createGxMapView(final Activity activity, final Coordinator coordinator, final GxMapViewDefinition definition) {
		return createInstance(activity, (Function<Void, IGxMapView>) input -> new GxMapView(activity, coordinator, definition));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public MapView createProviderMapView(Activity activity, IMapOptions options) {
		return createInstance(activity, input -> new MapView(activity, (GoogleMapOptions) options.getInstance()));
	}

	@Override
	public void afterAddView(IGxMapView view) { }

	@Override
	protected boolean initializeSDK(@NonNull Activity activity) {
		if (!Strings.hasValue(getApiKey(activity))) {
			Services.Log.error("No key was provided for Google Maps API V2.");
			Services.Log.error("Please create one in the Google Developer Console.");
			Services.Log.error("Ensure that the Google Maps Android API v2 is enabled.");
			Services.Log.error("Put the API key in the main object property: Android Google Services API.");
			return false;
		}

		if (!GoogleMapsHelper.checkGoogleMapsV2(activity))
			return false;

		MapsInitializer.initialize(activity, MapsInitializer.Renderer.LATEST, this);

		if (!Places.isInitialized())
			Places.initialize(activity, getApiKey(activity));

		return true;
	}

	@Override
	public void onMapsSdkInitialized(@NonNull MapsInitializer.Renderer renderer) {
		Services.Log.debug("Using GoogleMaps renderer: " + renderer.name());
	}

	@Override
	protected String getApiKey(@NonNull Activity activity) {
		return activity.getResources().getString(R.string.GoogleServicesApiKey);
	}
}
