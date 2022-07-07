package com.genexus.android.location

import android.content.Context
import com.genexus.android.core.base.metadata.enums.ControlTypes
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.common.TrnHelper
import com.genexus.android.core.externalapi.ExternalApiDefinition
import com.genexus.android.core.externalapi.ExternalApiFactory
import com.genexus.android.core.framework.GenexusModule
import com.genexus.android.core.usercontrols.UcFactory
import com.genexus.android.core.usercontrols.UserControlDefinition
import com.genexus.android.location.controls.GxLocationEdit
import com.genexus.android.location.controls.GxSDGeoLocation
import com.genexus.android.location.geolocation.LocationServices
import com.genexus.android.maps.MapViewWrapper
import com.genexus.android.maps.MapsOfflineServices
import com.genexus.android.maps.MapsServices

class LocationModule : GenexusModule {
    override fun initialize(context: Context?) {
        Services.Application.servicesLinker.apply {
            connectLocation(LocationServices(context))
            connectMaps(MapsServices(context), MapsOfflineServices(context))
        }

        ExternalApiFactory.addApi(ExternalApiDefinition(GeoLocationAPI.OBJECT_NAME, GeoLocationAPI::class.java))
        ExternalApiFactory.addApi(ExternalApiDefinition(MapsAPI.OBJECT_NAME, MapsAPI::class.java))
        ExternalApiFactory.addApi(ExternalApiDefinition(MapsOfflineAPI.OBJECT_NAME, MapsOfflineAPI::class.java))

        UcFactory.addControl(UserControlDefinition("SD Maps", MapViewWrapper::class.java))
        UcFactory.addControl(UserControlDefinition("SDGeoLocation", GxSDGeoLocation::class.java))

        TrnHelper.addControl(ControlTypes.LOCATION_CONTROL, GxLocationEdit::class.java)
    }
}
