package com.genexus.android.maps

import android.app.Activity
import com.genexus.android.core.base.metadata.enums.Connectivity
import com.genexus.android.core.base.metadata.expressions.Expression
import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.model.EntityFactory
import com.genexus.android.core.base.model.EntityList
import com.genexus.android.core.base.model.PropertiesObject
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.GeoFormats
import com.genexus.android.core.controls.maps.common.IGxMapViewSupportLayers
import com.genexus.android.core.controls.maps.common.IMapLocation
import com.genexus.android.core.controls.maps.common.IMapLocationBounds
import com.genexus.android.core.controls.maps.common.MapDataBase
import com.genexus.android.core.controls.maps.common.MapItemBase
import com.genexus.android.core.utils.TaskRunner
import com.genexus.android.core.utils.TaskRunner.BaseTask
import com.genexus.android.location.geolocation.Constants
import org.json.JSONArray
import org.json.JSONException

open class DirectionsServiceRequest<K : IMapLocation, V : IMapLocationBounds<K>, U : MapItemBase<K>, MapViewData : MapDataBase<U, K, V>>
(mapView: IGxMapViewSupportLayers?, activity: Activity, keyResourceId: Int?, private val providerName: String) :
    MapRouteLayerBase<K, V, U, MapViewData>(mapView, activity, keyResourceId) {

    override fun calculateRoute(
        origin: String,
        destination: String,
        waypoints: List<String>?,
        travelMode: String,
        requestAlternativeRoutes: Boolean,
        shouldDrawOnMap: Boolean
    ): Entity {
        return if (!shouldDrawOnMap) {
            calculateRoute(origin, destination, waypoints, travelMode, requestAlternativeRoutes)
        } else {
            TaskRunner.execute<PropertiesObject>(
                RouteDrawingTask(
                    origin,
                    destination,
                    waypoints,
                    travelMode,
                    requestAlternativeRoutes
                )
            )
            EntityFactory.newEntity()
        }
    }

    inner class RouteDrawingTask(
        private val origin: String,
        private val destination: String,
        private val waypoints: List<String>?,
        private val travelMode: String,
        private val requestAlternatives: Boolean
    ) : BaseTask<PropertiesObject?>() {
        override fun doInBackground(): PropertiesObject? {
            return doDirectionsApiRequest(origin, destination, waypoints, travelMode, requestAlternatives)
        }

        override fun onPostExecute(result: PropertiesObject?) {
            if (result == null) {
                Services.Log.error("doDirectionsApiRequest result is null")
                return
            }

            processOutputToMap(result)
        }
    }

    private fun calculateRoute(
        origin: String,
        destination: String,
        waypoints: List<String>?,
        travelMode: String,
        requestAlternativeRoutes: Boolean
    ): Entity {
        val requestResult = doDirectionsApiRequest(origin, destination, waypoints, travelMode, requestAlternativeRoutes)
            ?: return EntityFactory.newEntity()
        return handleResult(requestResult, travelMode)
    }

    fun doDirectionsApiRequest(
        origin: String,
        destination: String,
        waypoints: List<String>?,
        travelMode: String,
        alternatives: Boolean
    ): PropertiesObject? {
        val procedure = Services.Application.getApplicationServer(Connectivity.Online).getProcedure(
            Constants.DIRECTIONS_SERVICE_REQUEST
        )
        val parameter = PropertiesObject()
        val requestEntityParameter =
            EntityFactory.newSdt(Constants.SDT_DIRECTIONS_REQUEST_PARAMETERS)

        val waypointsCollection = Services.Serializer.createCollection()

        if (waypoints != null)
            for (value in waypoints)
                waypointsCollection.put(value)

        parameter.setProperty(
            Constants.DIRECTIONS_SERVICE_PROVIDER_PROPERTY,
            providerName
        )
        requestEntityParameter.setProperty(
            Constants.SDT_DIRECTIONS_REQUEST_PARAMETERS_SOURCE,
            origin
        )
        requestEntityParameter.setProperty(
            Constants.SDT_DIRECTIONS_REQUEST_PARAMETERS_DESTINATION,
            destination
        )
        requestEntityParameter.setProperty(
            Constants.SDT_DIRECTIONS_REQUEST_PARAMETERS_TRANSPORT_TYPE,
            travelMode
        )
        requestEntityParameter.setProperty(
            Constants.SDT_DIRECTIONS_REQUEST_PARAMETERS_REQ_ALTERNATES,
            alternatives
        )
        requestEntityParameter.setProperty(
            Constants.SDT_DIRECTIONS_REQUEST_PARAMETERS_WAYPOINTS,
            waypointsCollection.toString()
        )
        requestEntityParameter.setProperty(
            Constants.SDT_DIRECTIONS_REQUEST_PARAMETERS_OPTIMIZE_WAYPOINTS,
            false
        )
        parameter.setProperty(
            Constants.DIRECTIONS_REQUEST_PARAMETERS_PROPERTY,
            requestEntityParameter
        )
        val procedureResult = procedure.execute(parameter)
        if (!procedureResult.isOk) {
            Services.Log.error(SERVICE_CALL_FAILED + " - " + procedureResult.errorText)
        } else {
            val result = parameter.optStringProperty(Constants.SDT_DIRECTIONS_ROUTES)
            Services.Log.debug("Routes: $result")
            val resultError = parameter.optStringProperty(Constants.SDT_DIRECTIONS_ERROR_MESSAGES)
            Services.Log.debug("Error: $resultError")
            return parameter
        }
        return null
    }

    fun processOutputToMap(requestResult: PropertiesObject) {
        val result = requestResult.optStringProperty(Constants.SDT_DIRECTIONS_ROUTES)
        try {
            val arrayJson = JSONArray(result)
            if (arrayJson.length() <= 0) {
                Services.Log.info(SERVICE_NO_ROUTES)
                return
            } else {
                val routeJson = arrayJson.getJSONObject(0)
                val geoLineString = routeJson.getString(Constants.SDT_ROUTE_GEOLINE)
                val multiGeoLine = GeoFormats.parseMultiGeoline(geoLineString)
                if (multiGeoLine == null || multiGeoLine.size == 0) {
                    Services.Log.info(SERVICE_NO_ROUTES)
                    return
                }

                val paths = mutableListOf<List<IMapLocation>>()
                for (line in multiGeoLine) {
                    val list = mutableListOf<IMapLocation>()
                    for (pair in line)
                        Services.Maps.newMapLocation(pair.first, pair.second)?.let { list.add(it) }

                    paths.add(list)
                }

                addPathToMap(paths, routeClass, zoomLayer)
            }
        } catch (ex: JSONException) {
            Services.Log.info(ex.message)
        }
    }

    private fun handleResult(requestResult: PropertiesObject, travelMode: String): Entity {
        val routes = EntityList()
        routes.itemType = Expression.Type.SDT
        val errors = EntityList()
        errors.itemType = Expression.Type.SDT
        val resultRoutes = requestResult.optStringProperty(Constants.SDT_DIRECTIONS_ROUTES)
        try {
            val arrayRoutesJson = JSONArray(resultRoutes)
            if (arrayRoutesJson.length() <= 0) {
                Services.Log.info(SERVICE_NO_ROUTES)
                errors.add(messageToEntity(SERVICE_NO_ROUTES, 1))
            } else {
                val routeJson = arrayRoutesJson.getJSONObject(0)
                val summary = routeJson.getString(Constants.SDT_ROUTE_NAME)
                val distanceInMeters = routeJson.getString(Constants.SDT_ROUTE_DISTANCE)
                val durationInSeconds =
                    routeJson.getString(Constants.SDT_ROUTE_EXPECTED_TRAVEL_TIME)
                val geoLineString = routeJson.getString(Constants.SDT_ROUTE_GEOLINE)
                routes.add(
                    routeToEntity(
                        summary,
                        distanceInMeters,
                        durationInSeconds,
                        travelMode,
                        geoLineString
                    )
                )
                Services.Log.debug("Route1 SDT: $routes")
            }
            val message = requestResult.optStringProperty(Constants.SDT_DIRECTIONS_ERROR_MESSAGES)
            val arrayMessageJson = JSONArray(message)
            if (arrayMessageJson.length() > 0) {
                val messageJson = arrayMessageJson.getJSONObject(0)
                val type = messageJson.getInt(SDT_MESSAGES_TYPE)
                val description = messageJson.getString(SDT_MESSAGES_DESCRIPTION)
                errors.add(messageToEntity(description, type))
            }
        } catch (ex: JSONException) {
            Services.Log.error(ex.message)
        }
        fireDirectionsCalculatedEvent(routes, errors)
        val entity = EntityFactory.newSdt(Constants.SDT_DIRECTIONS)
        entity.putLevel(Constants.SDT_DIRECTIONS_ROUTES, routes)
        entity.putLevel(Constants.SDT_DIRECTIONS_MESSAGES, errors)
        return entity
    }
}
