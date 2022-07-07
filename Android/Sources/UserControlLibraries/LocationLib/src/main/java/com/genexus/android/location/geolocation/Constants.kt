package com.genexus.android.location.geolocation

object Constants {
    const val SDT_ROUTE = "GeneXus.Common.Route"
    const val SDT_ROUTE_NAME = "name"
    const val SDT_ROUTE_DISTANCE = "distance"
    const val SDT_ROUTE_EXPECTED_TRAVEL_TIME = "expectedTravelTime"
    const val SDT_ROUTE_TRANSPORT_TYPE = "transportType"
    const val SDT_ROUTE_GEOLINE = "geoline"

    const val SDT_DIRECTIONS = "GeneXus.Common.Directions"
    const val SDT_DIRECTIONS_ROUTES = "Routes"
    const val SDT_DIRECTIONS_ERROR_MESSAGES = "errorMessages"
    const val SDT_DIRECTIONS_MESSAGES = "Messages"

    const val SDT_DIRECTIONS_REQUEST_PARAMETERS = "GeneXus.Common.DirectionsRequestParameters"
    const val SDT_DIRECTIONS_REQUEST_PARAMETERS_SOURCE = "sourceLocation"
    const val SDT_DIRECTIONS_REQUEST_PARAMETERS_DESTINATION = "destinationLocation"
    const val SDT_DIRECTIONS_REQUEST_PARAMETERS_TRANSPORT_TYPE = "transportType"
    const val SDT_DIRECTIONS_REQUEST_PARAMETERS_REQ_ALTERNATES = "requestAlternateRoutes"
    const val SDT_DIRECTIONS_REQUEST_PARAMETERS_WAYPOINTS = "waypoints"
    const val SDT_DIRECTIONS_REQUEST_PARAMETERS_WAYPOINTS_FIELD = "waypoint"
    const val SDT_DIRECTIONS_REQUEST_PARAMETERS_OPTIMIZE_WAYPOINTS = "optimizeWaypoints"
    const val DIRECTIONS_REQUEST_PARAMETERS_PROPERTY = "DirectionsRequestParameters"
    const val DIRECTIONS_SERVICE_PROVIDER_PROPERTY = "DirectionsServiceProvider"
    const val DIRECTIONS_SERVICE_REQUEST = "DirectionsServiceRequest"

    const val EVENT_DIRECTIONS_CALCULATED = "DirectionsCalculated"

    const val SDT_DIRECTIONS_NAME = "SdtDirections"
    const val SDT_GEOLOCATION_PROXIMITY_ALERT = "SdtGeolocationProximityAlert"
    const val SDT_GEOLOCATION_INFO = "SdtGeolocationInfo"
    const val SDT_LOCATION_INFO = "SdtLocationInfo"
    const val SDT_LOCATION_INFO_LOCATION = "Location"
    const val SDT_LOCATION_INFO_DESCRIPTION = "Description"
    const val SDT_LOCATION_INFO_TIME = "Time"
    const val SDT_LOCATION_INFO_PRECISION = "Precision"
    const val SDT_LOCATION_INFO_RESTRICTED_ACCURACY = "RestrictedAccuracy"
    const val SDT_LOCATION_INFO_HEADING = "Heading"
    const val SDT_LOCATION_INFO_SPEED = "Speed"

    const val SDT_LOCATION_PROXIMITY_ALERT = "SdtLocationProximityAlert"
    const val SDT_PROXIMITY_ALERTS_NAME = "Name"
    const val SDT_PROXIMITY_ALERTS_DESCRIPTION = "Description"
    const val SDT_PROXIMITY_ALERTS_GEOLOCATION = "GeoLocation"
    const val SDT_PROXIMITY_ALERTS_RADIUS = "Radius"
    const val SDT_PROXIMITY_ALERTS_EXPIRATION_TIME = "ExpirationTime"
    const val SDT_PROXIMITY_ALERTS_ACTION_NAME = "ActionName"

    const val SDT_TRACKING_PARAMETERS_CHANGES_INTERVAL = "ChangesInterval"
    const val SDT_TRACKING_PARAMETERS_DISTANCE = "Distance"
    const val SDT_TRACKING_PARAMETERS_ACTION = "Action"
    const val SDT_TRACKING_PARAMETERS_ACTION_INTERVAL = "ActionTimeInterval"
    const val SDT_TRACKING_PARAMETERS_ACCURACY = "Accuracy"
    const val SDT_TRACKING_PARAMETERS_USE_FG_SERVICE = "UseForegroundService"

    const val SDT_LOCATION_PICKER_PARAMETERS_INITIAL_LOC = "InitialLocation"
    const val SDT_LOCATION_PICKER_PARAMETERS_MAP_TYPE = "MapType"
    const val SDT_LOCATION_PICKER_PARAMETERS_SHOW_LOC = "ShowMyLocation"

    const val CORE_MODULES_PACKAGE_OLD = "com.genexuscore.genexus.common"
    const val CORE_MODULES_PACKAGE_NEW = "com.coremodules.genexus.common"
}
