package com.genexus.android.qrscanner

import android.app.Activity
import android.content.Intent
import com.genexus.android.core.actions.ApiAction
import com.genexus.android.core.base.metadata.enums.RequestCodes
import com.genexus.android.core.base.model.EntityFactory
import com.genexus.android.core.base.model.ValueCollection
import com.genexus.android.core.externalapi.ExternalApi
import com.genexus.android.core.externalapi.ExternalApiResult

class QRScannerExternalObject(action: ApiAction) : ExternalApi(action) {

    companion object {
        const val NAME = "GeneXus.SD.Scanner"
        const val VALUE_SEPARATOR = ";;"
        private val PERMISSIONS = arrayOf("android.permission.CAMERA")
        private const val METHOD_SCAN = "ScanBarcode"
        private const val METHOD_SCAN_LOOP = "ScanInLoop"
        private const val REQUEST_CODE = RequestCodes.ACTIONNOREFRESH.toInt()
    }

    private val methodScan = object : IMethodInvokerWithActivityResult {

        override fun invoke(parameters: List<Any>): ExternalApiResult {
            val barcodeTypes = if (parameters.size == 1) (parameters[0] as ValueCollection) else null
            val intent = QRScannerActivity.newIntent(context, barcodeTypes = barcodeTypes)
            startActivityForResult(intent, REQUEST_CODE)
            return ExternalApiResult.SUCCESS_WAIT
        }

        override fun handleActivityResult(requestCode: Int, resultCode: Int, result: Intent?): ExternalApiResult {
            if (requestCode != REQUEST_CODE || resultCode != Activity.RESULT_OK) {
                return ExternalApiResult.FAILURE
            }

            val scannedBarcode = result?.getStringExtra(QRScannerActivity.EXTRA_RESULT)
                ?: return ExternalApiResult.FAILURE

            return ExternalApiResult.success(scannedBarcode)
        }
    }

    private val methodScanLoop = object : IMethodInvokerWithActivityResult {

        override fun invoke(parameters: List<Any>): ExternalApiResult {
            val beepOnEachRead = parameters[0].toString().toBoolean()
            val barcodeTypes = if (parameters.size == 2) (parameters[1] as ValueCollection) else null
            val intent = QRScannerActivity.newIntent(context, true, beepOnEachRead, barcodeTypes)
            startActivityForResult(intent, REQUEST_CODE)
            return ExternalApiResult.SUCCESS_WAIT
        }

        override fun handleActivityResult(requestCode: Int, resultCode: Int, result: Intent?): ExternalApiResult {
            if (requestCode != REQUEST_CODE || resultCode != Activity.RESULT_OK) {
                return ExternalApiResult.FAILURE
            }

            val scannedBarcodes = result?.getStringExtra(QRScannerActivity.EXTRA_RESULT)
                ?: return ExternalApiResult.FAILURE

            val values = scannedBarcodes.split(VALUE_SEPARATOR).toMutableList()
            values.removeAll { s -> s.isEmpty() }

            val collection = EntityFactory.newSdtCollection("GeneXus.SD.ScannedBarcodes", values)

            return ExternalApiResult.success(collection)
        }
    }

    init {
        addMethodHandlerRequestingPermissions(METHOD_SCAN, 0, PERMISSIONS, methodScan)
        addMethodHandlerRequestingPermissions(METHOD_SCAN, 1, PERMISSIONS, methodScan)
        addMethodHandlerRequestingPermissions(METHOD_SCAN_LOOP, 1, PERMISSIONS, methodScanLoop)
        addMethodHandlerRequestingPermissions(METHOD_SCAN_LOOP, 2, PERMISSIONS, methodScanLoop)
    }
}
