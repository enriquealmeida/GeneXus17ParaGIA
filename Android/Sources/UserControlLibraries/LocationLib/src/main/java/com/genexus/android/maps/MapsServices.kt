package com.genexus.android.maps

import android.app.Activity
import android.content.Context
import android.view.View
import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.model.EntityFactory
import com.genexus.android.core.base.services.IMaps
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.NameMap
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.controllers.ViewData
import com.genexus.android.core.controls.maps.GxMapViewDefinition
import com.genexus.android.core.controls.maps.common.IMapFeatureFactory
import com.genexus.android.core.controls.maps.common.IMapLocation
import com.genexus.android.core.controls.maps.common.IMapViewFactory
import com.genexus.android.core.controls.maps.common.IMapsProvider
import com.genexus.android.core.controls.maps.common.MapDataBase
import com.genexus.android.location.R

class MapsServices(private val appContext: Context?) : IMaps {

    private var mapProviders = NameMap<IMapsProvider>()
    private var mapViewFactories = mutableMapOf<IMapsProvider, IMapViewFactory<*>>()
    private var mapFeatureFactories = mutableMapOf<IMapsProvider, IMapFeatureFactory<*, *>>()
    private var providerIdOverride: String? = null

    override fun addProvider(provider: IMapsProvider) {
        mapProviders[provider.id] = provider
    }

    override fun getMapViewFactory(): IMapViewFactory<*>? {
        val provider = getProvider() ?: return null
        var factory = mapViewFactories[provider]
        if (factory != null)
            return factory

        factory = provider.mapViewFactory ?: return null
        mapViewFactories[provider] = factory
        return factory
    }

    override fun getMapFeatureFactory(): IMapFeatureFactory<*, *>? {
        val provider = getProvider() ?: return null
        var factory = mapFeatureFactories[provider]
        if (factory != null)
            return factory

        factory = provider.mapFeatureFactory ?: return null
        mapFeatureFactories[provider] = factory
        return factory
    }

    override fun getLocationPickerActivityClass(): Class<out Activity>? {
        return getProvider()?.locationPickerActivityClass
    }

    override fun getMapImageUrl(location: String?, width: Int, height: Int, mapType: String?, zoom: Int, language: String?): String? {
        return getProvider()?.getMapImageUrl(location, width, height, mapType, zoom, language)
    }

    override fun getMapLiteView(location: String?, mapType: String?, zoom: Int): View? {
        return getProvider()?.getMapLiteView(appContext, location, mapType, zoom)
    }

    override fun calculateDirections(
        activity: Activity?,
        origin: String?,
        destination: String?,
        waypoints: List<String>?,
        transportType: String?,
        requestAlternatives: Boolean?
    ): Entity? {
        val provider = getProvider()
        return if (provider != null)
            provider.calculateDirections(activity, origin, destination, waypoints, transportType, requestAlternatives)
        else
            EntityFactory.newEntity()
    }

    override fun getProviderId(): String? {
        return if (Strings.hasValue(providerIdOverride))
            providerIdOverride
        else
            appContext?.resources?.getString(R.string.MapsApi)
    }

    override fun getProvider(): IMapsProvider? {
        val providerId = getProviderId()
        if (Strings.hasValue(providerId)) {
            val provider = mapProviders[providerId]
            if (provider != null)
                return provider
        }

        Services.Log.error(String.format("Unknown value for MapsApi '%s'", providerId))
        return null
    }

    override fun newMapLocation(lat: Double, lng: Double): IMapLocation? {
        return getProvider()?.newMapLocation(lat, lng)
    }

    override fun newMapData(definition: GxMapViewDefinition?, viewData: ViewData?): MapDataBase<*, *, *>? {
        return getProvider()?.newMapData(definition, viewData)
    }

    override fun setProviderIdOverride(providerId: String) {
        providerIdOverride = providerId
    }
}
