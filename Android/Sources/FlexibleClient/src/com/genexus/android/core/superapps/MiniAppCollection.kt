package com.genexus.android.core.superapps

import com.genexus.GXBaseCollection
import com.genexus.android.core.base.metadata.expressions.Expression
import com.genexus.android.core.base.model.EntityFactory
import com.genexus.android.core.base.model.EntityList
import com.genexus.android.core.utils.GXInstanceFactory

class MiniAppCollection : ArrayList<MiniApp>() {
    fun toEntityList(): EntityList {
        val entityList = EntityList().apply { itemType = Expression.Type.SDT }
        if (isEmpty())
            return entityList

        for (miniApp in this) {
            val entity = EntityFactory.newSdt(SDT_MINIAPP_INFO_COLLECTION).apply {
                setProperty(MiniApp.FIELD_ID, miniApp.id)
                setProperty(MiniApp.FIELD_NAME, miniApp.name)
                setProperty(MiniApp.FIELD_DESCRIPTION, miniApp.description)
                setProperty(MiniApp.FIELD_ICON, miniApp.iconUrl)
                setProperty(MiniApp.FIELD_BANNER, miniApp.bannerUrl)
                setProperty(MiniApp.FIELD_CARD, miniApp.cardUrl)
                setProperty(MiniApp.FIELD_METADATA, miniApp.metadataRemoteUrl)
                setProperty(MiniApp.FIELD_ENTRY_POINT, miniApp.appEntry)
                setProperty(MiniApp.FIELD_SERVICES_URL, miniApp.apiUri)
                setProperty(MiniApp.FIELD_SIGNATURE, miniApp.signature)
                setProperty(MiniApp.FIELD_VERSION, miniApp.version)
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
                "com.superappsmodule.genexussuperapps.SdtMiniAppInformation",
                "MiniAppInformation", "MiniApp", remoteHandle
            )
        } catch (ex: IllegalStateException) {
            GXInstanceFactory.getGXBaseCollectionInstance(
                "com.coremodules.genexus.common.SdtMiniAppInformation",
                "MiniAppInformation", "MiniApp", remoteHandle
            )
        }
    }

    companion object {
        private const val SDT_MINIAPP_INFO_COLLECTION = "GeneXusSuperApps.MiniAppInformation"
    }
}
