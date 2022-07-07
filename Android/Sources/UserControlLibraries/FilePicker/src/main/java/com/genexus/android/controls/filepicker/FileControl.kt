package com.genexus.android.controls.filepicker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.genexus.android.core.base.controls.IGxHandleActivityResult
import com.genexus.android.core.base.metadata.enums.Alignment
import com.genexus.android.core.base.metadata.enums.ImageScaleType
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.controls.IGxEdit
import com.genexus.android.core.ui.Coordinator

@SuppressLint("ViewConstructor")
class FileControl(
    context: Context,
    private val coordinator: Coordinator,
    definition: LayoutItemDefinition
) : FrameLayout(context), IGxEdit, IGxHandleActivityResult {
    private var value: String = ""
    private var tag: String = ""
    private val scaleType: ImageScaleType
    private val alignment: Int
    private val imageView = AppCompatImageView(context)
    private var imageViewEdit: AppCompatImageView? = null

    init {
        addView(imageView)
        scaleType = if (definition.themeClass != null) definition.themeClass.imageScaleType else ImageScaleType.FIT
        alignment = definition.cellGravity

        val editable = !definition.optStringProperty("@readonly").equals("True", ignoreCase = true)
        if (editable) {
            val imageViewEditAlpha = View(context)
            imageViewEditAlpha.setBackgroundColor(Color.BLACK)
            imageViewEditAlpha.getBackground().alpha = 85 // 33%
            addView(imageViewEditAlpha)

            imageViewEdit = AppCompatImageView(context)
            val paramsEdit = LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            paramsEdit.gravity = Gravity.CENTER
            addView(imageViewEdit, paramsEdit)

            setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*"
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                coordinator.uiContext.activityController?.setActivityResultHandler(this)
                try {
                    (context as Activity).startActivityForResult(
                        Intent.createChooser(
                            intent,
                            "Select a File"
                        ),
                        FILE_SELECT_CODE
                    )
                } catch (e: ActivityNotFoundException) {
                    Services.Log.debug("BlobFilePicker", "Chooser not found: $e")
                }
            }
        }
    }

    override fun getGxTag(): String {
        return tag
    }

    override fun setGxTag(tag: String?) {
        this.tag = tag ?: ""
    }

    override fun setValueFromIntent(data: Intent?) {
    }

    override fun getGxValue(): String {
        return value
    }

    override fun setGxValue(value: String?) {
        if (!Services.Strings.hasValue(value)) {
            val img = Services.Resources.getImageDrawable(context, "ic_add")
            imageViewEdit?.setImageDrawable(img)
            imageView.setImageDrawable(null) // In case SetEmpty is called
        } else {
            imageView.setImageURI(Uri.parse(value))
            if (imageView.drawable == null) {
                val img = Services.Resources.getImageDrawable(context, "ic_binary_file")
                imageView.setImageDrawable(img)
            }
            setImageViewScaleType()
            val imgEdit = Services.Resources.getImageDrawable(context, "ic_edit")
            imageViewEdit?.setImageDrawable(imgEdit)
        }
        this.value = value ?: ""
    }

    private fun setImageViewScaleType() {
        if (scaleType == ImageScaleType.FILL)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
        else if (scaleType == ImageScaleType.FILL_KEEPING_ASPECT)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        else if (scaleType == ImageScaleType.FIT) {
            if (alignment == Alignment.START or Alignment.TOP)
                imageView.scaleType = ImageView.ScaleType.FIT_START
            else if (alignment == Alignment.END or Alignment.BOTTOM)
                imageView.scaleType = ImageView.ScaleType.FIT_END
            else
                imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        } else if (scaleType == ImageScaleType.NO_SCALE) {
            imageView.scaleType = ImageView.ScaleType.CENTER
        } else if (scaleType == ImageScaleType.TILE) {
            // It doesn't make sense to tile a drawable that isn't a bitmap
            // (since it will have no intrinsic dimensions).
            if (imageView.drawable is BitmapDrawable)
                (imageView.drawable as BitmapDrawable).setTileModeXY(
                    Shader.TileMode.REPEAT,
                    Shader.TileMode.REPEAT
                )
            imageView.scaleType = ImageView.ScaleType.FIT_XY
        }
    }

    override fun getEditControl(): IGxEdit {
        return this
    }

    override fun getViewControl(): IGxEdit {
        return this
    }

    override fun isEditable(): Boolean {
        return true
    }

    override fun handleOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (resultCode == Activity.RESULT_OK && requestCode == FILE_SELECT_CODE) {
            coordinator.onValueChanged(this, true, data?.data.toString())
            return true
        }
        return false
    }

    companion object {
        const val NAME = "SDFileControl"
        private const val FILE_SELECT_CODE = 325
    }
}
