package com.genexus.android.core.superapps

import com.genexus.GXBaseCollection
import com.genexus.android.core.base.metadata.expressions.Expression
import com.genexus.android.core.base.model.EntityFactory
import com.genexus.android.core.base.model.EntityList
import com.genexus.android.core.utils.GXInstanceFactory

class MiniAppCachedCollection : ArrayList<MiniApp>() {
    fun toEntityList(): EntityList {
        val entityList = EntityList().apply { itemType = Expression.Type.SDT }
        if (isEmpty())
            return entityList

        for (miniApp in this) {
            val entity = EntityFactory.newSdt(SDT_CACHED_MINIAPP).apply {
                setProperty(SDT_CACHED_MINIAPP_ID, miniApp.id)
                setProperty(SDT_CACHED_MINIAPP_VERSION, miniApp.version)
                setProperty(SDT_CACHED_MINIAPP_CREATED_AT, miniApp.creationDate.toString())
                setProperty(SDT_CACHED_MINIAPP_LAST_USED, miniApp.lastUsedDate.toString())
            }
            entityList.addEntity(entity)
        }

        return entityList
    }

    fun toBaseCollection(remoteHandle: Int): GXBaseCollection<*> {
        return newMiniAppCollectionSDT(remoteHandle).apply { this.fromJSonString(toEntityList().toString()) }
    }

    private fun newMiniAppCollectionSDT(remoteHandle: Int): GXBaseCollection<*> {
        return try {
            GXInstanceFactory.getGXBaseCollectionInstance(
                "com.superappsmodule.genexussuperapps.SdtCachedMiniApp",
                "CachedMiniApp", "CachedMiniApp", remoteHandle
            )
        } catch (ex: IllegalStateException) {
            GXInstanceFactory.getGXBaseCollectionInstance(
                "com.coremodules.genexus.common.SdtCachedMiniApp",
                "CachedMiniApp", "CachedMiniApp", remoteHandle
            )
        }
    }

    companion object {
        private const val SDT_CACHED_MINIAPP = "GeneXusSuperApps.CachedMiniApp"
        private const val SDT_CACHED_MINIAPP_ID = "MiniAppId"
        private const val SDT_CACHED_MINIAPP_VERSION = "MiniAppVersion"
        private const val SDT_CACHED_MINIAPP_CREATED_AT = "CreationDatetime"
        private const val SDT_CACHED_MINIAPP_LAST_USED = "LastUsedDatetime"
    }
}
