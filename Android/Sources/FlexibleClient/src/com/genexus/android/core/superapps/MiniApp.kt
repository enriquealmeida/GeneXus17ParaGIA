package com.genexus.android.core.superapps

import com.genexus.android.core.base.metadata.GenexusApplication
import com.genexus.android.core.base.metadata.InstanceProperties
import com.genexus.android.core.base.metadata.enums.Connectivity
import com.genexus.android.core.base.metadata.loader.MetadataLoader
import com.genexus.android.core.base.metadata.loader.MetadataParser
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.common.StorageHelper
import com.genexus.android.json.NodeObject
import org.json.JSONObject
import java.io.File
import java.io.Serializable
import java.net.URI
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class MiniApp() : GenexusApplication(false), Serializable {
    constructor(jsonObject: JSONObject?) : this (jsonObject, false)
    constructor(jsonObject: JSONObject?, prependProvisioningUrl: Boolean) : this() { fromJson(jsonObject, prependProvisioningUrl) }
    constructor(properties: InstanceProperties) : this() { fromProperties(properties) }
    constructor(id: String, version: Int) : this() {
        this.id = id
        this.version = version
    }

    var id: String? = null
        set(value) {
            field = value
            creationDate = getDate(PREFERENCES_CREATED_AT)
            lastUsedDate = getDate(PREFERENCES_LAST_USED)
        }
    var description: String? = null
    var signature: String? = null
    var iconUrl: String? = null
    var bannerUrl: String? = null
    var cardUrl: String? = null
    var metadataRemoteUrl: String? = null
    var metadataLocalFile: File? = null
    var version = INVALID_VERSION
        set(value) {
            field = value
            majorVersion = value
        }

    private val sharedPreferences by lazy { Services.Preferences.getAppSharedPreferences(this) }

    var creationDate: LocalDateTime? = null
    var lastUsedDate: LocalDateTime? = null

    fun refresh() {
        name = appEntry
        appEntry = MetadataLoader.getObjectName(appEntry)
        fromFile()?.let { fromProperties(it) }
    }

    fun exists(): Boolean {
        return getBaseDir().exists() && getExtractedVersion() != INVALID_VERSION
    }

    fun delete(): Boolean {
        val deleted = exists() && getBaseDir().deleteRecursively()
        if (deleted) sharedPreferences.edit().clear().apply()
        return deleted
    }

    fun getBaseDir(): File {
        return File(StorageHelper.getMiniAppsDirectoryPath() + id + File.separator + version.toString())
    }

    private fun fromFile(): InstanceProperties? {
        val resourceName = String.format("%s.properties", Strings.toLowerCase(appEntry))
        val json = MetadataLoader.getMiniAppDefinition(resourceName, this, true)
        if (json == null) {
            Services.Log.info("MiniApp has not been downloaded yet")
            return null
        }

        val jsonProperties = json.getNode("properties")
        if (jsonProperties == null) {
            Services.Log.warning(String.format("%s file has no 'properties' section", appEntry))
            return null
        }

        return InstanceProperties().apply { deserialize(jsonProperties) }
    }

    private fun fromJson(jsonObject: JSONObject?, prependProvisioningUrl: Boolean) {
        jsonObject?.let {
            id = it.optString(FIELD_ID)
            description = it.optString(FIELD_DESCRIPTION)
            signature = it.optString(FIELD_SIGNATURE)
            name = it.optString(FIELD_NAME)
            apiUri = it.optString(FIELD_SERVICES_URL)
            appEntry = it.optString(FIELD_ENTRY_POINT)
            version = it.optInt(FIELD_VERSION)

            val icon = it.optString(FIELD_ICON)
            val banner = it.optString(FIELD_BANNER)
            val card = it.optString(FIELD_CARD)
            val metadataRemote = it.optString(FIELD_METADATA)

            if (prependProvisioningUrl) {
                iconUrl = prependProvisioningUrl(icon)
                bannerUrl = prependProvisioningUrl(banner)
                cardUrl = prependProvisioningUrl(card)
                metadataRemoteUrl = prependProvisioningUrl(metadataRemote)
            } else {
                iconUrl = icon
                bannerUrl = banner
                cardUrl = card
                metadataRemoteUrl = metadataRemote
            }
        }
    }

    private fun fromProperties(properties: InstanceProperties) {
        setIsSecure(properties.optBooleanProperty("EnableIntegratedSecurity"))
        clientId = properties.optStringProperty("IntegratedSecurityObjClientID")
        enableAnonymousUser = properties.optBooleanProperty("IntegratedSecurityAnonymous")
        loginObject = properties.optStringProperty("IntegratedSecurityLoginSD")
        notAuthorizedObject = properties.optStringProperty("IntegratedSecurityNotAuthorizedSD")
        changePasswordObject = properties.optStringProperty("IntegratedSecurityChangePasswordObjectSD")
        useAds = properties.optBooleanProperty("AppEnableAds")
    }

    private fun fromServer() {
        val server = Services.HttpService.getJSONFromUrl("$apiUri$SD_APPS_JSON")
        if (server == null) {
            Services.Log.error(String.format("sdapps.json file not found at %s", apiUri))
            return
        }

        val apps = server.optJSONArray("apps")
        if (apps == null) {
            Services.Log.error(String.format("%s file has no 'apps' section", name))
            return
        }

        for (i in 0 until apps.length()) {
            val app = apps.optJSONObject(i) ?: continue
            val prop = app.optJSONObject("properties") ?: continue
            val connectivity = MetadataParser.readConnectivity(NodeObject(prop))
            val entryPoint = app.optString("n")
            if (connectivity == Connectivity.Offline || !name.endsWith(entryPoint, ignoreCase = true)) continue

            this.appEntry = entryPoint
            this.setIsSecure(prop.optBoolean("EnableIntegratedSecurity", false))
            this.clientId = prop.optString("IntegratedSecurityObjClientID")
            this.enableAnonymousUser = prop.optBoolean("IntegratedSecurityAnonymous", false)
            this.loginObject = prop.optString("IntegratedSecurityLoginSD")
            this.notAuthorizedObject = prop.optString("IntegratedSecurityNotAuthorizedSD")
            this.changePasswordObject = prop.optString("IntegratedSecurityChangePasswordObjectSD")
            this.useAds = prop.optBoolean("AppEnableAds", false)
            return
        }

        Services.Log.error(String.format("MiniApp download from server failed - Main '%s' not found at '%s'", name, apiUri))
    }

    private fun prependProvisioningUrl(value: String?): String? {
        if (value.isNullOrEmpty() || URI(value).isAbsolute)
            return value

        return Services.SuperApps.getProvisioningUrl() + value
    }

    fun setExtractedVersion(version: Int) {
        sharedPreferences.edit().apply {
            putInt(PREFERENCES_EXTRACTED_VERSION, version)
            apply()
        }
    }

    fun getExtractedVersion(): Int { return sharedPreferences.getInt(PREFERENCES_EXTRACTED_VERSION, INVALID_VERSION) }

    fun setCreatedAt() { setCreatedAt(now()) }
    fun setCreatedAt(date: LocalDateTime) { setDate(date, PREFERENCES_CREATED_AT) }

    fun setLastUsed() { setLastUsed(now()) }
    fun setLastUsed(date: LocalDateTime) { setDate(date, PREFERENCES_LAST_USED) }

    private fun setDate(date: LocalDateTime, key: String) {
        sharedPreferences.edit().apply {
            putString(key, date.toString())
            apply()
        }
    }

    private fun getDate(key: String): LocalDateTime? {
        val dateString = sharedPreferences.getString(key, null) ?: return null
        return LocalDateTime.parse(dateString)
    }

    private fun now(): LocalDateTime {
        return LocalDateTime.now(ZoneId.systemDefault().rules.getOffset(Instant.now()))
    }

    companion object {
        private const val SD_APPS_JSON = "/gxmetadata/sdapps.json"
        private const val PREFERENCES_EXTRACTED_VERSION = "extracted-version"
        private const val PREFERENCES_CREATED_AT = "created-at"
        private const val PREFERENCES_LAST_USED = "used-at"

        const val FIELD_ID = "Id"
        const val FIELD_NAME = "Name"
        const val FIELD_DESCRIPTION = "Description"
        const val FIELD_ICON = "Icon"
        const val FIELD_BANNER = "Banner"
        const val FIELD_CARD = "Card"
        const val FIELD_METADATA = "Metadata"
        const val FIELD_ENTRY_POINT = "EntryPoint"
        const val FIELD_SERVICES_URL = "ServicesURL"
        const val FIELD_SIGNATURE = "Signature"
        const val FIELD_VERSION = "Version"

        const val INVALID_VERSION = -1
    }
}
