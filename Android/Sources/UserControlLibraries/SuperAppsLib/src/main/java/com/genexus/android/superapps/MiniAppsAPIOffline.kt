package com.genexus.android.superapps

import com.genexus.GXBaseCollection
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.superapps.MiniApp
import com.genexus.xml.GXXMLSerializable
import org.json.JSONException
import org.json.JSONObject

object MiniAppsAPIOffline {
    @JvmStatic
    fun load(miniAppInformation: GXXMLSerializable) {
        buildMiniApp(miniAppInformation)?.let { Services.SuperApps.load(it) }
    }

    @JvmStatic
    fun getCached(): GXBaseCollection<*> {
        return Services.SuperApps.getCachedMiniApps().toBaseCollection(Services.Application.get().remoteHandle)
    }

    @JvmStatic
    fun removeCached(id: String, version: Int): Boolean {
        return Services.SuperApps.removeMiniApp(id, version)
    }

    @JvmStatic
    fun clearCached(): Boolean {
        return Services.SuperApps.clearCache()
    }

    private fun buildMiniApp(sdt: GXXMLSerializable): MiniApp? {
        return try {
            val json = JSONObject(sdt.toJSonString())
            MiniApp(json)
        } catch (e: JSONException) {
            Services.Log.error(e)
            null
        }
    }
}
