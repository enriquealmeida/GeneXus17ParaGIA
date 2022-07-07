package com.genexus.android.core.controls.maps.common;

import com.genexus.android.core.base.model.Entity;

public interface IGxMapViewRuntimeMethods extends IGxMapView
{
	// methods
	void setMapCenter(IMapLocation location, int zoomLevel);
	void setZoomLevel(int zoomLevel);

	Entity getVisibleRegion();

	void selectIndex(int index);
	void deselectIndex(int index);

	void saveEdition();
	Runnable getMyLocationRunnable();

	//properties
	int getSelectedIndex();
	String getEditableGeographies();
	boolean getSelectionLayer();
	void setDirectionsLayer(boolean directionsLayer);
	void setAnimationLayer(boolean useAnimationLayer);
	void setSelectionLayer(boolean useSelectionLayer);
	void setEditableGeographies(String mode);
}
