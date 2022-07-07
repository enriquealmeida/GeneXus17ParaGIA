package com.genexus.android.ar

import com.genexus.android.ar.AugmentedRealityUtils.checkIsSupportedDevice
import com.genexus.android.core.actions.ApiAction
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.externalapi.ExternalApi
import com.genexus.android.core.externalapi.ExternalApiResult

private const val PROPERTY_IS_AVAILABLE = "IsAvailable"
private const val METHOD_PREVIEW_ITEM = "PreviewObject"
private val PERMISSIONS = arrayOf("android.permission.CAMERA")
private const val REQUEST_CODE = 34859

class AugmentedRealityExternalObject(action: ApiAction) : ExternalApi(action) {

    private val methodIsAvailable = IMethodInvoker {
        ExternalApiResult.success(checkIsSupportedDevice(context))
    }

    private val methodPreviewItem = IMethodInvoker { parameters ->
        // It would be easier to use https://developers.google.com/ar/develop/java/scene-viewer
        // but i got a lot of crashes using that, also it has google branding,
        // so we use https://developers.google.com/ar/develop/java/sceneform
        val folderName = Services.Application.get().appId
        val intent = PreviewActivity.newIntent(context, parameters[0] as String, folderName)
        startActivityForResult(intent, REQUEST_CODE)
        ExternalApiResult.SUCCESS_WAIT
    }

    init {
        addReadonlyPropertyHandler(PROPERTY_IS_AVAILABLE, methodIsAvailable)
        addMethodHandlerRequestingPermissions(METHOD_PREVIEW_ITEM, 1, PERMISSIONS, methodPreviewItem)
    }

    companion object {
        const val NAME = "GeneXus.SD.ARPreview"
    }
}
