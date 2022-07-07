package com.genexus.android.qrscanner

import android.Manifest
import android.app.Activity
import android.content.Context
import com.genexus.android.WithPermission
import com.genexus.android.core.base.metadata.expressions.Expression
import com.genexus.android.core.base.metadata.layout.ControlInfo
import com.genexus.android.core.base.model.ValueCollection
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.Strings

class QRScannerHelper(context: Context, controlInfo: ControlInfo?) {
    private val operationMode = controlInfo?.optStringProperty("@ScannerOperationMode")
    val loop = Strings.hasValue(operationMode) && operationMode == "ContinuousRead"
    val beep = controlInfo?.optBooleanProperty("@ScannerBeep") ?: false
    val barcodeTypes = mutableListOf<String>()
    val intent = QRScannerActivity.newIntent(context, loop, beep, toValueCollection(barcodeTypes))

    fun permissionRequest(activity: Activity, onSuccess: Runnable, onFailure: Runnable?): WithPermission<Void> {
        return WithPermission.Builder<Void>(activity)
            .require(REQUIRED_PERMISSIONS)
            .setRequestCode(PERMISSION_REQUEST_CODE)
            .attachToActivityController()
            .onSuccess(onSuccess)
            .onFailure(onFailure)
            .build()
    }

    init {
        controlInfo?.optStringProperty("@ScannerBarcodeTypes")?.let {
            for (type in Services.Strings.split(it, ","))
                barcodeTypes.add(type.trim())
        }
    }

    private fun toValueCollection(list: MutableList<String>): ValueCollection {
        val ret = ValueCollection(Expression.Type.STRING)
        for (item in list)
            ret.add(item)

        return ret
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val PERMISSION_REQUEST_CODE = 25232
    }
}
