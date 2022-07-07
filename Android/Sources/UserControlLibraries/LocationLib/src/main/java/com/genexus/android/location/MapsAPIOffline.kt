package com.genexus.android.location

import android.app.Activity
import com.genexus.GXBaseCollection
import com.genexus.GXGeospatial
import com.genexus.GXSimpleCollection
import com.genexus.android.LocationAccuracy.COARSE
import com.genexus.android.WithBackgroundPermission
import com.genexus.android.core.activities.ActivityHelper
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.GeoFormats
import com.genexus.android.core.base.utils.ResultRunnable
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.controls.maps.LocationApi
import com.genexus.android.core.utils.GXInstanceFactory
import com.genexus.android.location.geolocation.Constants
import com.genexus.xml.GXXMLSerializable
import org.json.JSONException
import org.json.JSONObject
import java.util.Date
import java.util.Vector

class MapsAPIOffline {

    companion object {
        private val activity: Activity? by lazy { ActivityHelper.getCurrentActivity() }
        private val MAPS = LocationApi.MAPS

        @JvmStatic
        @JvmOverloads
        fun calculateDirections(startGeopoint: GXGeospatial, endGeopoint: GXGeospatial, transportType: String? = Strings.EMPTY, requestAlternateRoutes: Boolean? = false): Any {
            return calculateDirections(startGeopoint.toWKT(), endGeopoint.toWKT(), null, transportType, requestAlternateRoutes)
        }

        @JvmStatic
        fun calculateDirections(directionsRequestParameters: GXXMLSerializable): Any {
            val json = JSONObject(directionsRequestParameters.toJSonString())
            val origin = json.getString(Constants.SDT_DIRECTIONS_REQUEST_PARAMETERS_SOURCE)
            val destination = json.getString(Constants.SDT_DIRECTIONS_REQUEST_PARAMETERS_DESTINATION)
            val transportType = json.getString(Constants.SDT_DIRECTIONS_REQUEST_PARAMETERS_TRANSPORT_TYPE)
            val requestAlternatives = json.getBoolean(Constants.SDT_DIRECTIONS_REQUEST_PARAMETERS_REQ_ALTERNATES)
            val waypointsJsonArray = json.getJSONArray(Constants.SDT_DIRECTIONS_REQUEST_PARAMETERS_WAYPOINTS)
            val waypoints = mutableListOf<String>()
            for (i in 0 until waypointsJsonArray.length())
                waypoints.add(waypointsJsonArray.getString(0))

            return calculateDirections(origin, destination, waypoints, transportType, requestAlternatives)
        }

        private fun calculateDirections(
            origin: String,
            destination: String,
            waypoints: List<String>?,
            travelMode: String?,
            requestAlternativeRoutes: Boolean?
        ): Any {
            val sdtDirections = newDirections()
            val data = Services.Maps.calculateDirections(activity, origin, destination, waypoints, travelMode, requestAlternativeRoutes)
            if (data != null)
                sdtDirections.fromJSonString(data.toString())

            return sdtDirections
        }

        @JvmStatic
        @JvmOverloads
        fun getLocation(minAccuracy: Int?, timeout: Int?, includeHeadingAndSpeed: Boolean?, ignoreErrors: Boolean? = false): Any {
            return executeRequestingPermission(
                Services.Location.requiredPermissions, arrayOf(COARSE),
                { internalGetMyLocation(minAccuracy, timeout, includeHeadingAndSpeed, ignoreErrors) },
                { newGeolocationInfo() }
            )
        }

        @JvmStatic
        @Throws(JSONException::class)
        fun startTracking(trackingParameters: GXXMLSerializable) {
            val json = trackingParameters.toJSonString()
            val jsonObject = JSONObject(json)
            val changesInterval = jsonObject.getInt(Constants.SDT_TRACKING_PARAMETERS_CHANGES_INTERVAL)
            val minDistance = jsonObject.getInt(Constants.SDT_TRACKING_PARAMETERS_DISTANCE)
            val action = jsonObject.getString(Constants.SDT_TRACKING_PARAMETERS_ACTION)
            val actionTimeInterval = jsonObject.getInt(Constants.SDT_TRACKING_PARAMETERS_ACTION_INTERVAL)
            val accuracy = jsonObject.getInt(Constants.SDT_TRACKING_PARAMETERS_ACCURACY)
            val useForegroundService = jsonObject.getBoolean(Constants.SDT_TRACKING_PARAMETERS_USE_FG_SERVICE)
            startTracking(changesInterval, minDistance, action, actionTimeInterval, accuracy, useForegroundService)
        }

        @JvmStatic
        @JvmOverloads
        fun startTracking(changesInterval: Int?, minDistance: Int?, action: String?, actionTimeInterval: Int?, accuracy: Int? = 0, useForegroundService: Boolean? = false) {
            val successCode = ResultRunnable { Services.Location.startTracking(activity, changesInterval!!, minDistance!!, actionTimeInterval!!, action, accuracy!!, useForegroundService!!) }
            val failureCode = ResultRunnable { }
            executeRequestingPermission(Services.Location.requestPermissions, successCode, failureCode)
        }

        @JvmStatic
        fun endTracking() {
            Services.Location.endTracking()
        }

        @JvmStatic
        fun getLocationHistory(startDate: Date?): GXBaseCollection<*>? {
            val base = newSdtCollection(
                "${Constants.CORE_MODULES_PACKAGE_OLD}.${Constants.SDT_LOCATION_INFO}",
                "${Constants.CORE_MODULES_PACKAGE_NEW}.${Constants.SDT_LOCATION_INFO}",
                "LocationInfo",
                "Location"
            )

            val jsonArray = Services.Location.getLocationHistory(startDate, MAPS)
            base.fromJSonString(jsonArray.toString())
            return base
        }

        @JvmStatic
        fun clearLocationHistory() {
            Services.Location.clearLocationHistory()
        }

        @JvmStatic
        fun getLatitude(geopoint: GXGeospatial): Double {
            return GeoFormats.getGeopointLatitude(geopoint.toWKT())
        }

        @JvmStatic
        fun getLongitude(geopoint: GXGeospatial): Double {
            return GeoFormats.getGeopointLongitude(geopoint.toWKT())
        }

        @JvmStatic
        fun reverseGeocode(locationGeopoint: GXGeospatial): Vector<String> {
            val addressesVector: Vector<String> = GXSimpleCollection()
            val location = GeoFormats.geopointToGeolocation(locationGeopoint.toWKT())
            val addressCollection = Services.Location.reverseGeocodeAddress(location)
            for (i in 0 until addressCollection.length()) {
                try {
                    val address = addressCollection.getString(i)
                    addressesVector.add(address)
                } catch (ex: JSONException) {
                    Services.Log.error(ex.message)
                }
            }
            return addressesVector
        }

        @JvmStatic
        fun geocodeAddress(address: String?): Vector<GXGeospatial> {
            val geopointVector: Vector<GXGeospatial> = GXSimpleCollection()
            val geolocationCollection = Services.Location.geocodeAddress(address, MAPS)
            for (i in 0 until geolocationCollection.length()) {
                try {
                    val location = geolocationCollection.getString(i)
                    geopointVector.add(GXGeospatial(location))
                } catch (ex: JSONException) {
                    Services.Log.error(ex.message)
                }
            }
            return geopointVector
        }

        @JvmStatic
        fun setProximityAlerts(proximityAlerts: GXBaseCollection<*>): Boolean {
            return executeRequestingPermission(Services.Location.requestPermissions, { internalSetProximityAlerts(proximityAlerts) }, { false })
        }

        @JvmStatic
        fun getProximityAlerts(): GXBaseCollection<*>? {
            val base = newSdtCollection(
                "${Constants.CORE_MODULES_PACKAGE_OLD}.${Constants.SDT_LOCATION_PROXIMITY_ALERT}",
                "${Constants.CORE_MODULES_PACKAGE_NEW}.${Constants.SDT_LOCATION_PROXIMITY_ALERT}",
                "LocationProximityAlert", Constants.SDT_PROXIMITY_ALERTS_GEOLOCATION
            )

            val jsonAlerts = json.org.json.JSONArray()
            val proximityAlerts = Services.Location.getProximityAlerts(MAPS)
            try {
                for (i in 0 until proximityAlerts.size) {
                    val current = proximityAlerts[i]
                    val proximityAlertJSONObj = json.org.json.JSONObject()
                    proximityAlertJSONObj.put(Constants.SDT_PROXIMITY_ALERTS_NAME, current.optStringProperty(Constants.SDT_PROXIMITY_ALERTS_NAME))
                    proximityAlertJSONObj.put(Constants.SDT_PROXIMITY_ALERTS_DESCRIPTION, current.optStringProperty(Constants.SDT_PROXIMITY_ALERTS_DESCRIPTION))
                    proximityAlertJSONObj.put(Constants.SDT_PROXIMITY_ALERTS_GEOLOCATION, current.optStringProperty(Constants.SDT_PROXIMITY_ALERTS_GEOLOCATION))
                    proximityAlertJSONObj.put(Constants.SDT_PROXIMITY_ALERTS_RADIUS, current.optStringProperty(Constants.SDT_PROXIMITY_ALERTS_RADIUS))
                    proximityAlertJSONObj.put(Constants.SDT_PROXIMITY_ALERTS_EXPIRATION_TIME, current.optStringProperty(Constants.SDT_PROXIMITY_ALERTS_EXPIRATION_TIME))
                    proximityAlertJSONObj.put(Constants.SDT_PROXIMITY_ALERTS_ACTION_NAME, current.optStringProperty(Constants.SDT_PROXIMITY_ALERTS_ACTION_NAME))
                    jsonAlerts.put(proximityAlertJSONObj)
                }
            } catch (e: json.org.json.JSONException) {
                Services.Log.error("Failed to build proximity alerts collection", e)
            }

            base.fromJSonString(jsonAlerts.toString())
            return base
        }

        @JvmStatic
        fun getCurrentProximityAlert(): Any? {
            val sdtLocationProximityAlert = newProximityAlert()
            val alert = Services.Location.getCurrentProximityAlert(MAPS)
            sdtLocationProximityAlert.fromJSonString(alert.toString())
            return sdtLocationProximityAlert
        }

        @JvmStatic
        fun clearProximityAlerts() {
            Services.Location.clearProximityAlerts()
        }

        @JvmStatic
        fun getDistance(startGeopoint: GXGeospatial, endGeopoint: GXGeospatial): Int {
            val startLocation = GeoFormats.geopointToGeolocation(startGeopoint.toWKT())
            val endLocation = GeoFormats.geopointToGeolocation(endGeopoint.toWKT())
            return GeoFormats.getDistanceFromLocations(startLocation, endLocation)
        }

        private fun internalGetMyLocation(minAccuracy: Int?, timeout: Int?, includeHeadingAndSpeed: Boolean?, ignoreErrors: Boolean?): Any {
            val geolocationInfo = newGeolocationInfo()
            val geolocationInfoJSONObj = Services.Location.getCurrentLocation(
                activity,
                minAccuracy!!, timeout!!, includeHeadingAndSpeed!!, ignoreErrors!!, false, MAPS
            )
            if (geolocationInfoJSONObj != null)
                geolocationInfo.fromJSonString(geolocationInfoJSONObj.toString())

            return geolocationInfo
        }

        private fun newSdtCollection(clazz: String, fallbackClazz: String, elementsName: String, xmlNamespace: String): GXBaseCollection<*> {
            val remoteHandle = Services.Application.get().remoteHandle
            return try {
                GXInstanceFactory.getGXBaseCollectionInstance(clazz, elementsName, xmlNamespace, remoteHandle)
            } catch (ex: IllegalStateException) {
                GXInstanceFactory.getGXBaseCollectionInstance(fallbackClazz, elementsName, xmlNamespace, remoteHandle)
            }
        }

        private fun newDirections(): GXXMLSerializable {
            return newSdt(
                "${Constants.CORE_MODULES_PACKAGE_OLD}.${Constants.SDT_DIRECTIONS_NAME}",
                "${Constants.CORE_MODULES_PACKAGE_NEW}.${Constants.SDT_DIRECTIONS_NAME}"
            )
        }

        private fun newGeolocationInfo(): GXXMLSerializable {
            return newSdt(
                "${Constants.CORE_MODULES_PACKAGE_OLD}.${Constants.SDT_LOCATION_INFO}",
                "${Constants.CORE_MODULES_PACKAGE_NEW}.${Constants.SDT_LOCATION_INFO}"
            )
        }

        private fun newProximityAlert(): GXXMLSerializable {
            return newSdt(
                "${Constants.CORE_MODULES_PACKAGE_OLD}.${Constants.SDT_LOCATION_PROXIMITY_ALERT}",
                "${Constants.CORE_MODULES_PACKAGE_NEW}.${Constants.SDT_LOCATION_PROXIMITY_ALERT}"
            )
        }

        private fun newSdt(clazz: String, fallbackClazz: String): GXXMLSerializable {
            return try {
                GXInstanceFactory.getGXXMLSerializable(clazz, true)
            } catch (ex: IllegalStateException) {
                GXInstanceFactory.getGXXMLSerializable(fallbackClazz, true)
            }
        }

        private fun internalSetProximityAlerts(proximityAlerts: GXBaseCollection<*>): Boolean {
            return try {
                val proximityAlertsJSONArray = proximityAlerts.GetJSONObject() as json.org.json.JSONArray
                for (i in 0 until proximityAlertsJSONArray.length()) {
                    val proximityAlertJSONObj: json.org.json.JSONObject = if (proximityAlertsJSONArray[i] is json.org.json.JSONObject)
                        proximityAlertsJSONArray.getJSONObject(i)
                    else
                        proximityAlerts.struct[i] as json.org.json.JSONObject

                    val geopoint = proximityAlertJSONObj[Constants.SDT_PROXIMITY_ALERTS_GEOLOCATION] as GXGeospatial
                    val latLong = GeoFormats.parseGeopoint(geopoint.toWKT()) ?: continue
                    val name = proximityAlertJSONObj[Constants.SDT_PROXIMITY_ALERTS_NAME] as String
                    val description = proximityAlertJSONObj[Constants.SDT_PROXIMITY_ALERTS_DESCRIPTION] as String
                    val geolocation = GeoFormats.buildGeolocation(latLong.first, latLong.second)
                    val radius = proximityAlertJSONObj[Constants.SDT_PROXIMITY_ALERTS_RADIUS].toString().toInt()
                    val expirationTime = proximityAlertJSONObj[Constants.SDT_PROXIMITY_ALERTS_EXPIRATION_TIME] as String
                    val actionName = proximityAlertJSONObj[Constants.SDT_PROXIMITY_ALERTS_ACTION_NAME] as String
                    Services.Location.createProximityAlert(
                        name, description, geolocation,
                        radius, expirationTime, actionName, true, 0
                    )
                }

                true
            } catch (e: json.org.json.JSONException) {
                Services.Log.error(e)
                false
            }
        }

        @JvmStatic
        fun authorizationStatus(): Int {
            return Services.Location.authorizationStatus
        }

        @JvmStatic
        fun authorized(): Boolean {
            return Services.Location.isAuthorized
        }

        @JvmStatic
        fun serviceEnabled(): Boolean {
            return Services.Location.isEnabled
        }

        fun <T> executeRequestingPermission(neededPermissions: Array<String>, successCode: ResultRunnable<T>, failureCode: ResultRunnable<T>): T {
            return executeRequestingPermission(null, neededPermissions, successCode, failureCode)
        }

        fun <T> executeRequestingPermission(requestPermissions: Array<String>?, neededPermissions: Array<String>, successCode: ResultRunnable<T>, failureCode: ResultRunnable<T>): T {
            return WithBackgroundPermission<T>(activity, requestPermissions, neededPermissions).apply {
                this.blockThread = true
                this.attachToController = true
                this.successCode = successCode
                this.failureCode = failureCode
            }.run()
        }
    }
}
