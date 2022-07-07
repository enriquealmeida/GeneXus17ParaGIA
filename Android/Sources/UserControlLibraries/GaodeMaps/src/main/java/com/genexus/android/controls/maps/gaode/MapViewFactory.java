package com.genexus.android.controls.maps.gaode;

import android.app.Activity;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Function;
import com.genexus.android.core.controls.maps.GxMapViewDefinition;
import com.genexus.android.core.controls.maps.common.IGxMapView;
import com.genexus.android.core.controls.maps.common.IMapOptions;
import com.genexus.android.core.controls.maps.common.IMapViewFactory;
import com.genexus.android.core.ui.Coordinator;

class MapViewFactory extends IMapViewFactory<MapView> {

	@Override
	public IGxMapView createGxMapView(final Activity activity, final Coordinator coordinator, final GxMapViewDefinition definition) {
		return createInstance(activity, (Function<Void, IGxMapView>) input -> new GxMapView(activity, coordinator, definition));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public MapView createProviderMapView(Activity activity, IMapOptions options)  {
		return createInstance(activity, input -> new MapView(activity, (AMapOptions) options.getInstance()));
	}

	@Override
	public void afterAddView(IGxMapView view) { }

	@Override
	protected boolean initializeSDK(@NonNull Activity activity) {
		try {
			MapsInitializer.initialize(activity);
		} catch (RemoteException e) {
			Services.Log.error(e);
			return false;
		}

		return true;
	}

	@Override
	protected String getApiKey(@NonNull Activity activity) {
		return activity.getResources().getString(R.string.MapsApiKey);
	}
}
