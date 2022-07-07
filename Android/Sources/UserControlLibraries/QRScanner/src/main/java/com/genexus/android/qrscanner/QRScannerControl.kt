package com.genexus.android.qrscanner

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import com.genexus.android.core.base.controls.IGxEditThemeable
import com.genexus.android.core.base.controls.IGxHandleActivityResult
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition
import com.genexus.android.core.controls.GxTextView
import com.genexus.android.core.controls.IGxEdit
import com.genexus.android.core.ui.Coordinator
import com.genexus.android.core.utils.ThemeUtils

@SuppressLint("ViewConstructor")
class QRScannerControl(
    context: Context,
    private val coordinator: Coordinator,
    private val definition: LayoutItemDefinition
) : LinearLayout(context), IGxEdit, IGxEditThemeable, IGxHandleActivityResult {

    companion object {
        const val NAME = "Scanner"
        @VisibleForTesting
        const val REQUEST_CODE = 12734
    }

    private val edit: TextView
    private val scanButton: Button
    private val helper = QRScannerHelper(context, definition.controlInfo)
    private val clickListener = OnClickListener {
        val activity = context as Activity
        helper.permissionRequest(
            activity,
            Runnable {
                coordinator.uiContext.activityController?.setActivityResultHandler(this@QRScannerControl)
                activity.startActivityForResult(helper.intent, REQUEST_CODE)
            },
            null
        ).run()
    }

    init {
        edit = EditText(context)
        edit.isEnabled = false
        scanButton = Button(context)
        scanButton.setText(R.string.GXM_Scan)
        scanButton.setOnClickListener(clickListener)

        addView(edit, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f))
        addView(scanButton, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT))
    }

    override fun getGxValue(): String {
        return edit.text.toString()
    }

    override fun setGxValue(value: String) {
        edit.text = value
    }

    override fun getGxTag(): String {
        return edit.tag as String
    }

    override fun setGxTag(tag: String) {
        edit.tag = tag
    }

    override fun setValueFromIntent(data: Intent) {}

    override fun setEnabled(enabled: Boolean) {
        scanButton.isEnabled = enabled
        super.setEnabled(enabled)
    }

    override fun getViewControl(): IGxEdit {
        return GxTextView(context, definition)
    }

    override fun getEditControl(): IGxEdit {
        return this
    }

    override fun isEditable(): Boolean {
        return isEnabled // Editable when enabled.
    }

    override fun applyEditClass(themeClass: ThemeClassDefinition) {
        // Only apply font properties to edit control. Background, border, etc are already set by DataBoundControl.
        ThemeUtils.setFontProperties(edit, themeClass)
    }

    override fun handleOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (requestCode != REQUEST_CODE)
            return false

        if (resultCode == Activity.RESULT_OK) {
            val value = data?.getStringExtra(QRScannerActivity.EXTRA_RESULT)
            if (value != null) {
                val previousValue = edit.text
                edit.text = value
                val valueChanged = !TextUtils.equals(previousValue, value)
                if (valueChanged)
                    coordinator.onValueChanged(this@QRScannerControl, true)
            }
        }

        return true
    }
}
