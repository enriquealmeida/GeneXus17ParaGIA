package com.genexus.android.core.controls.maps.common

import android.location.Location
import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.utils.NameMap
import com.genexus.android.core.controls.maps.FeatureInfo

interface IGeographiesManager<Marker, MarkerDragListener> {
    fun getMapLayers(): NameMap<MapLayer>
    fun getServerLayer(): MapLayer
    fun addFeature(featureInfo: FeatureInfo, closeFeature: Boolean, updateAnimation: Boolean): MapLayer.MapFeature?
    fun addFeature(layerId: String? = null, featureInfo: FeatureInfo, closeFeature: Boolean, updateAnimation: Boolean): MapLayer.MapFeature?
    fun buildFeatureInfoFromFeature(feature: MapLayer.MapFeature, data: MapItemBase<*>?): FeatureInfo
    fun removeFeature(geographyId: String)
    fun removeFeature(layerId: String?, feature: MapLayer.MapFeature, updateCollection: Boolean)
    fun updateLayer(layerId: String?, feature: MapLayer.MapFeature): MapLayer.MapFeature?
    fun removeAll()
    fun setVisible(layer: MapLayer, visible: Boolean)
    fun updateDrawnFeatureWithNewPoint(type: MapLayer.FeatureType?, position: IMapLocation, id: String): MapLayer.MapFeature?
    fun saveEdition(): MapLayer.MapFeature?
    fun addOrUpdateLocationMarker(location: Location, iconResourceId: Int?)
    fun getPinImage(data: Entity): MapPinHelper.ResourceOrBitmap

    fun markerDrawnInEditMode(marker: Marker): Boolean
    fun handleMarkerDragged(marker: Marker): MapLayer.MapFeature?

    fun getFeatureKey(mapObject: Any?): String?
    fun getMarkerPosition(marker: Marker): IMapLocation
    fun extractMarkerId(marker: Marker): String?
    fun extractMarkerRotation(marker: Marker): Double

    fun setMarkerDragListener(dragListener: MarkerDragListener)
    fun setGeographyClickedListener(clickListener: OnGeographyClickedCallback)
}
