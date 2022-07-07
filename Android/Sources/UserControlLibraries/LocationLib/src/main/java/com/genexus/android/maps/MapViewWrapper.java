package com.genexus.android.maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.genexus.android.core.actions.ICustomMenuManager;
import com.genexus.android.core.base.controls.IGxControlPreserveState;
import com.genexus.android.core.base.controls.IGxControlRuntime;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.layout.GridDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.model.BaseCollection;
import com.genexus.android.core.base.model.ValueCollection;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.GeoFormats;
import com.genexus.android.core.base.utils.NameMap;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.controls.GxListView;
import com.genexus.android.core.controls.IGridView;
import com.genexus.android.core.controls.grids.GridHelper;
import com.genexus.android.core.controls.maps.GxMapViewDefinition;
import com.genexus.android.core.controls.maps.common.IGeographiesManager;
import com.genexus.android.core.controls.maps.common.IGxMapView;
import com.genexus.android.core.controls.maps.common.IGxMapViewRuntimeMethods;
import com.genexus.android.core.controls.maps.common.IMapLocation;
import com.genexus.android.core.controls.maps.common.IMapViewFactory;
import com.genexus.android.core.controls.maps.common.IGxMapViewSupportLayers;
import com.genexus.android.core.controls.maps.common.OnGeographiesManagerCreatedCallback;
import com.genexus.android.maps.kml.KmlReaderTask;
import com.genexus.android.core.controls.maps.common.MapLayer;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.Cast;
import com.genexus.android.core.utils.TaskRunner;
import com.genexus.android.core.utils.ThemeUtils;

import androidx.annotation.NonNull;

@SuppressWarnings({"rawtypes", "unused"})
public class MapViewWrapper extends RelativeLayout implements IGridView, ICustomMenuManager,
		IGxControlRuntime, IGxControlPreserveState, OnGeographiesManagerCreatedCallback {

	private final Coordinator mCoordinator;
	private final IMapViewFactory mFactory;
	private final GxMapViewDefinition mDefinition;
	private IGridView mView;

	private IGeographiesManager mGeographiesManager;

	// Properties
	private static final String PROPERTY_MAP_TYPE = "MapType";
	private static final String PROPERTY_MAP_DIRECTION_LAYER = "DirectionsLayer";
	private static final String PROPERTY_MAP_ANIMATION_LAYER = "AnimationLayer";
	private static final String PROPERTY_MAP_SELECTION_LAYER = "SelectionLayer";
	private static final String PROPERTY_EDITABLE_GEOGRAPHIES = "EditableGeographies";

	// Methods
	private static final String METHOD_LOAD_KML = "LoadKML";
	private static final String METHOD_LOAD_KML_LAYER = "LoadKMLLayer";
	private static final String METHOD_LOAD_KML_LAYER_FILE = "LoadKMLLayerFile";
	private static final String METHOD_GET_LAYER_VISIBLE = "GetLayerVisible";
	private static final String METHOD_SET_LAYER_VISIBLE = "SetLayerVisible";
	private static final String METHOD_ADJUST_VISIBLE_AREA_TO_LAYER = "AdjustVisibleAreaToLayer";
	private static final String METHOD_GET_LAYERS = "GetLayers";
	private static final String METHOD_DRAW_GEOLINE = "DrawGeoLine";
	private static final String METHOD_DRAW_GEOGRAPHY = "DrawGeography";
	private static final String METHOD_SAVE_EDITION = "SaveEdition";
	private static final String METHOD_CLEAR = "Clear";
	private static final String METHOD_SET_MAPCENTER = "SetMapCenter";
	private static final String METHOD_SET_ZOOMLEVEL = "SetZoomLevel";
	private static final String METHOD_VISIBLE_REGION = "GetVisibleRegion";

	public MapViewWrapper(Context context, Coordinator coordinator, LayoutItemDefinition layoutDefinition) {
		super(context);
		mFactory = Services.Maps.getMapViewFactory();
		mCoordinator = coordinator;
		mDefinition = new GxMapViewDefinition(context, (GridDefinition) layoutDefinition);
		mGeographiesManager = null;

		if (mFactory != null)
			mView = mFactory.createGxMapView((Activity) getContext(), coordinator, mDefinition);

		if (mView == null) {
			// We couldn't create a MapView, probably because the device doesn't have API support.
			// Use a normal ListView in that case.
			mView = new GxListView(context, coordinator, (GridDefinition) layoutDefinition);
		}

		// Unlink from a possible parent before adding here.
		View view = (View) mView;
		if (view.getParent() != null)
			((ViewGroup) view.getParent()).removeView(view);

		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		addView(view, lp);

		if (mFactory != null && mView != null && mView instanceof IGxMapView)
			mFactory.afterAddView((IGxMapView) mView);
	}

	@Override
	public void addListener(GridEventsListener listener) {
		mView.addListener(listener);
	}

	@Override
	public void update(ViewData data) {
		//This is needed so the wrapper knows about the layers loaded from the server data and therefore
		//it will be able to add/remove/hide/show these geographies that otherwise it wouldn't have access to
		final IGxMapViewSupportLayers mapView = Cast.as(IGxMapViewSupportLayers.class, mView);
		if (mapView != null)
			mapView.setGeographiesManagerCreatedCallback(this);

		mView.update(data);
	}

	@Override
	public void geographiesManagerCreated(@NonNull IGeographiesManager geographiesManager) {
		mGeographiesManager = geographiesManager;
	}

	public String getControlName() {
		return mDefinition.getGrid().getName();
	}

	@Override
	public void onCustomCreateOptionsMenu(Menu menu) {
		if (mView instanceof IGxMapView && mDefinition.getCanChooseMapType())
			MapViewHelper.addMapTypeMenu((IGxMapView) mView, menu);
	}

	public MapViewWrapper(Context context) {
		super(context);
		throw new UnsupportedOperationException("Unsupported constructor.");
	}

	@Override
	public Value getPropertyValue(String name) {
		// Maps must implement the interface to support these methods.
		final IGxMapViewRuntimeMethods mapView = Cast.as(IGxMapViewRuntimeMethods.class, mView);

		if (mapView != null) {
			switch (name) {
				case GridHelper.PROPERTY_SELECTED_INDEX:
					return Value.newInteger(mapView.getSelectedIndex() + 1);
				case PROPERTY_EDITABLE_GEOGRAPHIES:
					return Value.newString(mapView.getEditableGeographies());
				case PROPERTY_MAP_SELECTION_LAYER:
					return Value.newBoolean(mapView.getSelectionLayer());
				default:
					Services.Log.warning("Unsupported get map property: " + name);
			}
		}

		return null;
	}

	@Override
	public void setPropertyValue(String name, Value value) {
		// Maps must implement the interface to support these methods.
		final IGxMapViewRuntimeMethods mapView = Cast.as(IGxMapViewRuntimeMethods.class, mView);
		if (mapView != null) {
			if (PROPERTY_MAP_DIRECTION_LAYER.equalsIgnoreCase(name))
				mapView.setDirectionsLayer(value.coerceToBoolean());
			else if (PROPERTY_MAP_ANIMATION_LAYER.equalsIgnoreCase(name))
				mapView.setAnimationLayer(value.coerceToBoolean());
			else if (PROPERTY_EDITABLE_GEOGRAPHIES.equalsIgnoreCase(name))
				mapView.setEditableGeographies(value.coerceToString());
			else if (PROPERTY_MAP_SELECTION_LAYER.equalsIgnoreCase(name))
				mapView.setSelectionLayer(value.coerceToBoolean());
			else
				Services.Log.warning("Unsupported set map property: " + name);
		} else
			Services.Log.warning("Map control Unsupported runtime properties. set property: " + name);
	}


	@Override
	public Value callMethod(String name, List<Value> parameters) {
		Value value = runLayersMethods(name, parameters);
		if (value == null)
			value = runRuntimeMethods(name, parameters);

		return value;
	}

	private Value runLayersMethods(String name, List<Value> parameters) {
		final IGxMapViewSupportLayers mapView = Cast.as(IGxMapViewSupportLayers.class, mView);
		if (mapView == null) // Maps must implement the interface to support these methods.
			return null;

		if ((METHOD_LOAD_KML.equalsIgnoreCase(name) || METHOD_LOAD_KML_LAYER.equalsIgnoreCase(name)
				|| METHOD_LOAD_KML_LAYER_FILE.equalsIgnoreCase(name)) && parameters.size() == 3) {
			String layerId = parameters.get(0).coerceToString();
			String kmlString = parameters.get(1).coerceToString();
			boolean allowSelection = parameters.get(2).coerceToBoolean();
			boolean isFile = METHOD_LOAD_KML_LAYER_FILE.equalsIgnoreCase(name);
			return Value.newInteger(drawKMLLayer(mapView, layerId, kmlString, allowSelection, isFile));
		} else if (METHOD_GET_LAYER_VISIBLE.equalsIgnoreCase(name) && parameters.size() == 1) {
			String layerId = parameters.get(0).coerceToString();
			return Value.newBoolean(getVisibleForLayer(layerId));
		} else if (METHOD_SET_LAYER_VISIBLE.equalsIgnoreCase(name) && parameters.size() == 2) {
			String layerId = parameters.get(0).coerceToString();
			if (layerId.trim().isEmpty())
				return null;

			boolean visibleValue = parameters.get(1).coerceToBoolean();
			MapLayer layer = getMapLayers().get(layerId);
			if (layer != null)
				mapView.setLayerVisible(layer, visibleValue);
		} else if (METHOD_GET_LAYERS.equalsIgnoreCase(name) && parameters.size() == 0) {
			BaseCollection<String> collection = new ValueCollection(Expression.Type.STRING);
			for (Map.Entry<String, MapLayer> entry : getMapLayers().entrySet())
				collection.add(entry.getKey());

			return Value.newCollection(collection);
		} else if (METHOD_ADJUST_VISIBLE_AREA_TO_LAYER.equalsIgnoreCase(name) && parameters.size() == 1) {
			MapLayer layer = getMapLayers().get(parameters.get(0).coerceToString());
			if (layer != null)
				mapView.adjustBoundsToLayer(layer);
		} else if ((METHOD_DRAW_GEOGRAPHY.equalsIgnoreCase(name) || METHOD_DRAW_GEOLINE.equalsIgnoreCase(name)) && parameters.size() > 0) {
			String geography = parameters.get(0).coerceToString();
			ThemeClassDefinition themeClass = null;
			if (parameters.size() >= 2) {
				String geographyClass = parameters.get(1).coerceToString();
				if (Strings.hasValue(geographyClass))
					themeClass = Services.Themes.getThemeClass(geographyClass);
			}

			if (themeClass == null)
				themeClass = getDefaultClassForGeography(geography);

			String layerId = Strings.EMPTY;
			if (parameters.size() == 3)
				layerId = parameters.get(2).coerceToString();

			return Value.newString(drawGeography(geography, layerId, themeClass, mapView));
		} else if (METHOD_CLEAR.equalsIgnoreCase(name) && parameters.size() == 1) {
			String geographyId = parameters.get(0).coerceToString();
			removeGeography(geographyId);
		} else if (METHOD_CLEAR.equalsIgnoreCase(name) && parameters.size() == 0) {
			mapView.clearLayers();
		}

		return null;
	}

	private Value runRuntimeMethods(String name, List<Value> parameters) {
		final IGxMapViewRuntimeMethods mapView = Cast.as(IGxMapViewRuntimeMethods.class, mView);
		if (mapView == null) // Maps must implement the interface to support these methods.
			return null;

		if (METHOD_SET_MAPCENTER.equalsIgnoreCase(name) && (parameters.size() == 1 || parameters.size() == 2)) {
			String geoPoint = parameters.get(0).coerceToString();
			int zoomlevel = 0;
			if (parameters.size() == 2)
				zoomlevel = parameters.get(1).coerceToInt();

			// parse point as GeoPoint.
			Pair<Double, Double> pointPair = GeoFormats.parseGeopoint(geoPoint);
			if (pointPair == null) {
				// try parse as old format GeoLocation.
				pointPair = GeoFormats.parseGeolocation(geoPoint);
			}

			// set map center
			if (pointPair != null)
				mapView.setMapCenter(Services.Maps.newMapLocation(pointPair.first, pointPair.second), zoomlevel);

		} else if (METHOD_SET_ZOOMLEVEL.equalsIgnoreCase(name) && (parameters.size() == 1)) {
			int zoomLevel = parameters.get(0).coerceToInt();
			mapView.setZoomLevel(zoomLevel);
		} else if (GridHelper.METHOD_SELECT.equalsIgnoreCase(name) && (parameters.size() == 1)) {
			int index = parameters.get(0).coerceToInt();
			mapView.selectIndex(index - 1);
		} else if (GridHelper.METHOD_DESELECT.equalsIgnoreCase(name) && (parameters.size() == 1)) {
			int index = parameters.get(0).coerceToInt();
			mapView.deselectIndex(index - 1);
		} else if (METHOD_SAVE_EDITION.equalsIgnoreCase(name) && parameters.size() == 0) {
			mapView.saveEdition();
		} else if (METHOD_VISIBLE_REGION.equalsIgnoreCase(name) && parameters.size() == 0) {
			return Value.newSdt(mapView.getVisibleRegion());
		}

		return null;
	}

	private String drawGeography(String geography, String layerId, ThemeClassDefinition themeClass, IGxMapViewSupportLayers mapView) {
		MapLayer.FeatureType featureType = GeoFormats.guessFeatureType(geography);

		if (featureType == null)
			return Strings.EMPTY;

		MapLayer.MapFeature feature = null;
		switch (featureType) {
			case Point:
				feature = createGeopoint(geography);
				break;
			case Polyline:
				feature = createPolyline(geography, themeClass);
				break;
			case Polygon:
				feature = createPolygon(geography, themeClass);
				break;
		}

		if (feature == null)
			return Strings.EMPTY;

		mapView.updateLayer(layerId, feature);
		return feature.id;
	}

	private void removeGeography(String geographyId) {
		mGeographiesManager.removeFeature(geographyId);
	}

	private MapLayer.Point createGeopoint(String geopoint) {
		MapLayer.Point point = new MapLayer.Point();
		point.coordinates.add(newMapLocation(geopoint));
		return point;
	}

	private MapLayer.Polyline createPolyline(String geoline, ThemeClassDefinition lineClass) {
		MapLayer.Polyline polyline = new MapLayer.Polyline();
		polyline.points.addAll(newMapLocationList(geoline));
		ThemeUtils.applyMapFeatureClass(polyline, lineClass);
		return polyline;
	}

	private MapLayer.Polygon createPolygon(String geopolygon, ThemeClassDefinition polygonClass) {
		MapLayer.Polygon polygon = new MapLayer.Polygon();
		polygon.outerBoundary.addAll(newMapLocationList(geopolygon));
		ThemeUtils.applyMapFeatureClass(polygon, polygonClass);
		return polygon;
	}

	private int drawKMLLayer(IGxMapViewSupportLayers mapView, String layerId, String kml,
	                         boolean allowSelection, boolean isFile) {

		MapLayer layer = getMapLayers().get(layerId);
		if (layer != null)
			return -1;

		KmlReaderTask kmlReaderTask = new KmlReaderTask(Services.Maps.getProvider(), kml, isFile) {
			@Override
			public void onPostExecute(MapLayer result) {
				if (result != null) {
					result.id = layerId;
					mapView.addLayer(result);
				}
			}
		};

		TaskRunner.execute(kmlReaderTask);
		return 0;
	}

	private boolean getVisibleForLayer(String layerId) {
		if (layerId.trim().isEmpty())
			return true;

		MapLayer layer = getMapLayers().get(layerId);
		if (layer == null)
			return true;

		return layer.visible;
	}

	private ThemeClassDefinition getDefaultClassForGeography(String geography) {
		if (geography.startsWith("POINT"))
			return mDefinition.getGeographyClass(MapLayer.FeatureType.Point);
		else if (geography.startsWith("LINESTRING"))
			return mDefinition.getGeographyClass(MapLayer.FeatureType.Polyline);
		else if (geography.startsWith("POLYGON"))
			return mDefinition.getGeographyClass(MapLayer.FeatureType.Polygon);

		return null;
	}

	@NonNull @SuppressWarnings("unchecked")
	private NameMap<MapLayer> getMapLayers() {
		if (mGeographiesManager != null)
			return (NameMap<MapLayer>) mGeographiesManager.getMapLayers();

		Services.Log.warning("Geographies manager is not set in MapViewWrapper, returning empty layers collection");
		return new NameMap<>();
	}

	@Override
	public String getControlId() {
		return mDefinition.getGrid().getName();
	}

	@Override
	public void saveState(Map<String, Object> state) {
		if (mView instanceof IGxMapView) {
			String mapType = ((IGxMapView) mView).getMapType();
			state.put(PROPERTY_MAP_TYPE, mapType);
		}
	}

	@Override
	public void restoreState(Map<String, Object> state) {
		if (mView instanceof IGxMapView) {
			String mapType = (String) state.get(PROPERTY_MAP_TYPE);
			if (Strings.hasValue(mapType))
				((IGxMapView) mView).setMapType(mapType);
		}
	}

	private List<IMapLocation> newMapLocationList(String geography) {
		ArrayList<IMapLocation> list = new ArrayList<>();
		List<Pair<Double, Double>> path = new ArrayList<>();

		if (geography.startsWith("LINESTRING"))
			path = GeoFormats.parseGeoline(geography);
		else if (geography.startsWith("POLYGON"))
			path = GeoFormats.parseGeoPolygon(geography);

		if (path != null) {
			for (Pair<Double, Double> coordinate : path)
				list.add(Services.Maps.newMapLocation(coordinate.first, coordinate.second));
		}
		return list;
	}

	private IMapLocation newMapLocation(String geopoint) {
		Pair<Double, Double> coordinates = GeoFormats.parseGeopoint(geopoint);
		if (coordinates != null)
			return Services.Maps.newMapLocation(coordinates.first, coordinates.second);

		return null;
	}
}
