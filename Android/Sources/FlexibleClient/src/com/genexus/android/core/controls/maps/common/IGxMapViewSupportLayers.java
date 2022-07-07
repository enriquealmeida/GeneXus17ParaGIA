package com.genexus.android.core.controls.maps.common;

public interface IGxMapViewSupportLayers extends IGxMapView {
	void addLayer(MapLayer layer);
	void removeLayer(MapLayer layer);
	void updateLayer(String layerId, MapLayer.MapFeature feature);
	void clearLayers();
	void setLayerVisible(MapLayer layer, boolean visible);
	void adjustBoundsToLayer(MapLayer layer);
	void setGeographiesManagerCreatedCallback(OnGeographiesManagerCreatedCallback callback);
}
