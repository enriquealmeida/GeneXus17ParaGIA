package com.genexus.android.controls.maps.baidu

import android.graphics.Color
import android.os.Bundle
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.BitmapDescriptor
import com.baidu.mapapi.map.BitmapDescriptorFactory
import com.baidu.mapapi.map.MapView
import com.baidu.mapapi.map.Marker
import com.baidu.mapapi.map.MarkerOptions
import com.baidu.mapapi.map.Polygon
import com.baidu.mapapi.map.PolygonOptions
import com.baidu.mapapi.map.Polyline
import com.baidu.mapapi.map.PolylineOptions
import com.baidu.mapapi.map.Stroke
import com.baidu.mapapi.model.LatLng
import com.genexus.android.controls.maps.baidu.BaiduMapsHelper.applyClassToPolygonOptions
import com.genexus.android.controls.maps.baidu.BaiduMapsHelper.applyClassToPolylineOptions
import com.genexus.android.controls.maps.baidu.MapLocation.listToLatLng
import com.genexus.android.controls.maps.baidu.MapLocation.toLatLngLists
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.controls.maps.GxMapViewDefinition
import com.genexus.android.core.controls.maps.common.IMapLocation
import com.genexus.android.core.controls.maps.common.MapItemBase
import com.genexus.android.core.controls.maps.common.MapLayer
import com.genexus.android.core.controls.maps.common.MapPinHelper
import com.genexus.android.maps.GeographiesManagerBase
import com.genexus.android.maps.IAnimationLayer

internal class GeographiesManager(
    private val map: BaiduMap,
    mapView: MapView,
    definition: GxMapViewDefinition
) : GeographiesManagerBase<Marker, Polyline, Polygon, MarkerOptions, PolylineOptions, PolygonOptions,
    BitmapDescriptor, BaiduMap.OnMarkerDragListener>(mapView, definition) {

    override fun setMarkerDragListener(dragListener: BaiduMap.OnMarkerDragListener) {
        map.setOnMarkerDragListener(dragListener)
    }

    override fun addMarker(id: String, latitude: Double, longitude: Double): Marker {
        return addMarker(id, MapLocation(latitude, longitude), false, null)
    }

    override fun addMarker(id: String, mapLocation: IMapLocation, draggable: Boolean, iconData: MapPinHelper.ResourceOrBitmap?): Marker {
        val options = buildMarkerOptions(id, mapLocation, draggable, iconData)
        val marker = map.addOverlay(options) as Marker
        if (!hasFeatureKey(marker))
            marker.extraInfo = buildBundle(id)

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
        val polyline = map.addOverlay(options) as Polyline
        if (!hasFeatureKey(polyline))
            polyline.extraInfo = buildBundle(id)

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
        val polygon = map.addOverlay(options) as Polygon
        if (!hasFeatureKey(polygon))
            polygon.extraInfo = buildBundle(id)

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
            .extraInfo(buildBundle(id))
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
            .points(latLngs)
            .extraInfo(buildBundle(id))

        if (strokeColor == null && strokeWidth == null && defaultLineClass != null) {
            applyClassToPolylineOptions(defaultLineClass, lineOptions)
        } else {
            strokeColor?.let { lineOptions.color(it) }
            strokeWidth?.let { lineOptions.width(it.toInt()) }
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
            .extraInfo(buildBundle(id))

        val polygons = innerHoles?.let { toLatLngLists(it) } ?: ArrayList()
        val latLngs: MutableList<LatLng> = ArrayList()
        for (coordinates in outter)
            latLngs.add(LatLng(coordinates.latitude, coordinates.longitude))

        polygons.add(0, latLngs)
        if (polygons.size >= 0)
            polygonOptions.points(polygons[0])

        if (strokeColor == null && polygonColor == null && strokeWidth == null && defaultPolygonClass != null) {
            applyClassToPolygonOptions(defaultPolygonClass, polygonOptions)
        } else {
            var color = strokeColor
            var width = strokeWidth
            polygonColor?.let { polygonOptions.fillColor(it) }
            if (color == null)
                color = Color.BLACK
            if (width == null)
                width = 0f

            polygonOptions.stroke(Stroke(width.toInt(), color))
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
        rotation?.let { marker.rotate = it }
        flat?.let { marker.isFlat = flat }
        iconData?.let { marker.icon = getMarkerImage(it) }
        anchor?.let { marker.setAnchor(it.first, it.second) }
    }

    override fun updateMarker(marker: Marker, location: IMapLocation) {
        marker.position = LatLng(location.latitude, location.longitude)
    }

    override fun updateLine(line: Polyline, points: List<IMapLocation>) {
        line.points = listToLatLng(points)
    }

    override fun updatePolygon(polygon: Polygon, points: List<IMapLocation>) {
        polygon.points = listToLatLng(points)
    }

    override fun getMarkerPosition(marker: Marker): IMapLocation {
        return MapLocation(marker.position.latitude, marker.position.longitude)
    }

    @Throws(FeatureTypeNotRecognizedException::class)
    override fun getFeatureKey(mapObject: Any?): String? {
        val key = when (mapObject) {
            is Marker -> parseBundle(mapObject.extraInfo)
            is Polyline -> parseBundle(mapObject.extraInfo)
            is Polygon -> parseBundle(mapObject.extraInfo)
            else -> throw FeatureTypeNotRecognizedException(mapObject?.javaClass.toString())
        }

        if (!key.isNullOrEmpty()) Services.Log.debug(String.format("Feature key '%s' found", key))
        return key
    }

    override fun getAnimationLayer(): IAnimationLayer<IMapLocation, MapItemBase<IMapLocation>, Marker>? {
        return null
    }

    override fun extractMarkerId(marker: Marker): String? {
        return parseBundle(marker.extraInfo)
    }

    override fun extractMarkerRotation(marker: Marker): Double {
        return marker.rotate.toDouble()
    }

    override fun getMarkerImage(pin: MapPinHelper.ResourceOrBitmap?): BitmapDescriptor? {
        return when {
            pin == null -> BitmapDescriptorFactory.fromResource(R.drawable.red_markers)
            pin.resourceId != null && pin.resourceId != 0 -> BitmapDescriptorFactory.fromResource(pin.resourceId)
            pin.bitmap != null -> BitmapDescriptorFactory.fromBitmap(pin.bitmap)
            else -> BitmapDescriptorFactory.fromResource(R.drawable.red_markers)
        }
    }

    private fun buildBundle(id: String): Bundle {
        val bundle = Bundle()
        bundle.putString(MARKER_ID_KEY, id)
        return bundle
    }

    private fun parseBundle(bundle: Bundle): String? {
        return bundle.getString(MARKER_ID_KEY)
    }

    companion object {
        private const val MARKER_ID_KEY = "MARKER_KEY"
    }

    init {
        map.setOnMarkerClickListener { marker: Marker -> clickedListener!!.geographyClicked(getFeature(getFeatureKey(marker))) }
        map.setOnPolylineClickListener { line: Polyline -> clickedListener!!.geographyClicked(getFeature(getFeatureKey(line))) }
//        map.setOnPolygonClickListener { polygon: Polygon -> geographyClickedListener!!.geographyClicked(getFeature(getFeatureKey(polygon))) }
    }
}
