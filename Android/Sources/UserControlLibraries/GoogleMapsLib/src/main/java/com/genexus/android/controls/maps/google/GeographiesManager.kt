package com.genexus.android.controls.maps.google

import com.genexus.android.controls.maps.google.GoogleMapsHelper.applyClassToPolygonOptions
import com.genexus.android.controls.maps.google.GoogleMapsHelper.applyClassToPolylineOptions
import com.genexus.android.controls.maps.google.MapLocation.toLatLngList2
import com.genexus.android.controls.maps.google.MapLocation.toLatLngLists
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.controls.maps.GxMapViewDefinition
import com.genexus.android.core.controls.maps.common.IMapLocation
import com.genexus.android.core.controls.maps.common.MapItemBase
import com.genexus.android.core.controls.maps.common.MapLayer
import com.genexus.android.core.controls.maps.common.MapPinHelper
import com.genexus.android.maps.GeographiesManagerBase
import com.genexus.android.maps.IAnimationLayer
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PatternItem
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions

internal class GeographiesManager(
    private val googleMap: GoogleMap,
    mapView: MapView,
    definition: GxMapViewDefinition
) : GeographiesManagerBase<Marker, Polyline, Polygon, MarkerOptions, PolylineOptions, PolygonOptions,
    BitmapDescriptor, GoogleMap.OnMarkerDragListener>(mapView, definition) {

    override fun setMarkerDragListener(dragListener: GoogleMap.OnMarkerDragListener) {
        googleMap.setOnMarkerDragListener(dragListener)
    }

    override fun addMarker(id: String, latitude: Double, longitude: Double): Marker {
        return addMarker(id, MapLocation(latitude, longitude), false, null)
    }

    override fun addMarker(id: String, mapLocation: IMapLocation, draggable: Boolean, iconData: MapPinHelper.ResourceOrBitmap?): Marker {
        val options = buildMarkerOptions(id, mapLocation, draggable, iconData)
        val marker = googleMap.addMarker(options)
        if (!hasFeatureKey(marker))
            marker?.tag = id

        return marker!!
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
        val polyline = googleMap.addPolyline(options)
        if (!hasFeatureKey(polyline))
            polyline.tag = id

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
        val options = buildPolygonOptions(id, outter, innerHoles, strokeWidth, strokeColor, polygonColor, dashPattern, geodesic)
        val polygon = googleMap.addPolygon(options)
        if (!hasFeatureKey(polygon))
            polygon.tag = id

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

        val lineOptions = PolylineOptions().addAll(latLngs).geodesic(geodesic)

        if (strokeColor == null && strokeWidth == null && dashPattern == null && defaultLineClass != null) {
            applyClassToPolylineOptions(defaultLineClass, lineOptions)
        } else {
            strokeColor?.let { lineOptions.color(it) }
            strokeWidth?.let { lineOptions.width(it) }
            dashPattern?.let { lineOptions.pattern(buildPatternItemList(it)) }
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
        val polygons = innerHoles?.let { toLatLngLists(it) } ?: ArrayList()
        val latLngs: MutableList<LatLng> = ArrayList()
        for (coordinates in outter)
            latLngs.add(LatLng(coordinates.latitude, coordinates.longitude))

        polygons.add(0, latLngs)

        val polygonOptions = PolygonOptions()
        for ((index, _) in polygons.withIndex()) {
            if (index == 0)
                polygonOptions.addAll(polygons[index])
            else
                polygonOptions.addHole(polygons[index])
        }

        polygonOptions.geodesic(geodesic)

        if (strokeColor == null && polygonColor == null && strokeWidth == null && dashPattern == null && defaultPolygonClass != null) {
            applyClassToPolygonOptions(defaultPolygonClass, polygonOptions)
        } else {
            polygonColor?.let { polygonOptions.fillColor(it) }
            strokeColor?.let { polygonOptions.strokeColor(it) }
            strokeWidth?.let { polygonOptions.strokeWidth(it) }
            dashPattern?.let { polygonOptions.strokePattern(buildPatternItemList(it)) }
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
        rotation?.let { marker.rotation = it }
        flat?.let { marker.isFlat = flat }
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
            is Marker -> mapObject.tag as String?
            is Polyline -> mapObject.tag as String?
            is Polygon -> mapObject.tag as String?
            else -> throw FeatureTypeNotRecognizedException(mapObject?.javaClass.toString())
        }

        if (!key.isNullOrEmpty()) Services.Log.debug(String.format("Feature key '%s' found", key))
        return key
    }

    @Suppress("UNCHECKED_CAST")
    override fun getAnimationLayer(): IAnimationLayer<IMapLocation, MapItemBase<IMapLocation>, Marker> {
        return AnimationLayer(definition) as IAnimationLayer<IMapLocation, MapItemBase<IMapLocation>, Marker>
    }

    override fun extractMarkerId(marker: Marker): String? {
        return marker.tag as String?
    }

    override fun extractMarkerRotation(marker: Marker): Double {
        return marker.rotation.toDouble()
    }

    override fun getMarkerImage(pin: MapPinHelper.ResourceOrBitmap?): BitmapDescriptor? {
        return when {
            pin == null -> BitmapDescriptorFactory.defaultMarker()
            pin.resourceId != null && pin.resourceId != 0 -> BitmapDescriptorFactory.fromResource(pin.resourceId)
            pin.bitmap != null -> BitmapDescriptorFactory.fromBitmap(pin.bitmap)
            else -> BitmapDescriptorFactory.defaultMarker()
        }
    }

    private fun buildPatternItemList(dashPattern: IntArray): List<PatternItem> {
        val patternItemList = mutableListOf<PatternItem>()
        dashPattern.forEachIndexed { index, element ->
            val value = element.toFloat().times(5)
            if (index % 2 == 0)
                if (value == 5f) // Means that actual pattern value was "1"
                    patternItemList.add(Dot())
                else
                    patternItemList.add(Dash(value))
            else
                patternItemList.add(Gap(value))
        }
        return patternItemList
    }

    init {
        googleMap.setOnMarkerClickListener { marker: Marker -> clickedListener!!.geographyClicked(getFeature(getFeatureKey(marker))) }
        googleMap.setOnPolylineClickListener { line: Polyline -> clickedListener!!.geographyClicked(getFeature(getFeatureKey(line))) }
        googleMap.setOnPolygonClickListener { polygon: Polygon -> clickedListener!!.geographyClicked(getFeature(getFeatureKey(polygon))) }
    }
}
