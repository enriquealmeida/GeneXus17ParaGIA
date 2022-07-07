package com.genexus.android.core.controls.maps

data class LocationRequestParameters(
    val expirationTime: Long,
    val fastestInterval: Long,
    val interval: Long,
    val maxWaitTime: Long,
    val numUpdates: Int,
    val priority: Int,
    val smallestDisplacement: Float,
    val useForegroundService: Boolean,
    val includeHeadingSpeed: Boolean
) {
    companion object {
        fun default(): LocationRequestParameters {
            return LocationRequestParameters(
                expirationTime = 0,
                fastestInterval = 1000,
                interval = 1000,
                maxWaitTime = 0,
                numUpdates = 0,
                priority = 100, // LocationRequest.PRIORITY_HIGH_ACCURACY
                smallestDisplacement = 0f,
                useForegroundService = false,
                includeHeadingSpeed = false
            )
        }
    }
}
