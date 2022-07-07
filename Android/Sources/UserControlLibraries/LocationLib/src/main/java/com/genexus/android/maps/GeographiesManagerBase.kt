package com.genexus.android.maps

import android.location.Location
import android.os.Handler
import android.os.SystemClock
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition
import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.NameMap
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.controls.maps.FeatureInfo
import com.genexus.android.core.controls.maps.GxMapViewDefinition
import com.genexus.android.core.controls.maps.common.IGeographiesManager
import com.genexus.android.core.controls.maps.common.IMapLocation
import com.genexus.android.core.controls.maps.common.MapItemBase
import com.genexus.android.core.controls.maps.common.MapLayer
import com.genexus.android.core.controls.maps.common.MapLayer.MapFeature
import com.genexus.android.core.controls.maps.common.MapPinHelper
import com.genexus.android.core.controls.maps.common.OnGeographyClickedCallback

@Suppress("UNCHECKED_CAST")
abstract class GeographiesManagerBase<Marker, Line, Polygon, MarkerOptions, LineOptions, PolygonOptions, IconType, MarkDragListener>(
    mapView: View,
    val definition: GxMapViewDefinition
) : IGeographiesManager<Marker, MarkDragListener> {

    val pinHelper = MapPinHelper(mapView.context, definition)
    val defaultIconResource = if (definition.pinImageResourceId == 0) null else MapPinHelper.ResourceOrBitmap(definition.pinImageResourceId)
    val defaultPolygonClass: ThemeClassDefinition? = definition.getGeographyClass(MapLayer.FeatureType.Polygon)
    val defaultLineClass: ThemeClassDefinition? = definition.getGeographyClass(MapLayer.FeatureType.Polyline)
    private val animationsLayer by lazy { getAnimationLayer() }
    private val editableGeographies: HashMap<MapFeature, MutableList<Marker>> = HashMap()
    private val mapLayers: NameMap<MapLayer> = NameMap()
    private val serverLayersNames = mutableListOf<String>()
    private var locationMarker: Marker? = null
    private val knownLayerIds = listOf(LAYER_ID_EMPTY, LAYER_ID_DIRECTIONS, LAYER_ID_SERVER, LAYER_ID_ANIMATION)
    protected var clickedListener: OnGeographyClickedCallback? = null

    override fun setGeographyClickedListener(clickListener: OnGeographyClickedCallback) {
        clickedListener = clickListener
    }

    // A copy of the Map is returned so changes on it don't impact on the actual collection.
    // Also, the EMPTY and DIRECTIONS layers shouldn't be handled by the user so they aren't added to the return value
    override fun getMapLayers(): NameMap<MapLayer> {
        val layers = NameMap<MapLayer>()
        for (entry in mapLayers)
            if (!knownLayerIds.contains(entry.key))
                layers[entry.key] = entry.value

        return layers
    }

    override fun getServerLayer(): MapLayer {
        val layer = MapLayer()
        for (serverLayerName in serverLayersNames)
            mapLayers[serverLayerName]?.let { layer.features.addAll(it.features) }

        return layer
    }

    override fun addFeature(featureInfo: FeatureInfo, closeFeature: Boolean, updateAnimation: Boolean): MapFeature? {
        return addFeature(null, featureInfo, closeFeature, updateAnimation)
    }

    @Synchronized
    @Throws(FeatureTypeNotRecognizedException::class)
    override fun addFeature(layerId: String?, featureInfo: FeatureInfo, closeFeature: Boolean, updateAnimation: Boolean): MapFeature? {
        val dataLayerId = featureInfo.Data?.layerId
        val isServerLayer = !layerId.isNullOrEmpty() && !dataLayerId.isNullOrEmpty() && layerId == LAYER_ID_SERVER
        var finalLayerId = if (isServerLayer) dataLayerId else layerId
        if (isFeatureRegistered(finalLayerId, featureInfo)) {
            Services.Log.warning("Skipping ${featureInfo.Data?.data} because it is already registered")
            return null
        }

        val iconData = featureInfo.icon
        val featureId = featureInfo.id
        val feature: MapFeature
        when (featureInfo.type) {
            MapLayer.FeatureType.Point -> {
                feature = MapLayer.Point(featureId)
                val position = featureInfo.points[0]
                var animatedMarker: Marker? = null
                val points: MutableList<IMapLocation> = ArrayList()
                points.add(position)
                feature.setPoints(points)

                if (updateAnimation) {
                    animationsLayer?.let {
                        val animationMarkerKey = it.getAnimationMarkerKey(featureInfo.Data as MapItemBase<IMapLocation>)
                        if (Strings.hasValue(animationMarkerKey)) {
                            finalLayerId = LAYER_ID_ANIMATION
                            feature.id = animationMarkerKey
                            val oldMarker = getFeature(animationMarkerKey)?.mapObject
                            animatedMarker = oldMarker as Marker?
                                ?: addMarker(animationMarkerKey!!, position, false, iconData)

                            if (oldMarker != null && animatedMarker != null) {
                                it.animateMarkers(featureInfo.Data as MapItemBase<IMapLocation>, position, animatedMarker!!)
                                return null
                            }
                        }
                    }
                }

                feature.mapObject = animatedMarker ?: addMarker(feature.id, position, featureInfo.draggable, iconData)
            }
            MapLayer.FeatureType.Polyline -> {
                feature = MapLayer.Polyline(featureId).apply {
                    points = featureInfo.points
                    strokeColor = featureInfo.strokeColor
                    strokeWidth = featureInfo.lineWidth
                    dashPattern = featureInfo.dashPattern
                }
                feature.mapObject = addLine(
                    feature.id, featureInfo.points, featureInfo.lineWidth,
                    featureInfo.strokeColor, featureInfo.dashPattern, false
                )
            }
            MapLayer.FeatureType.Polygon -> {
                feature = MapLayer.Polygon(featureId).apply {
                    points = featureInfo.points
                    strokeColor = featureInfo.strokeColor
                    strokeWidth = featureInfo.lineWidth
                    fillColor = featureInfo.fillColor
                    dashPattern = featureInfo.dashPattern
                }
                feature.mapObject = addPolygon(
                    feature.id, featureInfo.points, null, featureInfo.strokeColor,
                    featureInfo.fillColor, featureInfo.lineWidth, featureInfo.dashPattern, false
                )
            }
            else -> throw FeatureTypeNotRecognizedException(featureInfo.type?.toString())
        }

        feature.itemData = featureInfo.Data
        feature.isClosed = closeFeature
        registerFeatureInLayer(finalLayerId, isServerLayer, feature)
        return feature
    }

    @Throws(FeatureTypeNotRecognizedException::class)
    override fun buildFeatureInfoFromFeature(feature: MapFeature, data: MapItemBase<*>?): FeatureInfo {
        val featureInfo = FeatureInfo(data)
        featureInfo.points = feature.points
        featureInfo.id = feature.id
        featureInfo.type = feature.type
        when (feature.type) {
            MapLayer.FeatureType.Point -> {
            }
            MapLayer.FeatureType.Polyline -> {
                val polyline = feature as MapLayer.Polyline
                featureInfo.lineWidth = polyline.strokeWidth
                featureInfo.strokeColor = polyline.strokeColor
                featureInfo.geodesic = true
            }
            MapLayer.FeatureType.Polygon -> {
                val polygon = feature as MapLayer.Polygon
                val holes = mutableListOf<List<IMapLocation>>()
                for (hole in polygon.holes)
                    holes.add(hole)

                featureInfo.holes = holes
                featureInfo.lineWidth = polygon.strokeWidth
                featureInfo.strokeColor = polygon.strokeColor
                featureInfo.fillColor = polygon.fillColor
                featureInfo.geodesic = true
            }
            else -> throw FeatureTypeNotRecognizedException(featureInfo.type?.toString())
        }

        return featureInfo
    }

    private fun registerFeatureInLayer(layerId: String?, isServerLayer: Boolean, feature: MapFeature) {
        var finalLayerId = layerId
        if (finalLayerId.isNullOrEmpty())
            finalLayerId = LAYER_ID_EMPTY

        if (isServerLayer && !layerId.isNullOrEmpty() && !serverLayersNames.contains(layerId))
            serverLayersNames.add(layerId)

        val layer = mapLayers[finalLayerId]
        if (layer != null) {
            layer.features.add(feature)
            return
        }

        val newLayer = MapLayer(finalLayerId)
        newLayer.features.add(feature)
        mapLayers[finalLayerId] = newLayer
    }

    private fun isFeatureRegistered(layerId: String?, featureInfo: FeatureInfo): Boolean {
        if (layerId.isNullOrEmpty())
            return false

        val newData = featureInfo.Data?.data?.toString()
        val layer = mapLayers[layerId] ?: return false
        for (feature in layer.features) {
            val oldData = feature.itemData?.data?.toString()
            if (oldData != null && oldData == newData)
                return true
        }

        return false
    }

    override fun removeFeature(geographyId: String) {
        val layerFeaturePair = getLayerBelongingTo(geographyId)
        if (layerFeaturePair != null)
            removeFeature(layerFeaturePair.first.id, layerFeaturePair.second, true)
    }

    @Throws(FeatureTypeNotRecognizedException::class)
    override fun removeFeature(layerId: String?, feature: MapFeature, updateCollection: Boolean) {
        if (feature.mapObject == null) {
            Services.Log.error("Feature mapObject is null")
            return
        }

        when (feature.type) {
            MapLayer.FeatureType.Polygon -> removePolygon(feature.mapObject as Polygon)
            MapLayer.FeatureType.Polyline -> removeLine(feature.mapObject as Line)
            MapLayer.FeatureType.Point -> removeMarker(feature.mapObject as Marker)
            else -> throw FeatureTypeNotRecognizedException(feature.type?.toString())
        }

        if (updateCollection) {
            val layer = mapLayers[layerId]
            layer?.let {
                for (layerFeature in it.features) {
                    var key = getFeatureKey(feature.mapObject)
                    if (!Strings.hasValue(key))
                        key = feature.id

                    if (layerFeature.id.equals(key)) {
                        it.features.remove(layerFeature)
                        break
                    }
                }
            }
        }
    }

    override fun updateLayer(layerId: String?, feature: MapFeature): MapFeature? {
        val featureInfo = FeatureInfo(null)
        featureInfo.points = feature.points
        featureInfo.id = feature.id
        featureInfo.type = feature.type
        when (feature.type) {
            MapLayer.FeatureType.Polyline -> {
                val polyline = feature as MapLayer.Polyline
                featureInfo.lineWidth = polyline.strokeWidth
                featureInfo.strokeColor = polyline.strokeColor
                featureInfo.dashPattern = polyline.dashPattern
                featureInfo.geodesic = true
            }
            MapLayer.FeatureType.Polygon -> {
                val polygon = feature as MapLayer.Polygon
                featureInfo.holes = null
                featureInfo.lineWidth = polygon.strokeWidth
                featureInfo.strokeColor = polygon.strokeColor
                featureInfo.fillColor = polygon.fillColor
                featureInfo.dashPattern = polygon.dashPattern
                featureInfo.geodesic = true
            }
            null -> {}
            else -> {}
        }

        return addFeature(layerId, featureInfo, closeFeature = true, updateAnimation = false)
    }

    override fun removeAll() {
        for (entry in mapLayers) {
            entry.value?.let { mapLayer ->
                for (feature in mapLayer.features)
                    removeFeature(entry.value.id, feature, false)
            }
        }
        mapLayers.clear()

        for (entry in editableGeographies) {
            for (marker in entry.value)
                removeMarker(marker)

            removeFeature(Strings.EMPTY, entry.key, false)
        }
        editableGeographies.clear()
    }

    override fun setVisible(layer: MapLayer, visible: Boolean) {
        if (mapLayers[layer.id]?.visible == visible)
            return

        for (feature in layer.features)
            setVisible(feature, visible)

        mapLayers[layer.id]?.visible = visible
    }

    private fun setVisible(feature: MapFeature, visible: Boolean) {
        val mapObject = feature.mapObject
        if (mapObject == null) {
            Services.Log.warning("MapFeature object is null")
            return
        }

        when (feature.type) {
            MapLayer.FeatureType.Point -> setMarkerVisible(feature as MapLayer.Point, visible)
            MapLayer.FeatureType.Polyline -> setLineVisible(feature as MapLayer.Polyline, visible)
            MapLayer.FeatureType.Polygon -> setPolygonVisible(feature as MapLayer.Polygon, visible)
            null -> { }
        }
    }

    private fun getLayer(layerId: String?): MapLayer {
        var id = layerId
        if (id.isNullOrEmpty())
            id = LAYER_ID_EMPTY

        var mapLayer = mapLayers[id]
        if (mapLayer == null)
            mapLayer = MapLayer(id)

        return mapLayer
    }

    private fun getLayerBelongingTo(geographyId: String): android.util.Pair<MapLayer, MapFeature>? {
        for ((_, value) in mapLayers.entries)
            for (feature in value.features)
                if (feature.id.equals(geographyId, ignoreCase = true))
                    return android.util.Pair(value, feature)

        return null
    }

    protected fun hasFeatureKey(mapObject: Any?): Boolean {
        val key = getFeatureKey(mapObject)
        return Strings.hasValue(key) && key!!.length >= 36 // GUID length
    }

    protected fun getFeature(key: String?): MapFeature? {
        for (entry in mapLayers)
            for (feature in entry.value.features)
                if (feature.id.equals(key))
                    return feature

        return null
    }

    protected fun getFeatureData(mapObject: Any?): Entity? {
        val featureKey = getFeatureKey(mapObject)
        if (!featureKey.isNullOrEmpty()) {
            val mapItem = getFeature(featureKey)!!.itemData
            return mapItem.data
        }
        return null
    }

    override fun markerDrawnInEditMode(marker: Marker): Boolean {
        val markerId = extractMarkerId(marker)
        for ((_, value) in editableGeographies) {
            for (featureMarker in value)
                if (extractMarkerId(featureMarker).equals(markerId))
                    return true
        }
        return false
    }

    override fun updateDrawnFeatureWithNewPoint(type: MapLayer.FeatureType?, position: IMapLocation, id: String): MapFeature? {
        when (type) {
            MapLayer.FeatureType.Point -> return handleDrawMarkerOnMapClick(position, id)
            MapLayer.FeatureType.Polyline -> handleDrawLineOnMapClick(position)
            MapLayer.FeatureType.Polygon -> handleDrawPolygonOnMapClick(position)
        }
        return null
    }

    override fun handleMarkerDragged(marker: Marker): MapFeature? {
        val id = extractMarkerId(marker)
        val position = getMarkerPosition(marker)
        for ((mapFeature, value) in editableGeographies) {
            if (mapFeature.type == MapLayer.FeatureType.Point || ! mapFeature.isClosed) {
                for (featureMarker in value) {
                    if (extractMarkerId(featureMarker).equals(id)) {
                        val newPoints: MutableList<IMapLocation> = ArrayList()
                        updateMarker(
                            featureMarker, position.latitude, position.longitude,
                            null, null, defaultIconResource, null
                        )

                        for (newMarker in value)
                            newPoints.add(getMarkerPosition(newMarker))

                        mapFeature.points.clear()
                        mapFeature.points.addAll(newPoints)
                        when (mapFeature.type) {
                            MapLayer.FeatureType.Point -> {
                                val secondMarker = mapFeature.mapObject as Marker
                                updateMarker(secondMarker, position)
                                return mapFeature
                            }
                            MapLayer.FeatureType.Polyline -> {
                                val line = mapFeature.mapObject as Line
                                updateLine(line, newPoints)
                            }
                            MapLayer.FeatureType.Polygon -> {
                                val polygonLatLng: MutableList<List<IMapLocation>> = ArrayList()
                                polygonLatLng.add(newPoints)
                                val polygon = mapFeature.mapObject as Polygon
                                updatePolygon(polygon, newPoints)
                            }
                            null -> {
                                return null
                            }
                        }
                    }
                }
            }
        }
        return null
    }

    private fun handleDrawMarkerOnMapClick(mapLocation: IMapLocation, id: String): MapFeature {
        val mapFeature = MapLayer.Point()
        mapFeature.coordinates.add(mapLocation)
        mapFeature.isClosed = true
        val marker = addMarker(id, mapLocation, true, defaultIconResource)
        mapFeature.mapObject = marker
        val editableGeography: MutableList<Marker> = ArrayList()
        editableGeography.add(marker)
        editableGeographies[mapFeature] = editableGeography

        // This method returns the geography because Points are automatically saved
        return mapFeature
    }

    private fun handleDrawLineOnMapClick(mapLocation: IMapLocation) {
        for ((key, value) in editableGeographies) {
            if (key.type == MapLayer.FeatureType.Polyline && ! key.isClosed) {
                val polylineMarker = addMarker(Strings.EMPTY, mapLocation, true, defaultIconResource)
                value.add(polylineMarker)
                key.points.add(mapLocation)
                if (key.mapObject != null) removeLine(key.mapObject as Line)
                val locationList: MutableList<IMapLocation> = ArrayList()
                for (lineMarker in value)
                    locationList.add(getMarkerPosition(lineMarker))

                key.mapObject = addLine(Strings.EMPTY, locationList, null, null, null, false)
                return
            }
        }

        val polyline = MapLayer.Polyline()
        val polylineMarkerList: MutableList<Marker> = ArrayList()
        val polylineMarker = addMarker(Strings.EMPTY, mapLocation, true, defaultIconResource)
        polylineMarkerList.add(polylineMarker)
        polyline.points.add(mapLocation)
        editableGeographies[polyline] = polylineMarkerList
    }

    private fun handleDrawPolygonOnMapClick(mapLocation: IMapLocation) {
        for ((key, value) in editableGeographies) {
            if (key.type == MapLayer.FeatureType.Polygon && ! key.isClosed) {
                val polygonMarker = addMarker(Strings.EMPTY, mapLocation, true, defaultIconResource)
                value.add(polygonMarker)
                key.points.add(mapLocation)
                if (key.mapObject != null)
                    removePolygon(key.mapObject as Polygon)

                val locationList: MutableList<IMapLocation> = ArrayList()
                for (marker in value)
                    locationList.add(getMarkerPosition(marker))

                key.mapObject = addPolygon(
                    Strings.EMPTY, locationList, null,
                    null, null, null, null, false
                )

                return
            }
        }

        val polygon = MapLayer.Polygon()
        val polygonMarkerList: MutableList<Marker> = ArrayList()
        val polygonMarker = addMarker(Strings.EMPTY, mapLocation, true, defaultIconResource)
        polygonMarkerList.add(polygonMarker)
        polygon.outerBoundary.add(mapLocation)
        editableGeographies[polygon] = polygonMarkerList
    }

    override fun saveEdition(): MapFeature? {
        for ((key, value) in editableGeographies) {
            if (!key.isClosed) {
                for (marker in value) removeMarker(marker)
                value.clear()
                key.isClosed = true
                registerFeatureInLayer(LAYER_ID_EMPTY, false, key)
                return key
            }
        }
        return null
    }

    override fun addOrUpdateLocationMarker(location: Location, iconResourceId: Int?) {
        iconResourceId?.let { resourceId ->
            var resource: MapPinHelper.ResourceOrBitmap? = null
            var flat = false
            var anchor: Pair<Float, Float>? = null
            if (locationMarker == null) {
                resource = MapPinHelper.ResourceOrBitmap(resourceId)
                Services.Log.debug("Creating new Location Marker $resourceId")
                flat = true
                anchor = Pair(0.5f, 0.5f)
                locationMarker = addMarker(ID_MY_LOCATION, location.latitude, location.longitude)
            }

            locationMarker?.let { marker ->
                updateMarker(
                    marker, location.latitude, location.longitude, null, flat,
                    resource, anchor
                )

                animateMarker(marker, location)
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun animateMarker(marker: Marker, location: Location) {
        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        val startRotation = extractMarkerRotation(marker)
        val duration: Long = 500
        val interpolator: Interpolator = LinearInterpolator()
        handler.post(object : Runnable {
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val t = interpolator.getInterpolation(elapsed.toFloat() / duration)
                val lng = t * location.longitude + (1 - t) * location.longitude
                val lat = t * location.latitude + (1 - t) * location.latitude
                val rotation = (t * location.bearing + (1 - t) * startRotation).toFloat()
                updateMarker(marker, lat, lng, rotation, null, null, null)
                if (t < 1.0)
                    handler.postDelayed(this, 16)
            }
        })
    }

    override fun getPinImage(data: Entity): MapPinHelper.ResourceOrBitmap {
        return pinHelper.getPinImage(data)
    }

    protected class FeatureTypeNotRecognizedException(type: String?) :
        IllegalArgumentException(String.format("FeatureType '%s' not recognized", type))

    companion object {
        private const val ID_MY_LOCATION = "MY_LOCATION"
        private const val LAYER_ID_EMPTY = "LAYER_EMPTY"
        const val LAYER_ID_DIRECTIONS = "LAYER_DIRECTIONS"
        const val LAYER_ID_SERVER = "LAYER_SERVER"
        const val LAYER_ID_ANIMATION = "LAYER_ANIMATION"
    }

    abstract fun addMarker(id: String, latitude: Double, longitude: Double): Marker
    abstract fun addMarker(
        id: String,
        mapLocation: IMapLocation,
        draggable: Boolean,
        iconData: MapPinHelper.ResourceOrBitmap?
    ): Marker
    abstract fun addLine(
        id: String,
        points: List<IMapLocation>,
        strokeWidth: Float?,
        strokeColor: Int?,
        dashPattern: IntArray?,
        geodesic: Boolean
    ): Line
    abstract fun addPolygon(
        id: String,
        outter: List<IMapLocation>,
        innerHoles: List<List<IMapLocation>>?,
        strokeColor: Int?,
        polygonColor: Int?,
        strokeWidth: Float?,
        dashPattern: IntArray?,
        geodesic: Boolean
    ): Polygon

    abstract fun removeMarker(marker: Marker)
    abstract fun removeLine(line: Line)
    abstract fun removePolygon(polygon: Polygon)

    abstract fun updateMarker(marker: Marker, latitude: Double, longitude: Double, rotation: Float?, flat: Boolean?, iconData: MapPinHelper.ResourceOrBitmap?, anchor: Pair<Float, Float>?)
    abstract fun updateMarker(marker: Marker, location: IMapLocation)
    abstract fun updateLine(line: Line, points: List<IMapLocation>)
    abstract fun updatePolygon(polygon: Polygon, points: List<IMapLocation>)

    abstract fun setMarkerVisible(point: MapLayer.Point, visible: Boolean)
    abstract fun setLineVisible(line: MapLayer.Polyline, visible: Boolean)
    abstract fun setPolygonVisible(polygon: MapLayer.Polygon, visible: Boolean)

    abstract fun buildMarkerOptions(id: String = Strings.EMPTY, mapLocation: IMapLocation, draggable: Boolean, iconData: MapPinHelper.ResourceOrBitmap?): MarkerOptions
    abstract fun buildLineOptions(
        id: String = Strings.EMPTY,
        locationList: List<IMapLocation>,
        strokeWidth: Float? = null,
        strokeColor: Int? = null,
        dashPattern: IntArray?,
        geodesic: Boolean
    ): LineOptions
    abstract fun buildPolygonOptions(
        id: String = Strings.EMPTY,
        outter: List<IMapLocation>,
        innerHoles: List<List<IMapLocation>>?,
        strokeWidth: Float?,
        strokeColor: Int?,
        polygonColor: Int?,
        dashPattern: IntArray?,
        geodesic: Boolean
    ): PolygonOptions

    abstract fun getMarkerImage(pin: MapPinHelper.ResourceOrBitmap?): IconType?
    abstract fun getAnimationLayer(): IAnimationLayer<IMapLocation, MapItemBase<IMapLocation>, Marker>?
}
