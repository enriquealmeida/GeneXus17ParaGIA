package com.genexus.android.maps

import android.app.Activity
import com.genexus.android.core.actions.ExternalObjectEvent
import com.genexus.android.core.activities.IGxActivity
import com.genexus.android.core.base.metadata.expressions.Expression
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition
import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.model.EntityFactory
import com.genexus.android.core.base.model.EntityList
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.GeoFormats
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.controls.maps.common.IGxMapViewSupportLayers
import com.genexus.android.core.controls.maps.common.IMapLocation
import com.genexus.android.core.controls.maps.common.IMapLocationBounds
import com.genexus.android.core.controls.maps.common.IMapRouteLayer
import com.genexus.android.core.controls.maps.common.MapDataBase
import com.genexus.android.core.controls.maps.common.MapItemBase
import com.genexus.android.core.controls.maps.common.MapLayer
import com.genexus.android.core.utils.ThemeUtils
import com.genexus.android.location.geolocation.Constants
import com.genexus.android.maps.GeographiesManagerBase.Companion.LAYER_ID_DIRECTIONS

abstract class MapRouteLayerBase<K : IMapLocation, V : IMapLocationBounds<K>, U : MapItemBase<K>, MapViewData : MapDataBase<U, K, V>>
(val mapView: IGxMapViewSupportLayers?, val activity: Activity, keyResourceId: Int?) : IMapRouteLayer<MapViewData> {

    val accessToken = keyResourceId?.let { activity.resources.getString(it) }
    var routeClass: ThemeClassDefinition? = null
    var mapLayer: MapLayer? = null
    var zoomLayer = false

    override fun removeRouteLayer() {
        mapView?.removeLayer(mapLayer)
    }

    override fun addRouteLayer(mapData: MapViewData, travelMode: String, mapRouteClass: ThemeClassDefinition, zoomToLayer: Boolean) {
        routeClass = mapRouteClass
        zoomLayer = zoomToLayer

        val geopointList = mutableListOf<IMapLocation>()
        for (item in mapData) {
            item?.let {
                if (it.featureType == MapLayer.FeatureType.Point)
                    it.locationList[0]?.let { loc -> geopointList.add(loc) }
            }
        }

        if (geopointList.size < 2) {
            Services.Log.info(SERVICE_FEW_POINTS)
            return
        }

        var origin: String = Strings.EMPTY
        var destination: String = Strings.EMPTY
        val waypoints = mutableListOf<String>()
        val waypointsSize = geopointList.size - 2
        for ((i, wp) in geopointList.withIndex()) {
            if (i == 0) {
                origin = GeoFormats.buildGeopoint(wp.latitude, wp.longitude)
            } else {
                if (i <= waypointsSize)
                    waypoints.add(i - 1, GeoFormats.buildGeopoint(wp.latitude, wp.longitude))
                else
                    destination = GeoFormats.buildGeopoint(wp.latitude, wp.longitude)
            }
        }

        calculateRoute(origin, destination, waypoints, travelMode, requestAlternativeRoutes = false, shouldDrawOnMap = true)
    }

    protected fun addPathToMap(path: List<List<IMapLocation>>, mapRouteClass: ThemeClassDefinition?, zoomToLayer: Boolean) {
        val newLayer = MapLayer(LAYER_ID_DIRECTIONS)
        for (line in path) {
            val polyline = MapLayer.Polyline()
            polyline.points.addAll(line)
            ThemeUtils.applyMapFeatureClass(polyline, mapRouteClass)
            newLayer.features.add(polyline)
        }

        val taskAddLayer = Runnable {
            mapView?.addLayer(newLayer)
            if (zoomToLayer)
                mapView?.adjustBoundsToLayer(newLayer)
        }

        Services.Device.runOnUiThread(taskAddLayer)
        mapLayer = newLayer
    }

    protected fun fireDirectionsCalculatedEvent(routes: EntityList, errors: EntityList) {
        if (activity is IGxActivity) {
            val event = ExternalObjectEvent(OBJECT_NAME_MAPS, Constants.EVENT_DIRECTIONS_CALCULATED)
            val coordinator = event.getFormCoordinatorForEvent(activity)
            coordinator?.let { event.fire(listOf<Any>(routes, errors), it, null) }
        }
    }

    protected fun routeToEntity(summary: String, distanceInMeters: String, durationInSeconds: String, travelMode: String, geoline: String): Entity {
        val entity = EntityFactory.newSdt(Constants.SDT_ROUTE)
        entity.setProperty(Constants.SDT_ROUTE_NAME, summary)
        entity.setProperty(Constants.SDT_ROUTE_DISTANCE, distanceInMeters)
        entity.setProperty(Constants.SDT_ROUTE_EXPECTED_TRAVEL_TIME, durationInSeconds)
        entity.setProperty(Constants.SDT_ROUTE_TRANSPORT_TYPE, travelMode)
        entity.setProperty(Constants.SDT_ROUTE_GEOLINE, geoline)
        return entity
    }

    protected fun messageToEntity(message: String, typeMsg: Int): Entity {
        if (typeMsg == 1 && message.isNotEmpty())
            Services.Log.error(message)

        val entity = EntityFactory.newSdt(SDT_MESSAGES)
        entity.setProperty(SDT_MESSAGES_TYPE, typeMsg)
        entity.setProperty(SDT_MESSAGES_DESCRIPTION, message)
        return entity
    }

    protected fun initEntityList(): EntityList {
        return EntityList().apply { itemType = Expression.Type.SDT }
    }

    protected fun getDirectionsSDT(routes: EntityList, errors: EntityList): Entity {
        val entity = EntityFactory.newSdt(Constants.SDT_DIRECTIONS)
        entity.putLevel(Constants.SDT_DIRECTIONS_ROUTES, routes)
        entity.putLevel(Constants.SDT_DIRECTIONS_MESSAGES, errors)
        return entity
    }

    companion object {
        const val OBJECT_NAME_MAPS = "GeneXus.Common.Maps"
        const val SERVICE_CALL_FAILED = "Directions Service call failed, make sure you set the right user and access token"
        const val SERVICE_NO_ROUTES = "Directions Services call returned no routes for these points and transportation type"
        const val SERVICE_FEW_POINTS = "Cannot show Route Layer with less than two points"
        const val ALTERNATIVES_TRAFFIC_MODE = "Alternative routes won't be calculated since travel mode is set to Traffic"
        const val CLIENT_BUILD_FAILED = "Cannot build Directions client"
        const val NULL_ACCESS_TOKEN = "AccessToken cannot be null at this point"
        const val NULL_ORIGIN_DESTINATION = "Origin and destination cannot be null"
        const val SDT_MESSAGES = "GeneXus.Common.Messages"
        const val SDT_MESSAGES_TYPE = "Type"
        const val SDT_MESSAGES_DESCRIPTION = "Description"
        const val TRAVEL_MODE_WALKING = "Walking"
        const val TRAVEL_MODE_TRANSIT = "Transit"
        const val TRAVEL_MODE_BICYCLING = "Bicycling"
        const val TRAVEL_MODE_DRIVING = "Driving"
    }
}
