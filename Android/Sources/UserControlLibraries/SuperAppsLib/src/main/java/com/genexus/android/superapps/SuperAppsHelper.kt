package com.genexus.android.superapps

import android.app.Activity
import android.content.Context
import android.location.Location
import com.genexus.android.core.base.services.ISuperApps
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.GeoFormats
import com.genexus.android.core.common.StorageHelper
import com.genexus.android.core.superapps.MiniApp
import com.genexus.android.core.superapps.MiniAppCachedCollection
import com.genexus.android.core.superapps.MiniAppCollection
import com.genexus.android.core.superapps.errors.LoadError
import com.genexus.android.core.superapps.errors.SearchError
import com.genexus.android.core.tasking.Task
import com.genexus.android.superapps.configuration.SuperAppSettings
import com.genexus.tasks.LoadTask
import com.genexus.tasks.SearchTask
import java.io.File

internal class SuperAppsHelper(private val context: Context) : ISuperApps {

    private val settings = SuperAppSettings(context)

    override fun load(miniApp: MiniApp): Task<Boolean, LoadError> {
        return LoadTask(context, settings.maxCount, settings.maxDays).load(miniApp)
    }

    override fun exit(activity: Activity) {
        if (Services.Application.hasActiveMiniApp())
            activity.startActivity(MiniAppStartupActivity.getExitIntent(context))
    }

    override fun searchByText(text: String, start: Int, count: Int): Task<MiniAppCollection, SearchError> {
        val parameters = mutableMapOf<String, String>().apply {
            put(FIELD_TEXT, text)
            putStartCount(start, count)
        }
        return search(SERVICE_TEXT, parameters)
    }

    override fun searchByLocation(center: Location, radius: Int, start: Int, count: Int): Task<MiniAppCollection, SearchError> {
        val parameters = mutableMapOf<String, String>().apply {
            put(FIELD_CENTER, GeoFormats.buildGeopoint(center.latitude, center.longitude))
            put(FIELD_RADIUS, radius.toString())
            putStartCount(start, count)
        }
        return search(SERVICE_LOCATION, parameters)
    }

    override fun searchByTag(tag: String, start: Int, count: Int): Task<MiniAppCollection, SearchError> {
        val parameters = mutableMapOf<String, String>().apply {
            put(FIELD_TAG, tag)
            putStartCount(start, count)
        }
        return search(SERVICE_TAG, parameters)
    }

    override fun searchFeatured(start: Int, count: Int): Task<MiniAppCollection, SearchError> {
        val parameters = mutableMapOf<String, String>().apply { putStartCount(start, count) }
        return search(SERVICE_FEATURED, parameters)
    }

    override fun getCachedMiniApps(): MiniAppCachedCollection {
        val miniAppCollection = MiniAppCachedCollection()
        val miniAppsDirectory = File(StorageHelper.getMiniAppsDirectoryPath())
        if (!miniAppsDirectory.exists())
            return miniAppCollection

        miniAppsDirectory.listFiles { file -> file.isDirectory }?.forEach { nameDir ->
            nameDir.listFiles { file -> file.isDirectory }?.forEach { versionDir ->
                val miniApp = MiniApp(nameDir.name, versionDir.name.toInt())
                if (miniApp.exists())
                    miniAppCollection.add(miniApp)
            }
        }

        return miniAppCollection
    }

    override fun removeMiniApp(id: String, version: Int): Boolean {
        val miniApp = MiniApp(id, version)
        if (!miniApp.exists())
            return false

        if (!miniApp.delete())
            return false

        val miniAppParentDir = miniApp.getBaseDir().parentFile
        if (miniAppParentDir?.exists() == true && miniAppParentDir.listFiles()?.size == 0)
            miniAppParentDir.delete()

        return true
    }

    override fun clearCache(): Boolean {
        for (miniApp in getCachedMiniApps())
            removeMiniApp(miniApp.id!!, miniApp.version)

        val directory = File(StorageHelper.getMiniAppsDirectoryPath())
        if (!directory.exists())
            return false

        return directory.deleteRecursively()
    }

    private fun search(serviceName: String, parameters: Map<String, String>): Task<MiniAppCollection, SearchError> {
        return SearchTask(serviceName).search(parameters)
    }

    private fun MutableMap<String, String>.putStartCount(start: Int, count: Int) {
        this.apply {
            put(FIELD_START, start.toString())
            put(FIELD_COUNT, count.toString())
        }
    }

    override fun getProvisioningUrl(): String {
        return settings.provisioningUrl ?: throw IllegalStateException("Provisioning URL hasn't been set yet")
    }

    override fun getId(): String {
        return settings.superAppId
    }

    override fun getVersion(): Int {
        return settings.superAppVersion
    }

    companion object {
        private const val FIELD_TEXT = "text"
        private const val FIELD_CENTER = "center"
        private const val FIELD_RADIUS = "radius"
        private const val FIELD_TAG = "tag"
        private const val FIELD_START = "start"
        private const val FIELD_COUNT = "count"

        private const val SERVICE_TEXT = "getbytext"
        private const val SERVICE_LOCATION = "getbylocation"
        private const val SERVICE_TAG = "getbytag"
        private const val SERVICE_FEATURED = "getfeatured"
    }
}
