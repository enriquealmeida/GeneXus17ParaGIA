package com.genexus.android.qrscanner

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatButton
import com.genexus.android.core.base.controls.IGxControlNotifyEvents
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.controls.IGxEdit
import com.genexus.android.core.ui.Coordinator
import com.genexus.android.qrscanner.QRScannerExternalObject.Companion.VALUE_SEPARATOR
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import io.fotoapparat.Fotoapparat
import io.fotoapparat.characteristic.LensPosition
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.preview.Frame
import io.fotoapparat.util.FrameProcessor
import io.fotoapparat.view.CameraView

class QRScannerInlineControl(
    context: Context,
    attrs: AttributeSet?,
    private val coordinator: Coordinator?,
    definition: LayoutItemDefinition?
) : RelativeLayout(context, attrs), IGxEdit, IGxControlNotifyEvents, View.OnClickListener {
    constructor(context: Context, attrs: AttributeSet) : this (context, attrs, null, null)
    @Suppress("UNUSED")
    constructor(context: Context, coordinator: Coordinator?, definition: LayoutItemDefinition?) :
        this(context, null, coordinator, definition)

    private var fotoapparat: Fotoapparat? = null
    private lateinit var frameProcessor: FrameProcessor
    private lateinit var barcodeScanner: BarcodeScanner
    private lateinit var helper: QRScannerHelper

    private var scannerView: CameraView
    private lateinit var nextButton: AppCompatButton
    private lateinit var finishButton: AppCompatButton

    private var loop = false
    private var beep = false
    private var shouldScan = true
    private var lastRead = 0L
    private var beepPlayer: MediaPlayer? = null
    private var barcodeTypes: MutableList<String>? = null
    private var scannedBarcodes = mutableListOf<String>()
    private var listener: OnQRCodeRead? = null

    fun start(loop: Boolean, beep: Boolean, barcodeTypes: MutableList<String>?, listener: OnQRCodeRead?) {
        this.listener = listener
        init(true, loop, beep, barcodeTypes)
    }

    init {
        inflate(context, R.layout.scanner_custom_view, this)
        scannerView = findViewById(R.id.scanner_view)

        if (coordinator != null && definition != null) {
            helper = QRScannerHelper(context, definition.controlInfo)
            helper.permissionRequest(
                context as Activity,
                { init(false, helper.loop, helper.beep, helper.barcodeTypes) },
                { (parent as? View)?.visibility = GONE }
            ).run()
        }
    }

    private fun init(buttonsVisible: Boolean, loop: Boolean, beep: Boolean, barcodeTypes: MutableList<String>?) {
        this.loop = loop
        this.beep = beep
        this.barcodeTypes = barcodeTypes

        if (buttonsVisible && loop) {
            nextButton = findViewById<AppCompatButton>(R.id.next_button).apply {
                setOnClickListener(this@QRScannerInlineControl)
                visibility = VISIBLE
            }
            finishButton = findViewById<AppCompatButton>(R.id.finish_button).apply {
                setOnClickListener(this@QRScannerInlineControl)
                visibility = VISIBLE
            }
        }

        frameProcessor = object : FrameProcessor {
            override fun invoke(frame: Frame) {
                scanImageForBarcode(frame)
            }
        }

        fotoapparat = Fotoapparat.with(context)
            .lensPosition { LensPosition.Back }
            .into(scannerView)
            .frameProcessor(frameProcessor)
            .previewScaleType(ScaleType.CenterCrop)
            .build()

        fotoapparat?.start()

        val options = BarcodeScannerOptions.Builder().barcodeFormats(barcodeTypes)
        barcodeScanner = BarcodeScanning.getClient(options.build())
    }

    private fun scanImageForBarcode(it: Frame) {
        val inputImage = InputImage.fromByteArray(it.image, it.size.width, it.size.height, it.rotation, InputImage.IMAGE_FORMAT_NV21)
        val task = barcodeScanner.process(inputImage)
        task.addOnSuccessListener { barCodesList ->
            for (barcodeObject in barCodesList)
                handleResult(barcodeObject)
        }
        task.addOnFailureListener {
            Services.Log.error("An exception occurred reading value from Frame", it)
        }
    }

    private fun handleResult(barcode: Barcode) {
        if (!shouldScan || lastRead != 0L && System.currentTimeMillis() - lastRead < 900)
            return

        val newReadValue = barcode.rawValue!!
        val lastReadValue = gxValue
        lastRead = System.currentTimeMillis()
        scannedBarcodes.add(newReadValue)
        Services.Log.debug("Barcode read $newReadValue")

        val codeReadAvailable = coordinator?.getControlEventHandler(this, EVENT_CODE_READ) != null
        val controlValueChangedAvailable = coordinator?.getControlEventHandler(this, EVENT_CONTROL_VALUE_CHANGED) != null
        if (beep && (codeReadAvailable || (controlValueChangedAvailable && lastReadValue != newReadValue))) {
            if (beepPlayer == null)
                beepPlayer = MediaPlayer.create(context, R.raw.beep)

            beepPlayer?.start()
        }

        if (coordinator == null) {
            if (loop)
                fotoapparat?.stop()
            else
                listener?.codeRead(Activity.RESULT_OK, newReadValue)
        } else {
            if (!loop) {
                fotoapparat?.stop()
                shouldScan = false
            }

            if (lastReadValue != newReadValue)
                controlValueChanged()

            codeReadEvent()
        }
    }

    override fun setVisibility(visibility: Int) {
        if (visibility == VISIBLE)
            isEnabled = true
        else if (visibility == INVISIBLE || visibility == GONE)
            isEnabled = false

        super.setVisibility(visibility)
    }

    override fun onClick(v: View) {
        if (v === nextButton)
            fotoapparat?.start()
        else if (v === finishButton) {
            val result = join(scannedBarcodes)
            listener?.codeRead(Activity.RESULT_OK, result)
        }
    }

    private fun controlValueChanged() {
        val previousValue = shouldScan
        coordinator?.onValueChanged(
            this, true,
            { shouldScan = false }, { shouldScan = previousValue }
        )
    }

    private fun codeReadEvent() {
        val previousValue = shouldScan
        coordinator?.runControlEvent(
            this, EVENT_CODE_READ,
            { shouldScan = false }, { shouldScan = previousValue }
        )
    }

    private fun BarcodeScannerOptions.Builder.barcodeFormats(barcodeTypes: List<String>?): BarcodeScannerOptions.Builder {
        if (barcodeTypes.isNullOrEmpty())
            return this

        val result = ArrayList<Int>(barcodeTypes.size)
        for (barcodeType in barcodeTypes) {
            when (barcodeType) {
                "UPC-E" -> result.add(Barcode.FORMAT_UPC_E)
                "Code39" -> result.add(Barcode.FORMAT_CODE_39)
                "EAN-13" -> result.add(Barcode.FORMAT_EAN_13)
                "EAN-8" -> result.add(Barcode.FORMAT_EAN_8)
                "Code93" -> result.add(Barcode.FORMAT_CODE_93)
                "Code128" -> result.add(Barcode.FORMAT_CODE_128)
                "ITF-14" -> result.add(Barcode.FORMAT_ITF)
                "PDF417" -> result.add(Barcode.FORMAT_PDF417)
                "QR" -> result.add(Barcode.FORMAT_QR_CODE)
                "Aztec" -> result.add(Barcode.FORMAT_AZTEC)
                "DataMatrix" -> result.add(Barcode.FORMAT_DATA_MATRIX)
            }
        }

        if (result.size == 1)
            setBarcodeFormats(result[0])
        else if (result.size > 1) {
            val firstElement = result[0]
            result.remove(0)
            setBarcodeFormats(firstElement, *result.toIntArray())
        }

        return this
    }

    private fun join(elements: List<String>): String {
        val strBuilder = StringBuilder()
        for (element in elements)
            strBuilder.append(element).append(VALUE_SEPARATOR)

        var result = strBuilder.toString()
        if (result.isNotEmpty())
            result = result.substring(0, result.length - VALUE_SEPARATOR.length)

        return result
    }

    override fun notifyEvent(type: IGxControlNotifyEvents.EventType?) {
        when (type) {
            IGxControlNotifyEvents.EventType.ACTIVITY_RESUMED -> onResume()
            IGxControlNotifyEvents.EventType.ACTIVITY_PAUSED -> onStop()
            IGxControlNotifyEvents.EventType.ACTIVITY_STOPPED -> onStop()
            null -> {}
            else -> {}
        }
    }

    private fun onResume() {
        if (shouldScan)
            fotoapparat?.start()
    }

    private fun onStop() {
        fotoapparat?.stop()
        beepPlayer?.release()
        beepPlayer = null
    }

    override fun getGxValue(): String {
        return if (scannedBarcodes.isNotEmpty())
            scannedBarcodes.last()
        else
            Strings.EMPTY
    }

    override fun setEnabled(enabled: Boolean) {
        shouldScan = enabled
        if (enabled)
            onResume()
        else
            onStop()
    }

    override fun setGxValue(value: String?) {}
    override fun setGxTag(tag: String?) { this.tag = tag }
    override fun getGxTag(): String? { return tag as String? }
    override fun isEditable(): Boolean { return true }
    override fun getViewControl(): IGxEdit { return this }
    override fun getEditControl(): IGxEdit { return this }
    override fun setValueFromIntent(data: Intent?) {}

    companion object {
        const val NAME = "ScannerInline"
        private const val EVENT_CODE_READ = "CodeRead"
        private const val EVENT_CONTROL_VALUE_CHANGED = "ControlValueChanged"
    }

    interface OnQRCodeRead {
        fun codeRead(result: Int, value: String)
    }
}
