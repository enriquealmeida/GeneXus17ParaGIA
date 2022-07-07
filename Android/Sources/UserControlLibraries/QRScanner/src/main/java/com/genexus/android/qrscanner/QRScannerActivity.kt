package com.genexus.android.qrscanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.genexus.android.core.base.model.ValueCollection

class QRScannerActivity : AppCompatActivity(), QRScannerInlineControl.OnQRCodeRead {

    private lateinit var scannerControl: QRScannerInlineControl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scanner_inline_activity)

        val loop = intent.getBooleanExtra(EXTRA_LOOP, false)
        val beep = intent.getBooleanExtra(EXTRA_BEEP, false)
        val barcodeTypes = intent.getStringArrayListExtra(EXTRA_BARCODE_TYPES)

        scannerControl = findViewById(R.id.scanner_inline_view)
        scannerControl.start(loop, beep, barcodeTypes, this)
    }

    override fun codeRead(result: Int, value: String) {
        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_RESULT, value)
        setResult(result, resultIntent)
        finish()
    }

    companion object {
        const val EXTRA_RESULT = "result"
        private const val EXTRA_LOOP = "loop"
        private const val EXTRA_BEEP = "beep"
        private const val EXTRA_BARCODE_TYPES = "barcodeTypes"

        fun newIntent(
            context: Context,
            withLoop: Boolean = false,
            beepOnEachRead: Boolean = false,
            barcodeTypes: ValueCollection? = null
        ): Intent {
            val intent = Intent(context, QRScannerActivity::class.java)
            intent.putExtra(EXTRA_LOOP, withLoop)
            intent.putExtra(EXTRA_BEEP, beepOnEachRead)
            if (barcodeTypes != null)
                intent.putExtra(EXTRA_BARCODE_TYPES, barcodeTypes)
            return intent
        }
    }
}
