package com.genexus.controls.maps.googlev2;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.artech.base.metadata.theme.ThemeClassDefinition;
import com.artech.base.model.Entity;
import com.artech.base.services.Services;
import com.artech.base.utils.Strings;
import com.artech.controls.maps.GxMapViewDefinition;
import com.artech.controls.maps.common.MapLayer;
import com.artech.controls.maps.common.MapPinHelper;
import com.artech.utils.Cast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("InconsistentCapitalization")
public class GxMapViewFeatures {

	private final GoogleMap mMap;
	private HashMap<String, GxMapViewItem> mFeatureData;

	private LoaderTask mTask;
	private MapPinHelper mPinHelper;

	private boolean mUpdateAnimation;
	private HashMap<String, MapLayer.MapFeature> mFeatures;
	private GxMapViewDefinition mDefinition;
	private AnimationLayer mAnimationLayer;

	private ThemeClassDefinition mDefaultPolygonClass;
	private ThemeClassDefinition mDefaultLineClass;

	private GxMapView.OnGeographiesLoadedCallback mGeographiesLoadedCallback;

	public GxMapViewFeatures(@NonNull Context context, @NonNull GoogleMap map, @NonNull GxMapViewDefinition definition,
							 GxMapView.OnGeographiesLoadedCallback onGeographiesLoadedCallback) {
		mMap = map;
		mFeatureData = new HashMap<>();
		mFeatures = new HashMap<>();
		mPinHelper = new MapPinHelper(context, definition);
		mDefinition = definition;
		mDefaultLineClass = definition.getRouteClass();
		mDefaultPolygonClass = definition.getPolygonClass();
		mGeographiesLoadedCallback = onGeographiesLoadedCallback;
	}

	public void update(GxMapViewData mapData, boolean useAnimation) {
		if (mTask != null)
			mTask.cancel(true);

		mUpdateAnimation = useAnimation;

		if (!useAnimation) {
			mMap.clear();
			mFeatures.clear();
			mFeatureData.clear();
		}

		mTask = new LoaderTask();
		mTask.execute(mapData);
	}

	Entity getFeatureData(Object mapObject) {
		String featureKey = getFeatureKey(mapObject);
		if (featureKey != null && !featureKey.isEmpty()) {
			GxMapViewItem mapItem = mFeatureData.get(featureKey);
			if (mapItem != null)
				return mapItem.getData();
		}
		return null;
	}

	String getFeatureKey(Object mapObject) {
		Class cls = mapObject.getClass();
		String featureKey = "";
		try {
			Method method = cls.getDeclaredMethod("getTag", (Class<?>[]) null);
			featureKey = Cast.as(String.class, method.invoke(mapObject));
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			Services.Log.error(e);
		}
		return featureKey;
	}

	private class LoaderTask extends AsyncTask<GxMapViewData, FeatureInfo, Void> {

		List<MapLayer.MapFeature> loadedFeatures = new ArrayList<>();

		@Override
		protected Void doInBackground(GxMapViewData... params) {
			GxMapViewData mapData = params[0];
			for (GxMapViewItem item : mapData) {
				if (isCancelled())
					return null;

				switch (item.getFeatureType()) {
					case Point:
						MarkerOptions markerOptions = buildDefaultMarkerOptions(item);
						publishProgress(new FeatureInfo(markerOptions, item, MapLayer.FeatureType.Point));
						break;
					case Polyline:
						PolylineOptions polylineOptions = buildDefaultPolylineOptions(item.getLocationList());
						publishProgress(new FeatureInfo(polylineOptions, item, MapLayer.FeatureType.Polyline));
						break;
					case Polygon:
						PolygonOptions polygonOptions = buildDefaultPolygonOptions(item.getLocationList());
						publishProgress(new FeatureInfo(polygonOptions, item, MapLayer.FeatureType.Polygon));
						break;
				}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(FeatureInfo... values) {
			FeatureInfo featureInfo = values[0];
			String featureKey;

			MapLayer.MapFeature feature = null;
			switch (featureInfo.Type) {
				case Point:
					feature = new MapLayer.Point();
					featureKey = feature.id;
					feature.getPoints().add(new MapLocation(((MarkerOptions) featureInfo.Options).getPosition()));
					String animationMarkerKey = "";

					if (mUpdateAnimation) {
						Services.Log.debug(" onProgressUpdate " + mUpdateAnimation + " ");
						animationMarkerKey = AnimationLayer.getAnimationMarkerKey(featureInfo.Data, mDefinition);
						if (Strings.hasValue(animationMarkerKey))
							feature = mFeatures.get(animationMarkerKey);
						Services.Log.debug(" onProgressUpdate " + animationMarkerKey);
					}

					if (feature != null) {
						Marker marker = mMap.addMarker((MarkerOptions) featureInfo.Options);
						marker.setTag(featureKey);
						feature.mapObject = marker;
						mFeatureData.put(featureKey, featureInfo.Data);
					}

					if (Strings.hasValue(animationMarkerKey)) {
						MapLayer.MapFeature oldFeature = mFeatures.get(animationMarkerKey);
						if (oldFeature != null && oldFeature.mapObject != null) {
							if (mAnimationLayer == null)
								mAnimationLayer = new AnimationLayer();

							MarkerOptions markerOptions = (MarkerOptions) featureInfo.Options;
							Services.Log.debug(" animateMarkers " + animationMarkerKey + " to " + markerOptions.getPosition());
							mAnimationLayer.animateMarkers(featureInfo.Data, mDefinition, markerOptions.getPosition(), (Marker) feature.mapObject);
						}
						mFeatures.put(animationMarkerKey, feature);
					}

					break;
				case Polyline:
					feature = new MapLayer.Polyline();
					featureKey = feature.id;
					feature.getPoints().addAll(MapLocation.listFromLatLng(((PolylineOptions) featureInfo.Options).getPoints()));
					Polyline polyline = mMap.addPolyline((PolylineOptions) featureInfo.Options);
					polyline.setTag(featureKey);
					feature.mapObject = polyline;
					mFeatureData.put(featureKey, featureInfo.Data);
					break;
				case Polygon:
					feature = new MapLayer.Polygon();
					featureKey = feature.id;
					feature.getPoints().addAll(MapLocation.listFromLatLng(((PolygonOptions) featureInfo.Options).getPoints()));
					Polygon polygon = mMap.addPolygon((PolygonOptions) featureInfo.Options);
					polygon.setTag(featureKey);
					feature.mapObject = polygon;
					mFeatureData.put(featureKey, featureInfo.Data);
					break;
			}
			if (feature != null && feature.getPoints().size() > 0)
				loadedFeatures.add(feature);
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			if (loadedFeatures.size() > 0)
				mGeographiesLoadedCallback.geographiesLoaded(loadedFeatures);
		}
	}

	@SuppressWarnings("checkstyle:MemberName")
	private static class FeatureInfo {
		final Object Options;
		final GxMapViewItem Data;
		final MapLayer.FeatureType Type;

		FeatureInfo(Object options, GxMapViewItem data, MapLayer.FeatureType featureType) {
			Options = options;
			Data = data;
			Type = featureType;
		}
	}

	private MarkerOptions buildDefaultMarkerOptions(GxMapViewItem item) {
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(item.getLocationList().get(0).getLatLng());
//		markerOptions.draggable(true); //TODO: Uncomment if possible to drag server Geopoints
		markerOptions.icon(getMarkerImage(item.getData()));
		return markerOptions;
	}

	private PolylineOptions buildDefaultPolylineOptions(List<MapLocation> locationList) {
		PolylineOptions polylineOptions = new PolylineOptions();

		for (MapLocation coordinates : locationList)
			polylineOptions.add(coordinates.getLatLng());

		polylineOptions.geodesic(true);
		polylineOptions.clickable(true);
		GoogleMapsHelper.applyClassToPolylineOptions(mDefaultLineClass, polylineOptions);
		return polylineOptions;
	}

	private PolygonOptions buildDefaultPolygonOptions(List<MapLocation> locationList) {
		PolygonOptions polygonOptions = new PolygonOptions();

		for (MapLocation coordinates : locationList)
			polygonOptions.add(coordinates.getLatLng());

		polygonOptions.geodesic(true);
		polygonOptions.clickable(true);
		GoogleMapsHelper.applyClassToPolygonOptions(mDefaultPolygonClass, polygonOptions);
		return polygonOptions;
	}

	private BitmapDescriptor getMarkerImage(Entity itemData) {
		MapPinHelper.ResourceOrBitmap pin = mPinHelper.getPinImage(itemData);

		if (pin.resourceId != null)
			return BitmapDescriptorFactory.fromResource(pin.resourceId);
		else if (pin.bitmap != null)
			return BitmapDescriptorFactory.fromBitmap(pin.bitmap);
		else
			return BitmapDescriptorFactory.defaultMarker();
	}
}
