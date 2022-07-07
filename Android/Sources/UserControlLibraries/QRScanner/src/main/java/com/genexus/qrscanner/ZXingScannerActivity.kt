package com.genexus.qrscanner

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.artech.base.model.ValueCollection
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import java.util.ArrayList
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ZXingScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler, View.OnClickListener {
    private lateinit var scannerView: ZXingScannerView
    private lateinit var scannedBarcodes: MutableList<String>
    private lateinit var nextButton: Button
    private lateinit var finishButton: Button
    private var loop: Boolean = false
    private var beep: Boolean = false
    private var beepPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loop = intent.getBooleanExtra(EXTRA_LOOP, false)
        beep = intent.getBooleanExtra(EXTRA_BEEP, false)
        val barcodeTypes = intent.getStringArrayListExtra(EXTRA_BARCODE_TYPES)

        setContentView(R.layout.scanner_activity)

        scannerView = findViewById(R.id.scanner_view)
        nextButton = findViewById(R.id.next_button)
        finishButton = findViewById(R.id.finish_button)

        nextButton.setOnClickListener(this)
        finishButton.setOnClickListener(this)

        if (Build.MANUFACTURER == "Huawei") {
            scannerView.setAspectTolerance(0.5f)
        }

        val mBarcodeFormats = mapBarcodeTypeToBarcodeFormat(barcodeTypes)
        if (mBarcodeFormats != null)
            scannerView.setFormats(mBarcodeFormats)

        scannedBarcodes = ArrayList()

        nextButton.visibility = if (loop) View.VISIBLE else View.INVISIBLE
        finishButton.visibility = if (loop) View.VISIBLE else View.INVISIBLE
    }

    private fun mapBarcodeTypeToBarcodeFormat(barcodeTypes: List<String>?): List<BarcodeFormat>? {
        if (barcodeTypes == null) {
            return null
        }

        val result = ArrayList<BarcodeFormat>(barcodeTypes.size)
        for (barcodeType in barcodeTypes) {
            when (barcodeType) {
                "UPC-E" -> result.add(BarcodeFormat.UPC_E)
                "Code39" -> result.add(BarcodeFormat.CODE_39)
                "EAN-13" -> result.add(BarcodeFormat.EAN_13)
                "EAN-8" -> result.add(BarcodeFormat.EAN_8)
                "Code93" -> result.add(BarcodeFormat.CODE_93)
                "Code128" -> result.add(BarcodeFormat.CODE_128)
                "ITF-14" -> result.add(BarcodeFormat.ITF)
                "PDF417" -> result.add(BarcodeFormat.PDF_417)
                "QR" -> result.add(BarcodeFormat.QR_CODE)
                "Aztec" -> result.add(BarcodeFormat.AZTEC)
                "DataMatrix" -> result.add(BarcodeFormat.DATA_MATRIX)
            }
        }

        return result
    }

    override fun onResume() {
        super.onResume()
        scannerView.setResultHandler(this)
        scannerView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }

    override fun handleResult(result: Result) {
        if (!loop) {
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_RESULT, result.text)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        } else {
            scannedBarcodes.add(result.text)
            if (beep) {
                if (beepPlayer == null)
                    beepPlayer = MediaPlayer.create(this, R.raw.beep)
                beepPlayer?.start()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        beepPlayer?.release()
        beepPlayer = null
    }

    override fun onClick(v: View) {
        if (v === nextButton) {
            onNextClicked()
        } else if (v === finishButton) {
            onFinishClicked()
        }
    }

    private fun onNextClicked() {
        scannerView.resumeCameraPreview(this)
    }

    private fun onFinishClicked() {
        val result = join(QRScannerExternalObject.VALUE_SEPARATOR, scannedBarcodes)

        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_RESULT, result)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun join(separator: String, elements: List<String>): String {
        val strBuilder = StringBuilder()
        for (element in elements) {
            strBuilder.append(element).append(separator)
        }
        var result = strBuilder.toString()
        if (result.isNotEmpty()) {
            result = result.substring(0, result.length - separator.length)
        }
        return result
    }

    companion object {
        const val EXTRA_RESULT = "result"
        private const val EXTRA_LOOP = "loop"
        private const val EXTRA_BEEP = "beep"
        private const val EXTRA_BARCODE_TYPES = "barcodetypes"

        fun newIntent(context: Context, withLoop: Boolean = false, beepOnEachRead: Boolean = false, barcodeTypes: ValueCollection? = null): Intent {
            val intent = Intent(context, ZXingScannerActivity::class.java)
            intent.putExtra(EXTRA_LOOP, withLoop)
            intent.putExtra(EXTRA_BEEP, beepOnEachRead)
            if (barcodeTypes != null)
                intent.putExtra(EXTRA_BARCODE_TYPES, barcodeTypes)
            return intent
        }
    }
}
