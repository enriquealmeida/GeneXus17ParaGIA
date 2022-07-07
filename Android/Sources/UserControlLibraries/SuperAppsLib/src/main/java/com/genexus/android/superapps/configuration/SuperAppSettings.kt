package com.genexus.android.superapps.configuration

import android.content.Context
import androidx.core.content.pm.PackageInfoCompat
import com.genexus.android.core.base.metadata.loader.MetadataLoader
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.Strings
import java.io.FileNotFoundException

internal class SuperAppSettings(context: Context) {

    private val nodeObject = try {
        val stream = MetadataLoader.getFromResources(context, CONFIGURATION_FILE)
        val dataInfo = Services.Strings.convertStreamToString(stream)
        if (Strings.hasValue(dataInfo)) Services.Serializer.createNode(dataInfo) else null
    } catch (ex: FileNotFoundException) {
        Services.Log.warning("SuperAppSettings", "SuperApp configuration file not found")
        null
    }

    var provisioningUrl = nodeObject?.getString(PROPERTY_SUPERAPP_URL)
        private set
	
    var superAppId: String = nodeObject?.optString(PROPERTY_SUPERAPP_ID) ?: Strings.EMPTY
        private set

    var superAppVersion = nodeObject?.optInt(PROPERTY_SUPERAPP_VERSION) ?: 0
        private set

    val maxDays = nodeObject?.optInt(PROPERTY_KEEP_FOR_DAYS) ?: 0
    val maxCount = nodeObject?.optInt(PROPERTY_MAX_MINIAPP_COUNT) ?: 0

    init {
        if (provisioningUrl?.endsWith("/") == false)
            provisioningUrl += "/"

        if (superAppId.isEmpty())
            superAppId = context.packageName

        if (superAppVersion == 0)
            superAppVersion = PackageInfoCompat.getLongVersionCode(context.packageManager.getPackageInfo(context.packageName, 0)).toInt()
    }

    companion object {
        private const val CONFIGURATION_FILE = "superapp_json"
        private const val PROPERTY_SUPERAPP_URL = "GXSuperAppProvisioningURL"
        private const val PROPERTY_SUPERAPP_ID = "GXSuperAppId"
        private const val PROPERTY_SUPERAPP_VERSION = "GXSuperAppVersion"
        private const val PROPERTY_MAX_MINIAPP_COUNT = "GXMiniAppCacheMaxCount"
        private const val PROPERTY_KEEP_FOR_DAYS = "GXMiniAppCacheMaxDays"
    }
}
