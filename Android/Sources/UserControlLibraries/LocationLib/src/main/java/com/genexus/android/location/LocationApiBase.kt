package com.genexus.android.location

import android.app.Activity
import android.content.Intent
import com.genexus.android.LocationAccuracy.COARSE
import com.genexus.android.core.actions.ApiAction
import com.genexus.android.core.activities.IntentFactory
import com.genexus.android.core.base.metadata.enums.RequestCodes
import com.genexus.android.core.base.metadata.expressions.Expression
import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.model.EntityList
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.GeoFormats
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.controls.maps.LocationApi
import com.genexus.android.core.controls.maps.common.LocationPickerConstants.EXTRA_LOCATION
import com.genexus.android.core.externalapi.ExternalApi
import com.genexus.android.core.externalapi.ExternalApi.IMethodInvoker
import com.genexus.android.core.externalapi.ExternalApiResult
import com.genexus.android.location.geolocation.Constants
import com.genexus.android.maps.GooglePlaceHelper
import com.genexus.android.maps.LocationPickerHelper

open class LocationApiBase(private val apiType: LocationApi, action: ApiAction) : ExternalApi(action) {

    private var changesInterval = 0
    private var minDistance = 0
    private var actionTimeInterval = 0
    private var actionName = Strings.EMPTY
    private var accuracy = 0
    private var useForegroundService = false
    private var proximityAlerts: EntityList? = null
        set(value) {
            value?.let {
                it.itemType = Expression.Type.SDT
                field = it
            }
        }

    private val methodGetLocationHandler = IMethodInvoker { parameters: List<Any> ->
        val minAccuracy = parameters[0].toString().toInt()
        val timeout = parameters[1].toString().toInt()
        val includeHeadingAndSpeed = parameters[2].toString().toBoolean()
        val ignoreErrors = if (parameters.size == 4) parameters[3].toString().toBoolean() else false
        val result = Services.Location.getCurrentLocation(activity, minAccuracy, timeout, includeHeadingAndSpeed, ignoreErrors, true, apiType)
        if (result == null)
            ExternalApiResult.FAILURE
        else
            ExternalApiResult.success(result)
    }

    private val methodStartTrackingHandler = IMethodInvoker { parameters: List<Any> ->
        if (parameters.size == 1) {
            (parameters[0] as Entity?)?.let {
                changesInterval = it.optIntProperty(Constants.SDT_TRACKING_PARAMETERS_CHANGES_INTERVAL) * 1000 // convert seconds to milliseconds
                minDistance = it.optIntProperty(Constants.SDT_TRACKING_PARAMETERS_DISTANCE)
                actionTimeInterval = it.optIntProperty(Constants.SDT_TRACKING_PARAMETERS_ACTION_INTERVAL) // in seconds
                actionName = it.optStringProperty(Constants.SDT_TRACKING_PARAMETERS_ACTION)
                accuracy = it.optIntProperty(Constants.SDT_TRACKING_PARAMETERS_ACCURACY)
                useForegroundService = it.optBooleanProperty(Constants.SDT_TRACKING_PARAMETERS_USE_FG_SERVICE)
            }
        } else {
            changesInterval = parameters[0].toString().toInt()
            minDistance = parameters[1].toString().toInt()
            actionName = parameters[2].toString()
            actionTimeInterval = parameters[3].toString().toInt()
            if (parameters.size == 5) accuracy = parameters[4].toString().toInt()
        }

        Services.Location.startTracking(activity, changesInterval, minDistance, actionTimeInterval, actionName, accuracy, useForegroundService)
        ExternalApiResult.success(true)
    }

    private val methodEndTrackingHandler = IMethodInvoker {
        Services.Location.endTracking()
        ExternalApiResult.success(true)
    }

    private val methodGetLocationHistoryHandler = IMethodInvoker { parameters: List<Any?> ->
        val startDate = Services.Strings.getDate(parameters[0].toString())
        ExternalApiResult.success(Services.Location.getLocationHistory(startDate, apiType))
    }

    private val methodClearLocationHistoryHandler = IMethodInvoker {
        Services.Location.clearLocationHistory()
        ExternalApiResult.success(true)
    }

    private val methodGetDistanceHandler = IMethodInvoker { parameters: List<Any> ->
        val startLocation = if (apiType == LocationApi.GEOLOCATION)
            parameters[0].toString()
        else
            GeoFormats.geopointToGeolocation(parameters[0].toString())

        val endLocation = if (apiType == LocationApi.GEOLOCATION)
            parameters[1].toString()
        else
            GeoFormats.geopointToGeolocation(parameters[1].toString())

        ExternalApiResult.success(GeoFormats.getDistanceFromLocations(startLocation, endLocation))
    }

    private val methodGetLatitudeHandler = IMethodInvoker { parameters: List<Any> ->
        val location = if (apiType == LocationApi.GEOLOCATION)
            GeoFormats.getLatitudeFromLocation(parameters[0].toString())
        else
            GeoFormats.getGeopointLatitude(parameters[0].toString())
        ExternalApiResult.success(location)
    }

    private val methodGetLongitudeHandler = IMethodInvoker { parameters: List<Any> ->
        val location = if (apiType == LocationApi.GEOLOCATION)
            GeoFormats.getLongitudeFromLocation(parameters[0].toString())
        else
            GeoFormats.getGeopointLongitude(parameters[0].toString())
        ExternalApiResult.success(location)
    }

    private val methodReverseGeocodeHandler = IMethodInvoker { parameters: List<Any?> ->
        val location = if (apiType == LocationApi.GEOLOCATION)
            parameters[0].toString()
        else
            GeoFormats.geopointToGeolocation(parameters[0].toString())
        ExternalApiResult.success(Services.Location.reverseGeocodeAddress(location))
    }

    private val methodGeocodeAddressHandler = IMethodInvoker { parameters: List<Any?> ->
        val address = parameters[0].toString()
        ExternalApiResult.success(Services.Location.geocodeAddress(address, apiType))
    }

    private val methodSetProximityAlertsHandler = IMethodInvoker { parameters: List<Any> ->
        proximityAlerts = parameters[0] as EntityList?
        val result = Services.Location.setProximityAlerts(proximityAlerts, apiType)
        ExternalApiResult.success(result)
    }

    private val methodGetProximityAlertsHandler = IMethodInvoker { ExternalApiResult.success(Services.Location.getProximityAlerts(apiType)) }

    private val methodGetCurrentProximityAlertHandler = IMethodInvoker { ExternalApiResult.success(Services.Location.getCurrentProximityAlert(apiType)) }

    private val methodClearProximityAlertsHandler = IMethodInvoker {
        Services.Location.clearProximityAlerts()
        ExternalApiResult.success(true)
    }

    private val methodPickLocationHandler: IMethodInvokerWithActivityResult = object : IMethodInvokerWithActivityResult {
        private var usePlacePicker = false

        override fun invoke(parameters: List<Any>): ExternalApiResult {
            val locationPickerParameters = parameters[0] as Entity?
            var initialLocation = locationPickerParameters?.optStringProperty(Constants.SDT_LOCATION_PICKER_PARAMETERS_INITIAL_LOC)
            if (apiType == LocationApi.MAPS) initialLocation = GeoFormats.geopointToGeolocation(initialLocation)
            val mapType = locationPickerParameters?.optIntProperty(Constants.SDT_LOCATION_PICKER_PARAMETERS_MAP_TYPE)?.let { LocationPickerHelper.parseMapType(it) }
            val showMyLocation = locationPickerParameters?.optBooleanProperty(Constants.SDT_LOCATION_PICKER_PARAMETERS_SHOW_LOC) ?: false
            return callBestLocationPicker(initialLocation, mapType, showMyLocation, 0)
        }

        override fun handleActivityResult(requestCode: Int, resultCode: Int, result: Intent?): ExternalApiResult {
            if (resultCode == Activity.RESULT_OK && result != null) {
                var value = if (usePlacePicker)
                    GooglePlaceHelper.getLocationValueFromResult(context, resultCode, result)
                else
                    result.getStringExtra(EXTRA_LOCATION)

                if (Strings.hasValue(value)) {
                    if (apiType == LocationApi.GEOLOCATION)
                        return ExternalApiResult.success(value!!)
                    else {
                        val latLong = GeoFormats.parseGeolocation(value)
                        if (latLong != null) {
                            value = GeoFormats.buildGeopoint(latLong.first, latLong.second)
                            return ExternalApiResult.success(value)
                        }
                    }
                }
            }
            if (usePlacePicker && resultCode == GooglePlaceHelper.getPlacePickerResultError())
                Services.Log.warning("Call to PlacePicker returned with RESULT_ERROR. Is 'Google Places API for Android' enabled in the Developer Console?")

            return ExternalApiResult.FAILURE
        }

        private fun callBestLocationPicker(initialLocation: String?, mapType: String?, showLocation: Boolean, zoom: Int): ExternalApiResult {
            if (GooglePlaceHelper.isAvailable(context)) {
                usePlacePicker = true
                val placePickerIntent = GooglePlaceHelper.buildIntent(activity, initialLocation, mapType, showLocation, zoom)
                if (placePickerIntent != null) {
                    activity.startActivityForResult(placePickerIntent, RequestCodes.ACTIONNOREFRESH.toInt())
                    return ExternalApiResult.SUCCESS_WAIT
                }
            } else {
                usePlacePicker = false
                val pickerIntent = IntentFactory.getLocationPickerIntent(activity, initialLocation, mapType, showLocation, zoom)
                if (pickerIntent != null) {
                    activity.startActivityForResult(pickerIntent, RequestCodes.ACTIONNOREFRESH.toInt())
                    return ExternalApiResult.SUCCESS_WAIT
                }
            }
            return ExternalApiResult.FAILURE
        }
    }

    private val propertyServiceEnabledHandler = IMethodInvoker { ExternalApiResult.success(Services.Location.isEnabled) }
    private val propertyAuthorizedHandler = IMethodInvoker { ExternalApiResult.success(Services.Location.isAuthorized) }
    private val propertyAuthorizationStatusHandler = IMethodInvoker { ExternalApiResult.success(Services.Location.authorizationStatus) }

    init {
        val getLocationMethodName = if (apiType == LocationApi.GEOLOCATION) METHOD_GET_LOCATION_GEOLOCATION else METHOD_GET_LOCATION_MAPS
        val reverseGeocodeMethodName = if (apiType == LocationApi.GEOLOCATION) METHOD_REVERSE_GEOCODE_GEOLOCATION else METHOD_REVERSE_GEOCODE_MAPS
        val geocodeAddressMethodName = if (apiType == LocationApi.GEOLOCATION) METHOD_GEOCODE_ADDRESS_GEOLOCATION else METHOD_GEOCODE_ADDRESS_MAPS

        addMethodHandlerRequestingPermissions(METHOD_START_TRACKING, 1, requestPermissions, methodStartTrackingHandler)
        addMethodHandlerRequestingPermissions(METHOD_START_TRACKING, 4, requestPermissions, methodStartTrackingHandler)
        addMethodHandlerRequestingPermissions(METHOD_START_TRACKING, 5, requestPermissions, methodStartTrackingHandler)
        addMethodHandlerRequestingPermissions(METHOD_SET_PROXIMITY_ALERTS, 1, requestPermissions, methodSetProximityAlertsHandler)
        addMethodHandlerRequestingPermissions(getLocationMethodName, 3, requiredPermissions, arrayOf(COARSE), methodGetLocationHandler)
        addMethodHandlerRequestingPermissions(getLocationMethodName, 4, requiredPermissions, arrayOf(COARSE), methodGetLocationHandler)
        addMethodHandler(reverseGeocodeMethodName, 1, methodReverseGeocodeHandler)
        addMethodHandler(geocodeAddressMethodName, 1, methodGeocodeAddressHandler)
        addMethodHandler(METHOD_END_TRACKING, 0, methodEndTrackingHandler)
        addMethodHandler(METHOD_GET_LOCATION_HISTORY, 1, methodGetLocationHistoryHandler)
        addMethodHandler(METHOD_CLEAR_LOCATION_HISTORY, 0, methodClearLocationHistoryHandler)
        addMethodHandler(METHOD_GET_DISTANCE, 2, methodGetDistanceHandler)
        addMethodHandler(METHOD_GET_LATITUDE, 1, methodGetLatitudeHandler)
        addMethodHandler(METHOD_GET_LONGITUDE, 1, methodGetLongitudeHandler)
        addMethodHandler(METHOD_GET_PROXIMITY_ALERTS, 0, methodGetProximityAlertsHandler)
        addMethodHandler(METHOD_GET_CURRENT_PROXIMITY_ALERT, 0, methodGetCurrentProximityAlertHandler)
        addMethodHandler(METHOD_CLEAR_PROXIMITY_ALERTS, 0, methodClearProximityAlertsHandler)
        addMethodHandler(METHOD_PICK_LOCATION, 1, methodPickLocationHandler)
        addReadonlyPropertyHandler(PROPERTY_SERVICE_ENABLED, propertyServiceEnabledHandler)
        addReadonlyPropertyHandler(PROPERTY_AUTHORIZED, propertyAuthorizedHandler)
        addReadonlyPropertyHandler(PROPERTY_AUTHORIZATION_STATUS, propertyAuthorizationStatusHandler)
    }

    companion object {
        private val requiredPermissions: Array<String> = Services.Location.requiredPermissions
        private val requestPermissions: Array<String> = Services.Location.requestPermissions
        private const val METHOD_GET_LOCATION_MAPS = "GetLocation"
        private const val METHOD_GET_LOCATION_GEOLOCATION = "GetMyLocation"
        private const val METHOD_START_TRACKING = "StartTracking"
        private const val METHOD_END_TRACKING = "EndTracking"
        private const val METHOD_GET_LOCATION_HISTORY = "GetLocationHistory"
        private const val METHOD_CLEAR_LOCATION_HISTORY = "ClearLocationHistory"
        private const val METHOD_GET_DISTANCE = "GetDistance"
        private const val METHOD_GET_LATITUDE = "GetLatitude"
        private const val METHOD_GET_LONGITUDE = "GetLongitude"
        private const val METHOD_REVERSE_GEOCODE_MAPS = "ReverseGeocode"
        private const val METHOD_REVERSE_GEOCODE_GEOLOCATION = "GetAddress"
        private const val METHOD_GEOCODE_ADDRESS_MAPS = "GeocodeAddress"
        private const val METHOD_GEOCODE_ADDRESS_GEOLOCATION = "GetLocation"
        private const val METHOD_SET_PROXIMITY_ALERTS = "SetProximityAlerts"
        private const val METHOD_GET_PROXIMITY_ALERTS = "GetProximityAlerts"
        private const val METHOD_GET_CURRENT_PROXIMITY_ALERT = "GetCurrentProximityAlert"
        private const val METHOD_CLEAR_PROXIMITY_ALERTS = "ClearProximityAlerts"
        private const val METHOD_PICK_LOCATION = "PickLocation"
        private const val PROPERTY_SERVICE_ENABLED = "ServiceEnabled"
        private const val PROPERTY_AUTHORIZED = "Authorized"
        private const val PROPERTY_AUTHORIZATION_STATUS = "AuthorizationStatus"
    }
}
