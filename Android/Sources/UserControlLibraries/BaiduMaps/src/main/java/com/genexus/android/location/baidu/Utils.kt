package com.genexus.android.location.baidu

import android.location.Location
import com.baidu.location.BDLocation
import com.genexus.android.core.base.services.Services

object Utils {
    fun toLocationFormat(bdLocation: BDLocation?): Location? {
        if (bdLocation == null)
            return null

        return Location("Baidu").apply {
            latitude = bdLocation.latitude
            longitude = bdLocation.longitude
            altitude = bdLocation.altitude
            accuracy = bdLocation.radius
            time = Services.Strings.getDateTime(bdLocation.time, true, true, false).time
            bearing = if (bdLocation.direction >= 0) bdLocation.direction else - 1f
            speed = if (bdLocation.hasSpeed()) bdLocation.speed else - 1f
        }
    }
}
