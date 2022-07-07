package com.genexus.android.qrscanner

import android.content.Context
import com.genexus.android.core.externalapi.ExternalApiDefinition
import com.genexus.android.core.externalapi.ExternalApiFactory
import com.genexus.android.core.framework.GenexusModule
import com.genexus.android.core.usercontrols.UcFactory
import com.genexus.android.core.usercontrols.UserControlDefinition

class QRScannerModule : GenexusModule {
    override fun initialize(context: Context) {
        val sdScannerExternalObject = ExternalApiDefinition(
            QRScannerExternalObject.NAME,
            QRScannerExternalObject::class.java
        )
        ExternalApiFactory.addApi(sdScannerExternalObject)
        UcFactory.addControl(UserControlDefinition(QRScannerControl.NAME, QRScannerControl::class.java))
        UcFactory.addControl(UserControlDefinition(QRScannerInlineControl.NAME, QRScannerInlineControl::class.java))
    }
}
