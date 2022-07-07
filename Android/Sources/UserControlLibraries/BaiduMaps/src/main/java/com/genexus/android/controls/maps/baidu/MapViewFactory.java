package com.genexus.android.controls.maps.baidu;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Function;
import com.genexus.android.core.controls.maps.GxMapViewDefinition;
import com.genexus.android.core.controls.maps.common.IGxMapView;
import com.genexus.android.core.controls.maps.common.IMapOptions;
import com.genexus.android.core.controls.maps.common.IMapViewFactory;
import com.genexus.android.core.ui.Coordinator;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapView;

class MapViewFactory extends IMapViewFactory<MapView> {

	private static BMapManager sMapManager;

	@Override
	public IGxMapView createGxMapView(Activity activity, Coordinator coordinator, GxMapViewDefinition definition) {
		return createInstance(activity, (Function<Void, IGxMapView>) input -> new GxMapView(activity, coordinator, definition));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public MapView createProviderMapView(Activity activity, IMapOptions options) {
		return createInstance(activity, input -> new MapView(activity, (BaiduMapOptions) options.getInstance()));
	}

	@Override
	public void afterAddView(IGxMapView view) { }

	@Override
	protected boolean initializeSDK(@NonNull Activity activity) {
		/* Although Baidu's initialize method receives a Context parameter, it later specifically requires
		 that the Context passed to initialize the SDK must be an Application instance as it will crash otherwise.
		 Passing the Application instance to the IMapViewFactory.initializeSDK method is not the best way
		 to solve this and we can't either receive a Context here as Google's initialization needs an Activity
		 (as it is currently declared) */
		SDKInitializer.initialize(Services.Application.getAndroidApplication());

		//sMapManager = new BMapManager(context);
		//sMapManager.init(apiKey, sGeneralListener);
		//sMapManager.start();
		//TODO Baidu: error if not have permisson

		return true;
	}

	@Override
	protected String getApiKey(@NonNull Activity activity) {
		return activity.getResources().getString(R.string.MapsApiKey);
	}
}
