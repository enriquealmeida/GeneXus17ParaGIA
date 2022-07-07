package com.genexus.android.controls.maps.mapbox

import android.content.Context
import com.genexus.android.core.activities.ActivityHelper
import com.genexus.android.core.base.metadata.expressions.Expression
import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.model.EntityFactory
import com.genexus.android.core.base.model.ValueCollection
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.GeoFormats
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.common.StorageHelper
import com.genexus.android.core.controls.maps.common.IOfflineRegionManager
import com.genexus.android.core.controls.maps.common.OnOfflineRegionEventCallback
import com.genexus.android.maps.OfflineRegionDefinition
import com.genexus.android.maps.OfflineRegionStatus
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.offline.OfflineManager
import com.mapbox.mapboxsdk.offline.OfflineManager.ListOfflineRegionsCallback
import com.mapbox.mapboxsdk.offline.OfflineRegion
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition
import com.mapbox.mapboxsdk.plugins.offline.model.NotificationOptions
import com.mapbox.mapboxsdk.plugins.offline.model.OfflineDownloadOptions
import com.mapbox.mapboxsdk.plugins.offline.offline.OfflineDownloadChangeListener
import com.mapbox.mapboxsdk.plugins.offline.offline.OfflinePlugin
import com.mapbox.mapboxsdk.plugins.offline.utils.OfflineUtils
import org.json.JSONException
import org.json.JSONObject
import java.io.File

class OfflineRegionManager private constructor(private val context: Context) : IOfflineRegionManager {

    private val plugin: OfflinePlugin = OfflinePlugin.getInstance(context)
    private val manager: OfflineManager = OfflineManager.getInstance(context)
    private var downloadedRegions: HashMap<String, OfflineRegionDefinition> = hashMapOf()
    private var callback: OnOfflineRegionEventCallback? = null
    private var regionDownloadToCancel = Strings.EMPTY
    private var regionsListed = false

    override fun downloadRegion(
        regionName: String?,
        centerGeopoint: String?,
        radius: Int,
        minZoom: Int,
        maxZoom: Int,
        style: String?,
        density: Float,
        listener: OnOfflineRegionEventCallback?
    ): Boolean {

        if (radius < 1) {
            Services.Log.error(String.format("Invalid radius value %s", radius))
            return false
        }

        val centerPair = GeoFormats.parseGeopoint(centerGeopoint)
        if (centerPair == null) {
            Services.Log.error(String.format("Invalid Center '%s' coordinates", centerGeopoint))
            return false
        }

        val mapUtils = MapUtils(null)
        val mapBounds = mapUtils.getBoundingBox(MapLocation(centerPair.first, centerPair.second), radius.toDouble())
        if (mapBounds == null) {
            Services.Log.error("Can't build region bounding box")
            return false
        }

        val latLngNE = mapBounds.northeast().latLng
        val latLngSW = mapBounds.southwest().latLng

        return downloadRegion(regionName, centerGeopoint, radius, latLngNE, latLngSW, minZoom, maxZoom, style, density, listener)
    }

    override fun downloadRegion(
        regionName: String?,
        northEastGeopoint: String?,
        southWestGeopoint: String?,
        minZoom: Int,
        maxZoom: Int,
        style: String?,
        density: Float,
        listener: OnOfflineRegionEventCallback?
    ): Boolean {

        val pairNE = GeoFormats.parseGeopoint(northEastGeopoint)
        val pairSW = GeoFormats.parseGeopoint(southWestGeopoint)
        if (pairNE == null || pairSW == null) {
            Services.Log.error(String.format("Invalid NE '%s' or SW '%s' coordinates", northEastGeopoint, southWestGeopoint))
            return false
        }

        val latLngNE = LatLng(pairNE.first, pairNE.second)
        val latLngSW = LatLng(pairSW.first, pairSW.second)

        return downloadRegion(
            regionName, null, 0, latLngNE, latLngSW, minZoom, maxZoom, style,
            density, listener
        )
    }

    private fun downloadRegion(
        regionName: String?,
        centerGeopoint: String?,
        radius: Int,
        latLngNE: LatLng?,
        latLngSW: LatLng?,
        _minZoom: Int,
        _maxZoom: Int,
        mapStyleUri: String?,
        density: Float,
        listener: OnOfflineRegionEventCallback?
    ): Boolean {

        if (regionName.isNullOrEmpty()) {
            Services.Log.error("Region name cannot be null or empty")
            return false
        }

        if (latLngNE == null || latLngSW == null) {
            Services.Log.error(String.format("Invalid NorthEast or SouthWest coordinates"))
            return false
        }

        var minZoom = _minZoom
        var maxZoom = _maxZoom
        if (minZoom > maxZoom) {
            Services.Log.error(String.format("Invalid zoom values %s-%s", minZoom, maxZoom))
            return false
        }

        if (minZoom < 1) minZoom = 1
        if (maxZoom > 22) maxZoom = 22

        if (isRegionDownloaded(regionName)) {
            Services.Log.info("Region $regionName already exists")
            return false
        }

        var style = mapStyleUri
        if (style.isNullOrEmpty()) {
            style = DEFAULT_MAP_STYLE
            Services.Log.info("Map style is empty, using default one: $style")
        }

        plugin.addOfflineDownloadStateChangeListener(offlineDownloadChangeListener)
        callback = listener

        val regionDefinition = OfflineTilePyramidRegionDefinition(
            style,
            LatLngBounds.Builder()
                .include(latLngNE)
                .include(latLngSW)
                .build(),
            minZoom.toDouble(),
            maxZoom.toDouble(),
            density
        )

        // Customize the download notification's appearance
        val notificationOptions = NotificationOptions.builder(context)
            .contentTitle(String.format("Downloading %s region", regionName))
            .smallIconRes(R.drawable.appicon)
            .returnActivity(ActivityHelper.getCurrentActivity().javaClass.name)
            .build()

        // Start downloading the map tiles for offline use
        plugin.startDownload(
            OfflineDownloadOptions.builder()
                .definition(regionDefinition)
                .metadata(buildMetadata(regionName, centerGeopoint, latLngNE, latLngSW, radius, minZoom, maxZoom, OfflineRegionStatus.QUEUED))
                .notificationOptions(notificationOptions)
                .build()
        )

        return true
    }

    override fun getRegionStatus(regionName: String?): Entity {
        if (!regionName.isNullOrEmpty()) {
            downloadedRegions[regionName]?.let { return regionToEntity(it) }
        }

        return newOfflineRegionEntity()
    }

    override fun getSize(): Double {
        return File(StorageHelper.getApplicationDataPath(), DATABASE_FILE_NAME).sizeKB
    }

    private val File.sizeKB get() = if (!exists()) 0.0 else length().toDouble() / 1024

    private fun updateRegionStatus(regionName: String, status: Int) {
        downloadedRegions[regionName]?.status = status
    }

    override fun getDownloadedRegions(callback: OnOfflineRegionEventCallback?): ValueCollection? {
        if (regionsListed) {
            val regionEntities = ValueCollection(Expression.Type.STRING)

            for (region in downloadedRegions)
                regionEntities.add(region.value.regionName)

            return regionEntities
        }

        this.callback = callback
        updateDownloadedRegions()
        return null
    }

    override fun clearRegion(regionName: String?, updateRegions: Boolean) {
        downloadedRegions[regionName]?.let {
            val region = it.regionObject as OfflineRegion
            region.delete(regionDeletedListener)
            updateRegionStatus(getIdFromMetadata(region.metadata), OfflineRegionStatus.CANCELED)

            if (updateRegions)
                updateDownloadedRegions()
        }
    }

    override fun clearAllRegions() {
        for (region in downloadedRegions)
            clearRegion(region.key, updateRegions = false)

        manager.clearAmbientCache(object : OfflineManager.FileSourceCallback {
            override fun onSuccess() {
                Services.Log.info("Offline Database cleared")
            }

            override fun onError(error: String) {
                Services.Log.error("Offline Database couldn't be cleared: $error")
            }
        })

        downloadedRegions.clear()
        updateDownloadedRegions()
    }

    private fun isRegionDownloaded(regionName: String?): Boolean {
        if (regionName.isNullOrEmpty())
            return false

        return downloadedRegions.containsKey(regionName)
    }

    override fun pauseDownload(regionName: String?) {
        Services.Log.error("Pausing region download is not supported")
    }

    override fun cancelDownload(regionName: String?) {
        if (!regionName.isNullOrEmpty())
            regionDownloadToCancel = regionName
    }

    private fun updateDownloadedRegions() {
        manager.listOfflineRegions(object : ListOfflineRegionsCallback {
            override fun onList(offlineRegions: Array<OfflineRegion>) {
                setDownloadedRegions(offlineRegions)
                regionsListed = true
            }

            override fun onError(error: String) {
                Services.Log.error("Couldn't retrieve offline regions: $error")
                regionsListed = true
            }
        })
    }

    private fun setDownloadedRegions(offlineRegions: Array<OfflineRegion>) {
        if (offlineRegions.isEmpty())
            downloadedRegions.clear()

        for (region in offlineRegions) {
            val offlineRegionDefinition = parseRegion(region)
            offlineRegionDefinition?.let {
                val regionName = it.regionName
                downloadedRegions[regionName] = it
                updateRegionStatus(regionName, OfflineRegionStatus.DOWNLOADED)
            }
        }

        callback?.let {
            val regionCollection = ValueCollection(Expression.Type.STRING)
            for (region in downloadedRegions)
                regionCollection.add(region.value.regionName)

            it.regionsListed(regionCollection)
            callback = null
        }
    }

    private fun regionToEntity(region: OfflineRegionDefinition): Entity {
        val entity = newOfflineRegionEntity()

        entity.setProperty("Name", region.regionName)
        entity.setProperty("Status", region.status)
        entity.setProperty("MinZoom", region.minZoom)
        entity.setProperty("MaxZoom", region.maxZoom)

        if (region.center.isNullOrEmpty()) {
            entity.setProperty("NorthEast", region.northEast)
            entity.setProperty("SouthWest", region.southWest)
        } else {
            entity.setProperty("CenterCoordinate", region.center)
            entity.setProperty("Radius", region.radius)
        }

        return entity
    }

    private fun newOfflineRegionEntity(): Entity {
        return EntityFactory.newSdt("GeneXus.SD.MapRegion")
    }

    private fun newMessagesEntity(error: String?, message: String?, type: Int): Entity {
        val entity = EntityFactory.newSdt("GeneXus.Common.Messages", "Message")
        val description = if (error.isNullOrEmpty()) message else "$error: $message"
        entity.setProperty("Type", type)
        entity.setProperty("Description", description)
        return entity
    }

    private val regionDeletedListener: OfflineRegion.OfflineRegionDeleteCallback = object : OfflineRegion.OfflineRegionDeleteCallback {
        override fun onDelete() {
            Services.Log.info("Offline region successfully deleted")
        }

        override fun onError(error: String?) {
            Services.Log.error("Couldn't delete offline region: $error")
        }
    }

    private val offlineDownloadChangeListener: OfflineDownloadChangeListener = object : OfflineDownloadChangeListener {
        override fun onCreate(offlineDownload: OfflineDownloadOptions) {
            val regionId = getIdFromMetadata(offlineDownload.metadata())
            updateRegionStatus(regionId, OfflineRegionStatus.DOWNLOADING)
            Services.Log.debug("Region download started: $regionId")
        }

        override fun onSuccess(offlineDownload: OfflineDownloadOptions) {
            val region = parseMetadata(offlineDownload.metadata())
            updateDownloadedRegions()
            region?.let {
                callback?.regionDownloadedSuccessfully(
                    region.regionName,
                    newMessagesEntity(Strings.EMPTY, "Download finished successfully", 2)
                )
                callback = null
            }

            Services.Log.info("Region download finished successfully: $region")
        }

        override fun onCancel(offlineDownload: OfflineDownloadOptions?) {
            onFailed(offlineDownload, "Canceled", Strings.EMPTY)
        }

        override fun onError(offlineDownload: OfflineDownloadOptions?, error: String?, message: String?) {
            onFailed(offlineDownload, error, message)
        }

        private fun onFailed(offlineDownload: OfflineDownloadOptions?, error: String?, message: String?) {
            var regionId = Strings.EMPTY
            offlineDownload?.let {
                val region = parseMetadata(it.metadata())
                regionId = region?.regionName ?: Strings.EMPTY
                updateRegionStatus(regionId, OfflineRegionStatus.CANCELED)
                region?.let {
                    callback?.regionDownloadingFailed(
                        region.regionName,
                        newMessagesEntity(error, message, 1)
                    )
                    callback = null
                }
            }

            Services.Log.error("Region $regionId download failed: $error - $message")
        }

        override fun onProgress(offlineDownload: OfflineDownloadOptions, progress: Int) {
            val regionId = getIdFromMetadata(offlineDownload.metadata())
            Services.Log.debug("Region $regionId download progress: $progress")
            if (regionDownloadToCancel.isNotEmpty() && regionId == regionDownloadToCancel) {
                plugin.cancelDownload(offlineDownload)
                regionDownloadToCancel = Strings.EMPTY
            }
        }
    }

    private fun getIdFromMetadata(metadata: ByteArray): String {
        return OfflineUtils.convertRegionName(metadata)
    }

    private fun buildMetadata(
        regionName: String,
        centerGeopoint: String?,
        latLngNE: LatLng,
        latLngSW: LatLng,
        radius: Int,
        minZoom: Int,
        maxZoom: Int,
        status: Int
    ): ByteArray {

        var metadata: ByteArray = OfflineUtils.convertRegionName(regionName)

        var geopointNE = Strings.EMPTY
        var geopointSW = Strings.EMPTY
        var geopointCenter = Strings.EMPTY

        if (centerGeopoint.isNullOrEmpty()) {
            geopointNE = GeoFormats.buildGeopoint(latLngNE.latitude, latLngNE.longitude)
            geopointSW = GeoFormats.buildGeopoint(latLngSW.latitude, latLngSW.longitude)
        } else {
            geopointCenter = centerGeopoint
        }

        try {
            val jsonObject = JSONObject().apply {
                put(JSON_FIELD_REGION_NAME, regionName)
                put(JSON_FIELD_RADIUS, radius)
                put(JSON_FIELD_CENTER, geopointCenter)
                put(JSON_FIELD_NORTHEAST, geopointNE)
                put(JSON_FIELD_SOUTHWEST, geopointSW)
                put(JSON_FIELD_MIN_ZOOM, minZoom)
                put(JSON_FIELD_MAX_ZOOM, maxZoom)
                put(JSON_FIELD_STATUS, status)
            }

            val json: String = jsonObject.toString()
            metadata = json.toByteArray(Charsets.UTF_8)
        } catch (exception: Exception) {
            Services.Log.error("Failed to encode metadata", exception)
        }

        return metadata
    }

    private fun parseRegion(region: OfflineRegion): OfflineRegionDefinition? {
        return parseMetadata(region.metadata)?.apply { regionObject = region }
    }

    private fun parseMetadata(metadata: ByteArray): OfflineRegionDefinition? {
        try {
            val json = String(metadata, Charsets.UTF_8)
            val jsonObject = JSONObject(json)
            val regionName = jsonObject.getString(JSON_FIELD_REGION_NAME)
            val northEast = jsonObject.getString(JSON_FIELD_NORTHEAST)
            val southWest = jsonObject.getString(JSON_FIELD_SOUTHWEST)
            val center = jsonObject.getString(JSON_FIELD_CENTER)
            val radius = jsonObject.getInt(JSON_FIELD_RADIUS)
            val minZoom = jsonObject.getInt(JSON_FIELD_MIN_ZOOM)
            val maxZoom = jsonObject.getInt(JSON_FIELD_MAX_ZOOM)
            val status = jsonObject.getInt(JSON_FIELD_STATUS)
            return OfflineRegionDefinition(
                regionName, center, northEast, southWest,
                radius, minZoom, maxZoom, status, null
            )
        } catch (exception: JSONException) {
            Services.Log.error("An error occurred parsing region metadata", exception)
        }

        return null
    }

    companion object {
        private const val JSON_FIELD_REGION_NAME = "FIELD_REGION_NAME"
        private const val JSON_FIELD_CENTER = "FIELD_CENTER"
        private const val JSON_FIELD_NORTHEAST = "FIELD_NORTHEAST"
        private const val JSON_FIELD_SOUTHWEST = "FIELD_SOUTHWEST"
        private const val JSON_FIELD_RADIUS = "FIELD_RADIUS"
        private const val JSON_FIELD_MIN_ZOOM = "FIELD_MIN_ZOOM"
        private const val JSON_FIELD_MAX_ZOOM = "FIELD_MAX_ZOOM"
        private const val JSON_FIELD_STATUS = "FIELD_STATUS"
        private const val DATABASE_FILE_NAME = "mbgl-offline.db"
        private const val DEFAULT_MAP_STYLE = "mapbox://styles/mapbox/streets-v11"

        private var instance: OfflineRegionManager? = null

        @Synchronized
        fun getInstance(context: Context): OfflineRegionManager {
            if (instance == null)
                instance = OfflineRegionManager(context)

            return instance!!
        }
    }

    init {
        updateDownloadedRegions()
    }
}
