package com.genexus.android.controls.maps.mapbox

import android.graphics.Color
import androidx.core.content.res.ResourcesCompat
import com.genexus.android.controls.maps.mapbox.MapLocation.Companion.listToLatLng
import com.genexus.android.controls.maps.mapbox.MapLocation.Companion.toLatLngLists
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.controls.maps.GxMapViewDefinition
import com.genexus.android.core.controls.maps.common.IMapLocation
import com.genexus.android.core.controls.maps.common.MapItemBase
import com.genexus.android.core.controls.maps.common.MapLayer
import com.genexus.android.core.controls.maps.common.MapPinHelper
import com.genexus.android.core.utils.ThemeUtils
import com.genexus.android.maps.GeographiesManagerBase
import com.genexus.android.maps.IAnimationLayer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.Fill
import com.mapbox.mapboxsdk.plugins.annotation.FillManager
import com.mapbox.mapboxsdk.plugins.annotation.FillOptions
import com.mapbox.mapboxsdk.plugins.annotation.Line
import com.mapbox.mapboxsdk.plugins.annotation.LineManager
import com.mapbox.mapboxsdk.plugins.annotation.LineOptions
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolDragListener
import com.mapbox.mapboxsdk.plugins.annotation.Symbol
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.utils.BitmapUtils

@Suppress("UNCHECKED_CAST")
internal class GeographiesManager(
    mapboxMap: MapboxMap?,
    private val mapView: MapView?,
    definition: GxMapViewDefinition
) : GeographiesManagerBase<Symbol, Line, Fill, SymbolOptions, LineOptions, FillOptions, String,
    OnSymbolDragListener>(mapView!!, definition) {

    private val symbolManager: SymbolManager
    private val lineManager: LineManager
    private val fillManager: FillManager
    private val style: Style

    override fun setMarkerDragListener(dragListener: OnSymbolDragListener) {
        symbolManager.addDragListener(dragListener)
    }

    override fun addMarker(id: String, latitude: Double, longitude: Double): Symbol {
        return addMarker(id, MapLocation(latitude, longitude), false, null)
    }

    override fun addMarker(
        id: String,
        mapLocation: IMapLocation,
        draggable: Boolean,
        iconData: MapPinHelper.ResourceOrBitmap?
    ): Symbol {
        val options = buildMarkerOptions(id, mapLocation, draggable, iconData)
        return symbolManager.create(options)
    }

    override fun addLine(
        id: String,
        points: List<IMapLocation>,
        strokeWidth: Float?,
        strokeColor: Int?,
        dashPattern: IntArray?,
        geodesic: Boolean
    ): Line {
        val options = buildLineOptions(id, points, strokeWidth, strokeColor, dashPattern, false)
        return lineManager.create(options)
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
    ): Fill {
        val options = buildPolygonOptions(
            id,
            outter,
            innerHoles,
            strokeWidth,
            strokeColor,
            polygonColor,
            dashPattern,
            false
        )
        return fillManager.create(options)
    }

    override fun removeMarker(marker: Symbol) {
        symbolManager.delete(marker)
    }

    override fun removeLine(line: Line) {
        lineManager.delete(line)
    }

    override fun removePolygon(polygon: Fill) {
        fillManager.delete(polygon)
    }

    override fun setMarkerVisible(point: MapLayer.Point, visible: Boolean) {
        if (visible)
            point.mapObject = addMarker(point.id, point.points[0], false, pinHelper.getPinImage(point.itemData?.data))
        else
            removeMarker(point.mapObject as Symbol)
    }

    override fun setLineVisible(line: MapLayer.Polyline, visible: Boolean) {
        if (visible)
            line.mapObject = addLine(line.id, line.points, line.strokeWidth, line.strokeColor, line.dashPattern, false)
        else
            removeLine(line.mapObject as Line)
    }

    override fun setPolygonVisible(polygon: MapLayer.Polygon, visible: Boolean) {
        if (visible)
            polygon.mapObject = addPolygon(
                polygon.id, polygon.points as List<MapLocation>, null,
                polygon.strokeColor, polygon.fillColor, polygon.strokeWidth, polygon.dashPattern, false
            )
        else
            removePolygon(polygon.mapObject as Fill)
    }

    override fun buildMarkerOptions(id: String, mapLocation: IMapLocation, draggable: Boolean, iconData: MapPinHelper.ResourceOrBitmap?): SymbolOptions {
        val markerOptions = SymbolOptions().withDraggable(draggable)
            .withLatLng(LatLng(mapLocation.latitude, mapLocation.longitude))
            .withIconImage(getMarkerImage(iconData))
            .withDraggable(draggable)

        if (id.isNotEmpty())
            markerOptions.withData(getJsonElement(id))

        return markerOptions
    }

    override fun buildLineOptions(
        id: String,
        locationList: List<IMapLocation>,
        strokeWidth: Float?,
        strokeColor: Int?,
        dashPattern: IntArray?,
        geodesic: Boolean
    ): LineOptions {
        val latLngs: MutableList<LatLng> = ArrayList()
        for (coordinates in locationList) latLngs.add(LatLng(coordinates.latitude, coordinates.longitude))
        val lineOptions = LineOptions().withLatLngs(latLngs)
        if (id.isNotEmpty()) lineOptions.withData(getJsonElement(id))
        if (strokeColor == null && strokeWidth == null && defaultLineClass != null) {
            applyClassToPolylineOptions(defaultLineClass, lineOptions)
        } else {
            strokeColor?.let { lineOptions.withLineColor(PropertyFactory.lineColor(it).getValue()) }
            strokeWidth?.let { lineOptions.withLineWidth(PropertyFactory.lineWidth(it).getValue()) }
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
    ): FillOptions {
        val polygons = innerHoles?.let { toLatLngLists(it) } ?: ArrayList()
        val latLngs: MutableList<LatLng> = ArrayList()
        for (coordinates in outter) latLngs.add(LatLng(coordinates.latitude, coordinates.longitude))
        polygons.add(0, latLngs)
        val fillOptions = FillOptions().withLatLngs(polygons)
        if (id.isNotEmpty()) fillOptions.withData(getJsonElement(id))
        if (strokeColor == null && polygonColor == null && defaultPolygonClass != null) {
            applyClassToPolygonOptions(defaultPolygonClass, fillOptions)
        } else {
            fillOptions.withFillColor(polygonColor?.let { PropertyFactory.fillColor(it).getValue() })
            fillOptions.withFillOutlineColor(strokeColor?.let { PropertyFactory.fillOutlineColor(it).getValue() })
        }
        return fillOptions
    }

    override fun updateMarker(marker: Symbol, latitude: Double, longitude: Double, rotation: Float?, flat: Boolean?, iconData: MapPinHelper.ResourceOrBitmap?, anchor: Pair<Float, Float>?) {
        marker.latLng = LatLng(latitude, longitude)
        rotation?.let { marker.iconRotate = it }
        iconData?.let { marker.iconImage = getMarkerImage(iconData) }
//        anchor?.let { markerImpl.iconAnchor = it.first, it.second }
    }

    override fun updateMarker(marker: Symbol, location: IMapLocation) {
        marker.latLng = LatLng(location.latitude, location.longitude)
        symbolManager.update(marker)
    }

    override fun updateLine(line: Line, points: List<IMapLocation>) {
        line.latLngs = listToLatLng(points)
        lineManager.update(line)
    }

    override fun updatePolygon(polygon: Fill, points: List<IMapLocation>) {
        val lists = mutableListOf<List<LatLng>>()
        lists.add(listToLatLng(points))
        polygon.latLngs = lists
        fillManager.update(polygon)
    }

    override fun getMarkerPosition(marker: Symbol): IMapLocation {
        val latLng = marker.latLng
        return MapLocation(latLng.latitude, latLng.longitude)
    }

    @Throws(FeatureTypeNotRecognizedException::class)
    override fun getFeatureKey(mapObject: Any?): String {
        val element = when (mapObject) {
            is Symbol -> mapObject.data
            is Line -> mapObject.data
            is Fill -> mapObject.data
            else -> throw FeatureTypeNotRecognizedException(mapObject?.javaClass.toString())
        }

        val key = getKey(element)
        if (key.isNotEmpty()) Services.Log.debug(String.format("Feature key '%s' found", key))
        return key
    }

    override fun getAnimationLayer(): IAnimationLayer<IMapLocation, MapItemBase<IMapLocation>, Symbol>? {
        return null
    }

    override fun extractMarkerId(marker: Symbol): String {
        return getKey(marker.data)
    }

    override fun extractMarkerRotation(marker: Symbol): Double {
        return marker.iconRotate.toDouble()
    }

    override fun getMarkerImage(pin: MapPinHelper.ResourceOrBitmap?): String {
        return when {
            pin == null -> DEFAULT_MARKER_ICON
            pin.resourceId != null && pin.resourceId != 0 -> {
                val id = pin.resourceId.toString()
                if (style.getImage(id) != null) return id

                val resources = mapView?.context?.resources ?: return DEFAULT_MARKER_ICON
                BitmapUtils.getBitmapFromDrawable(
                    ResourcesCompat.getDrawable(resources, pin.resourceId, null)
                )?.let {
                    style.addImage(id, it)
                }

                id
            }
            pin.bitmap != null -> {
                val id = pin.bitmap.toString()
                if (style.getImage(id) != null) return id

                style.addImage(id, pin.bitmap)
                id
            }
            else -> DEFAULT_MARKER_ICON
        }
    }

    private fun setMarkerOptionsIdIfEmpty(id: String?, symbolOptions: SymbolOptions): SymbolOptions {
        val key = getKey(symbolOptions.data)
        return if (key.isEmpty()) symbolOptions.withData(getJsonElement(id)) else symbolOptions
    }

    private fun setLineOptionsIdIfEmpty(id: String?, lineOptions: LineOptions): LineOptions {
        val key = getKey(lineOptions.data)
        return if (key.isEmpty()) lineOptions.withData(getJsonElement(id)) else lineOptions
    }

    private fun setPolygonOptionsIdIfEmpty(id: String?, fillOptions: FillOptions): FillOptions {
        val key = getKey(fillOptions.data)
        return if (key.isEmpty()) fillOptions.withData(getJsonElement(id)) else fillOptions
    }

    private fun getKey(element: JsonElement?): String {
        return if (element == null || element is JsonNull) Strings.EMPTY else element.asJsonObject[MARKER_KEY].asString
    }

    private fun getJsonElement(id: String?): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty(MARKER_KEY, id)
        return jsonObject
    }

    private fun applyClassToPolylineOptions(themeClassDefinition: ThemeClassDefinition, polylineOptions: LineOptions) {
        val lineWidth = themeClassDefinition.lineWidth.toFloat()
        if (lineWidth > 0) polylineOptions.withLineWidth(PropertyFactory.lineWidth(lineWidth).getValue())
        polylineOptions.withLineColor(PropertyFactory.lineColor(ThemeUtils.getColorId(themeClassDefinition.strokeColor, Color.BLACK)).getValue())
    }

    private fun applyClassToPolygonOptions(themeClassDefinition: ThemeClassDefinition, polygonOptions: FillOptions) {
        // LineWidth is not supported by Mapbox yet
        polygonOptions.withFillOutlineColor(PropertyFactory.fillOutlineColor(ThemeUtils.getColorId(themeClassDefinition.strokeColor, Color.BLACK)).getValue())
        polygonOptions.withFillColor(PropertyFactory.fillColor(ThemeUtils.getColorId(themeClassDefinition.fillColor, Color.TRANSPARENT)).getValue())
    }

    companion object {
        private const val MARKER_KEY = "key"
        const val DEFAULT_MARKER_ICON = "DEFAULT_MARKER_ICON"
    }

    init {
        require(!(mapboxMap == null || mapView == null)) { "Mapbox map components cannot be null" }
        style = mapboxMap.style ?: throw IllegalArgumentException("MapboxMap style cannot be null")
        symbolManager = SymbolManager(mapView, mapboxMap, style)
        lineManager = LineManager(mapView, mapboxMap, style)
        fillManager = FillManager(mapView, mapboxMap, style)
        symbolManager.addClickListener { symbol: Symbol -> clickedListener!!.geographyClicked(getFeature(getKey(symbol.data))) }
        lineManager.addClickListener { line: Line -> clickedListener!!.geographyClicked(getFeature(getKey(line.data))) }
        fillManager.addClickListener { fill: Fill -> clickedListener!!.geographyClicked(getFeature(getKey(fill.data))) }
    }
}
