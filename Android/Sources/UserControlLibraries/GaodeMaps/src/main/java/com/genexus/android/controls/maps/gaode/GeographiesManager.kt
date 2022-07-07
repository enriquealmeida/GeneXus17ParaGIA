package com.genexus.android.controls.maps.gaode

import com.amap.api.maps.AMap
import com.amap.api.maps.MapView
import com.amap.api.maps.model.BitmapDescriptor
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.Polygon
import com.amap.api.maps.model.PolygonOptions
import com.amap.api.maps.model.Polyline
import com.amap.api.maps.model.PolylineOptions
import com.genexus.android.controls.maps.gaode.GaodeMapsHelper.applyClassToPolygonOptions
import com.genexus.android.controls.maps.gaode.GaodeMapsHelper.applyClassToPolylineOptions
import com.genexus.android.controls.maps.gaode.MapLocation.toLatLngList2
import com.genexus.android.controls.maps.gaode.MapLocation.toLatLngLists
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.controls.maps.GxMapViewDefinition
import com.genexus.android.core.controls.maps.common.IMapLocation
import com.genexus.android.core.controls.maps.common.MapItemBase
import com.genexus.android.core.controls.maps.common.MapLayer
import com.genexus.android.core.controls.maps.common.MapPinHelper
import com.genexus.android.maps.GeographiesManagerBase
import com.genexus.android.maps.IAnimationLayer

internal class GeographiesManager(
    private val map: AMap,
    mapView: MapView,
    definition: GxMapViewDefinition
) : GeographiesManagerBase<Marker, Polyline, Polygon, MarkerOptions, PolylineOptions, PolygonOptions,
    BitmapDescriptor, AMap.OnMarkerDragListener>(mapView, definition) {

    override fun setMarkerDragListener(dragListener: AMap.OnMarkerDragListener) {
        map.setOnMarkerDragListener(dragListener)
    }

    override fun addMarker(id: String, latitude: Double, longitude: Double): Marker {
        return addMarker(id, MapLocation(latitude, longitude), false, null)
    }

    override fun addMarker(id: String, mapLocation: IMapLocation, draggable: Boolean, iconData: MapPinHelper.ResourceOrBitmap?): Marker {
        val options = buildMarkerOptions(id, mapLocation, draggable, iconData)
        val marker = map.addMarker(options) as Marker
        if (!hasFeatureKey(marker))
            marker.`object` = id

        return marker
    }

    override fun addLine(
        id: String,
        points: List<IMapLocation>,
        strokeWidth: Float?,
        strokeColor: Int?,
        dashPattern: IntArray?,
        geodesic: Boolean
    ): Polyline {
        val options = buildLineOptions(id, points, strokeWidth, strokeColor, dashPattern, geodesic)
        val polyline = map.addPolyline(options) as Polyline
// 		TODO: Should set feature Id here
//      if (!hasFeatureKey(polyline))
//      	polyline.extraInfo = id

        return polyline
    }

    override fun addPolygon(
        id: String,
        outter: List<IMapLocation>,
        innerHoles: List<List<IMapLocation>>?,
        strokeColor: Int?,
        polygonColor: Int?,
        strokeWidth: Float?,
        dashPattern: IntArray?,
        geodesic: Boolean
    ): Polygon {
        val options = buildPolygonOptions(
            id,
            outter,
            innerHoles,
            strokeWidth,
            strokeColor,
            polygonColor,
            dashPattern,
            geodesic
        )
        val polygon = map.addPolygon(options) as Polygon
// 		TODO: Should set feature Id here
//      if (!hasFeatureKey(polygon))
//      	polygon.extraInfo = id

        return polygon
    }

    override fun removeMarker(marker: Marker) {
        marker.remove()
    }

    override fun removeLine(line: Polyline) {
        line.remove()
    }

    override fun removePolygon(polygon: Polygon) {
        polygon.remove()
    }

    override fun setMarkerVisible(point: MapLayer.Point, visible: Boolean) {
        (point.mapObject as Marker).isVisible = visible
    }

    override fun setLineVisible(line: MapLayer.Polyline, visible: Boolean) {
        (line.mapObject as Polyline).isVisible = visible
    }

    override fun setPolygonVisible(polygon: MapLayer.Polygon, visible: Boolean) {
        (polygon.mapObject as Polygon).isVisible = visible
    }

    override fun buildMarkerOptions(id: String, mapLocation: IMapLocation, draggable: Boolean, iconData: MapPinHelper.ResourceOrBitmap?): MarkerOptions {
        val markerOptions = MarkerOptions()
// 			TODO: Should set feature Id here
// 				.extraInfo(id)
            .draggable(draggable)
            .position(LatLng(mapLocation.latitude, mapLocation.longitude))

        iconData?.let { markerOptions.icon(getMarkerImage(iconData)) }
        return markerOptions
    }

    override fun buildLineOptions(
        id: String,
        locationList: List<IMapLocation>,
        strokeWidth: Float?,
        strokeColor: Int?,
        dashPattern: IntArray?,
        geodesic: Boolean
    ): PolylineOptions {

        val latLngs: MutableList<LatLng> = ArrayList()
        for (coordinates in locationList)
            latLngs.add(LatLng(coordinates.latitude, coordinates.longitude))

        val lineOptions = PolylineOptions()
            .addAll(latLngs)
// 			TODO: Should set feature Id here
//            .extraInfo(id)

        if (strokeColor == null && strokeWidth == null && defaultLineClass != null) {
            applyClassToPolylineOptions(defaultLineClass, lineOptions)
        } else {
            strokeColor?.let { lineOptions.color(it) }
            strokeWidth?.let { lineOptions.width(it) }
        }
        return lineOptions
    }

    override fun buildPolygonOptions(
        id: String,
        outter: List<IMapLocation>,
        innerHoles: List<List<IMapLocation>>?,
        strokeWidth: Float?,
        strokeColor: Int?,
        polygonColor: Int?,
        dashPattern: IntArray?,
        geodesic: Boolean
    ): PolygonOptions {
        val polygonOptions = PolygonOptions()
// 			TODO: Should set feature Id here
//            .extraInfo(buildBundle(id))

        val polygons = innerHoles?.let { toLatLngLists(it) } ?: ArrayList()
        val latLngs: MutableList<LatLng> = ArrayList()
        for (coordinates in outter)
            latLngs.add(LatLng(coordinates.latitude, coordinates.longitude))

        polygons.add(0, latLngs)
        if (polygons.size >= 0)
            polygonOptions.addAll(polygons[0])

        if (strokeColor == null && polygonColor == null && strokeWidth == null && defaultPolygonClass != null) {
            applyClassToPolygonOptions(defaultPolygonClass, polygonOptions)
        } else {
            polygonColor?.let { polygonOptions.fillColor(it) }
            strokeColor?.let { polygonOptions.strokeColor(it) }
            strokeWidth?.let { polygonOptions.strokeWidth(it) }
        }

        return polygonOptions
    }

    override fun updateMarker(
        marker: Marker,
        latitude: Double,
        longitude: Double,
        rotation: Float?,
        flat: Boolean?,
        iconData: MapPinHelper.ResourceOrBitmap?,
        anchor: Pair<Float, Float>?
    ) {
        marker.position = LatLng(latitude, longitude)
//        rotation?.let { marker.rotation = it }
//        flat?.let { marker.isFlat = flat }
        iconData?.let { marker.setIcon(getMarkerImage(it)) }
        anchor?.let { marker.setAnchor(it.first, it.second) }
    }

    override fun updateMarker(marker: Marker, location: IMapLocation) {
        marker.position = LatLng(location.latitude, location.longitude)
    }

    override fun updateLine(line: Polyline, points: List<IMapLocation>) {
        line.points = toLatLngList2(points)
    }

    override fun updatePolygon(polygon: Polygon, points: List<IMapLocation>) {
        polygon.points = toLatLngList2(points)
    }

    override fun getMarkerPosition(marker: Marker): IMapLocation {
        return MapLocation(marker.position.latitude, marker.position.longitude)
    }

    @Throws(FeatureTypeNotRecognizedException::class)
    override fun getFeatureKey(mapObject: Any?): String? {
        val key = when (mapObject) {
            is Marker -> mapObject.`object` as String?
            is Polyline -> mapObject.id // TODO: Should be retrieved from another field
            is Polygon -> mapObject.id // TODO: Should be retrieved from another field
            else -> throw FeatureTypeNotRecognizedException(mapObject?.javaClass.toString())
        }

        if (!key.isNullOrEmpty()) Services.Log.debug(String.format("Feature key '%s' found", key))
        return key
    }

    override fun getAnimationLayer(): IAnimationLayer<IMapLocation, MapItemBase<IMapLocation>, Marker>? {
        return null
    }

    override fun extractMarkerId(marker: Marker): String? {
        return marker.`object` as String?
    }

    override fun extractMarkerRotation(marker: Marker): Double {
        return 0.toDouble()
    }

    override fun getMarkerImage(pin: MapPinHelper.ResourceOrBitmap?): BitmapDescriptor? {
        return when {
            pin == null -> BitmapDescriptorFactory.defaultMarker()
            pin.resourceId != null && pin.resourceId != 0 -> BitmapDescriptorFactory.fromResource(pin.resourceId)
            pin.bitmap != null -> BitmapDescriptorFactory.fromBitmap(pin.bitmap)
            else -> BitmapDescriptorFactory.defaultMarker()
        }
    }

    init {
        map.setOnMarkerClickListener { marker: Marker -> clickedListener!!.geographyClicked(getFeature(getFeatureKey(marker))) }
//        map.setOnPolylineClickListener { line: Polyline -> geographyClickedListener!!.geographyClicked(getFeature(getFeatureKey(line))) }
//        map.setOnPolygonClickListener { polygon: Polygon -> geographyClickedListener!!.geographyClicked(getFeature(getFeatureKey(polygon))) }
    }
}
