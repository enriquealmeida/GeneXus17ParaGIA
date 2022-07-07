package com.genexus.android.controls.maps.mapbox

import android.app.Activity
import android.util.Pair
import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.model.EntityFactory
import com.genexus.android.core.base.model.EntityList
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.synchronization.SynchronizedResult
import com.genexus.android.core.base.utils.GeoFormats
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.controls.maps.common.IGxMapViewSupportLayers
import com.genexus.android.core.controls.maps.common.IMapLocation
import com.genexus.android.maps.MapRouteLayerBase
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Point
import com.mapbox.geojson.utils.PolylineUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class MapRouteLayer(mapView: IGxMapViewSupportLayers?, activity: Activity) :
    MapRouteLayerBase<MapLocation, MapLocationBounds, GxMapViewItem, GxMapViewData>(mapView, activity, R.string.MapsApiKey) {

    override fun calculateRoute(
        origin: String,
        destination: String,
        waypoints: List<String>?,
        travelMode: String,
        requestAlternativeRoutes: Boolean,
        shouldDrawOnMap: Boolean
    ): Entity {

        var callback: Callback<DirectionsResponse>? = null
        if (shouldDrawOnMap)
            callback = drawToMapCallback

        return calculateRoute(origin, destination, waypoints, travelMode, requestAlternativeRoutes, callback)
    }

    private fun calculateRoute(
        origin: String,
        destination: String,
        waypoints: List<String>?,
        travelMode: String,
        requestAlternativeRoutes: Boolean,
        callback: Callback<DirectionsResponse>?
    ): Entity {

        if (accessToken == null)
            throw IllegalStateException(NULL_ACCESS_TOKEN)

        val syncedEntity = SynchronizedResult<Entity>()
        val routes = initEntityList()
        val messages = initEntityList()

        val originLatLng = GeoFormats.parseGeopoint(origin)
        val destinationLatLng = GeoFormats.parseGeopoint(destination)

        if (originLatLng == null || destinationLatLng == null) {
            messages.addEntity(messageToEntity(NULL_ORIGIN_DESTINATION, 1))
            return getDirectionsSDT(routes, messages)
        }

        val criteria = travelModeToMapbox(travelMode)

        // Alternative routes is not supported when Travel Mode = Traffic
        val alternatives = requestAlternativeRoutes && criteria != DirectionsCriteria.PROFILE_DRIVING_TRAFFIC
        if (alternatives != requestAlternativeRoutes)
            Services.Log.warning(ALTERNATIVES_TRAFFIC_MODE)

        // Mapbox expects inverted coordinates (LngLat, Geopoint order)
        val builder = MapboxDirections.builder()
            .origin(Point.fromLngLat(originLatLng.second, originLatLng.first))
            .destination(Point.fromLngLat(destinationLatLng.second, destinationLatLng.first))
            .overview(DirectionsCriteria.OVERVIEW_FULL)
            .alternatives(alternatives)
            .profile(criteria)
            .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6)
            .accessToken(accessToken)

        waypoints?.let { list ->
            for (waypoint in list) {
                val coordinates = GeoFormats.parseGeopoint(waypoint)
                coordinates?.let {
                    builder.addWaypoint(Point.fromLngLat(it.first, it.second))
                }
            }
        }

        val client = builder.build()
        if (client == null) {
            messages.addEntity(messageToEntity(CLIENT_BUILD_FAILED, 1))
            return getDirectionsSDT(routes, messages)
        }

        if (callback != null) {
            client.enqueueCall(callback)
            return EntityFactory.newEntity()
        } else {
            client.enqueueCall(object : Callback<DirectionsResponse> {
                override fun onResponse(call: Call<DirectionsResponse>, response: Response<DirectionsResponse>) {
                    when {
                        response.body() == null -> {
                            messages.addEntity(messageToEntity(SERVICE_CALL_FAILED, 1))
                        }
                        response.body() !!.routes().size < 1 -> {
                            messages.addEntity(messageToEntity(SERVICE_NO_ROUTES, 1))
                        }
                        else -> {
                            val responseRoutes = response.body() !!.routes()
                            for (route in responseRoutes)
                                routes.addEntity(mapboxRouteToEntity(route))
                        }
                    }

                    setResultAndFireEvent(routes, messages)
                }

                override fun onFailure(call: Call<DirectionsResponse>, throwable: Throwable) {
                    Services.Log.error(throwable)
                    messages.addEntity(messageToEntity(throwable.message ?: Strings.EMPTY, 1))
                    setResultAndFireEvent(routes, messages)
                }

                private fun setResultAndFireEvent(routes: EntityList, errors: EntityList) {
                    syncedEntity.result = getDirectionsSDT(routes, errors)
                    fireDirectionsCalculatedEvent(routes, errors)
                }
            })
        }

        return syncedEntity.result
    }

    private val drawToMapCallback = object : Callback<DirectionsResponse> {
        override fun onResponse(call: Call<DirectionsResponse>, response: Response<DirectionsResponse>) {
            if (response.body() == null || response.body()?.routes().isNullOrEmpty()) {
                Services.Log.error(SERVICE_NO_ROUTES)
                return
            }

            val routes = response.body()!!.routes()
            val directionsRoute = routes[0]
            directionsRoute.geometry()?.let {
                val path: MutableList<IMapLocation> = ArrayList()
                val pointList = PolylineUtils.decode(it, 6)
                if (pointList.isNotEmpty()) {
                    for (position in pointList)
                        path.add(MapLocation(position.latitude(), position.longitude()))

                    val finalList = mutableListOf<List<IMapLocation>>()
                    finalList.add(path)
                    addPathToMap(finalList, routeClass, zoomLayer)
                }
            }
        }

        override fun onFailure(call: Call<DirectionsResponse>, throwable: Throwable) {
            Services.Log.error(throwable)
        }
    }

    private fun travelModeToMapbox(travelMode: String): String {
        return when {
            travelMode.equals(TRAVEL_MODE_WALKING, ignoreCase = true) -> DirectionsCriteria.PROFILE_WALKING
            travelMode.equals(TRAVEL_MODE_TRANSIT, ignoreCase = true) -> DirectionsCriteria.PROFILE_DRIVING_TRAFFIC
            travelMode.equals(TRAVEL_MODE_BICYCLING, ignoreCase = true) -> DirectionsCriteria.PROFILE_CYCLING
            else -> DirectionsCriteria.PROFILE_DRIVING
        }
    }

    private fun mapboxModeToTravelMode(mapboxMode: String): String {
        return when {
            mapboxMode.equals(DirectionsCriteria.PROFILE_WALKING, ignoreCase = true) -> TRAVEL_MODE_WALKING
            mapboxMode.equals(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC, ignoreCase = true) -> TRAVEL_MODE_TRANSIT
            mapboxMode.equals(DirectionsCriteria.PROFILE_CYCLING, ignoreCase = true) -> TRAVEL_MODE_BICYCLING
            else -> TRAVEL_MODE_DRIVING
        }
    }

    private fun mapboxRouteToEntity(route: DirectionsRoute): Entity {
        val durationInSeconds = route.durationTypical()?.toString()
            ?: route.duration().toString()
        val distance = route.distance().toString()
        val summary = route.legs()?.get(0)?.summary() ?: Strings.EMPTY
        val mode = mapboxModeToTravelMode(route.routeOptions()?.profile() ?: Strings.EMPTY)
        val pointsList = mutableListOf<Pair<Double, Double>>()
        val geometryPositions = PolylineUtils.decode(route.geometry() ?: Strings.EMPTY, 6)
        for (position in geometryPositions)
            pointsList.add(Pair(position.latitude(), position.longitude()))

        val geoline = GeoFormats.buildGeoline(pointsList)
        return routeToEntity(summary, distance, durationInSeconds, mode, geoline)
    }
}
