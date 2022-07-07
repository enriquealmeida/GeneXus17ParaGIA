package com.genexus.android.location

import com.genexus.android.core.actions.ActionExecution
import com.genexus.android.core.actions.ApiAction
import com.genexus.android.core.actions.ExternalObjectEvent
import com.genexus.android.core.activities.IGxActivity
import com.genexus.android.core.base.metadata.expressions.Expression
import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.model.EntityList
import com.genexus.android.core.base.model.ValueCollection
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.controls.maps.common.IOfflineRegionManager
import com.genexus.android.core.controls.maps.common.OnOfflineRegionEventCallback
import com.genexus.android.core.externalapi.ExternalApi
import com.genexus.android.core.externalapi.ExternalApi.IMethodInvoker
import com.genexus.android.core.externalapi.ExternalApiResult

class MapsOfflineAPI(action: ApiAction?) : ExternalApi(action) {

    private val methodDownloadRegion = IMethodInvoker { params ->
        regionManager?.let { manager ->
            val regionName = params[0].toString()
            val minZoom = params[3].toString().toInt()
            val maxZoom = params[4].toString().toInt()
            val density = activity.resources.displayMetrics.density
            if (params[2].toString().startsWith("POINT") && params[2].toString().endsWith(")")) {
                val neGeopoint = params[1].toString()
                val swGeopoint = params[2].toString()
                return@IMethodInvoker ExternalApiResult.success(
                    manager.downloadRegion(
                        regionName, neGeopoint,
                        swGeopoint, minZoom, maxZoom, null, density, offlineRegionEventCallback
                    )
                )
            } else {
                val centerGeopoint = params[1].toString()
                val radius = params[2].toString().toInt()
                return@IMethodInvoker ExternalApiResult.success(
                    manager.downloadRegion(
                        regionName, centerGeopoint,
                        radius, minZoom, maxZoom, null, density, offlineRegionEventCallback
                    )
                )
            }
        }

        ExternalApiResult.FAILURE
    }

    private val methodClearRegion = IMethodInvoker { params ->
        regionManager?.let { manager ->
            val regionName = params[0].toString()
            manager.clearRegion(regionName, updateRegions = true)
            return@IMethodInvoker ExternalApiResult.SUCCESS_CONTINUE
        }

        ExternalApiResult.FAILURE
    }

    private val methodClearAllRegions = IMethodInvoker {
        regionManager?.let { manager ->
            manager.clearAllRegions()
            return@IMethodInvoker ExternalApiResult.SUCCESS_CONTINUE
        }

        ExternalApiResult.FAILURE
    }

    private val propertyDownloadedRegions = IMethodInvoker {
        regionManager?.let { manager ->
            val regionCollection = manager.getDownloadedRegions(offlineRegionEventCallback)
                ?: return@IMethodInvoker ExternalApiResult.SUCCESS_WAIT

            return@IMethodInvoker ExternalApiResult.success(regionCollection)
        }

        ExternalApiResult.FAILURE
    }

    private val propertyIsOfflineRegionsSupported = IMethodInvoker {
        ExternalApiResult.success(Services.MapsOffline.isOfflineGeographicDataSupported())
    }

    private val propertySize = IMethodInvoker {
        regionManager?.let { manager ->
            return@IMethodInvoker ExternalApiResult.success(manager.getSize())
        }

        ExternalApiResult.FAILURE
    }

    private val methodGetRegionStatus = IMethodInvoker { params ->
        regionManager?.let { manager ->
            val regionName = params[0].toString()
            return@IMethodInvoker ExternalApiResult.success(manager.getRegionStatus(regionName))
        }

        ExternalApiResult.FAILURE
    }

    private val methodPauseDownload = IMethodInvoker { params ->
        regionManager?.let { manager ->
            val regionName = params[0].toString()
            return@IMethodInvoker ExternalApiResult.success(manager.pauseDownload(regionName))
        }

        ExternalApiResult.FAILURE
    }

    private val methodCancelDownload = IMethodInvoker { params ->
        regionManager?.let { manager ->
            val regionName = params[0].toString()
            return@IMethodInvoker ExternalApiResult.success(manager.cancelDownload(regionName))
        }

        ExternalApiResult.FAILURE
    }

    private val offlineRegionEventCallback: OnOfflineRegionEventCallback = object :
        OnOfflineRegionEventCallback {
        override fun regionDownloadedSuccessfully(regionName: String, entity: Entity) {
            fireRegionDownloadedEvent(regionName, entity)
        }

        override fun regionDownloadingFailed(regionName: String, entity: Entity) {
            fireRegionDownloadedEvent(regionName, entity)
        }

        override fun regionsListed(regionList: ValueCollection) {
            action?.let {
                if (it.methodName.equals(PROPERTY_DOWNLOADED_REGIONS)) {
                    it.setOutputValue(Expression.Value.newCollection(regionList))
                    ActionExecution.continueCurrent(activity, true, it)
                }
            }
        }

        private fun fireRegionDownloadedEvent(regionName: String, entity: Entity) {
            val event = ExternalObjectEvent(OBJECT_NAME, EVENT_REGION_DOWNLOADING_ENDED)
            val coordinator = event.getFormCoordinatorForEvent(activity as IGxActivity)
            val entities = EntityList().apply { add(entity) }
            coordinator?.let { event.fire(listOf(regionName, entities), it, null) }
        }
    }

    init {
        addReadonlyPropertyHandler(PROPERTY_IS_OFFLINE_SUPPORTED, propertyIsOfflineRegionsSupported)
        addReadonlyPropertyHandler(PROPERTY_DOWNLOADED_REGIONS, propertyDownloadedRegions)
        addReadonlyPropertyHandler(PROPERTY_SIZE, propertySize)
        addMethodHandler(METHOD_DOWNLOAD_REGION, 5, methodDownloadRegion)
        addMethodHandler(METHOD_DOWNLOAD_REGION_RADIAL, 5, methodDownloadRegion)
        addMethodHandler(METHOD_CLEAR_REGION, 1, methodClearRegion)
        addMethodHandler(METHOD_CLEAR_REGIONS, 0, methodClearAllRegions)
        addMethodHandler(METHOD_GET_REGION_STATUS, 1, methodGetRegionStatus)
        addMethodHandler(METHOD_PAUSE_DOWNLOAD, 1, methodPauseDownload)
        addMethodHandler(METHOD_CANCEL_DOWNLOAD, 1, methodCancelDownload)

        regionManager = Services.MapsOffline.getInstance()
    }

    companion object {
        const val OBJECT_NAME = "GeneXus.SD.MapsOffline"
        private const val PROPERTY_IS_OFFLINE_SUPPORTED = "IsOfflineGeographicDataSupported"
        private const val PROPERTY_DOWNLOADED_REGIONS = "DownloadedRegions"
        private const val PROPERTY_SIZE = "Size"
        private const val METHOD_DOWNLOAD_REGION = "DownloadRegion"
        private const val METHOD_DOWNLOAD_REGION_RADIAL = "DownloadRegionRadial"
        private const val METHOD_CLEAR_REGION = "ClearRegion"
        private const val METHOD_CLEAR_REGIONS = "ClearRegions"
        private const val METHOD_GET_REGION_STATUS = "GetRegionStatus"
        private const val METHOD_PAUSE_DOWNLOAD = "PauseRegionDownload"
        private const val METHOD_CANCEL_DOWNLOAD = "CancelRegionDownload"
        private const val EVENT_REGION_DOWNLOADING_ENDED = "RegionDownloadEnded"
        private var regionManager: IOfflineRegionManager? = null
    }
}
