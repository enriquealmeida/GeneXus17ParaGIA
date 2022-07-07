package com.genexus.android.core.controls

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Matrix
import android.graphics.drawable.AnimatedImageDrawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import com.genexus.android.core.base.metadata.enums.Alignment
import com.genexus.android.core.base.metadata.enums.ImageScaleType
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition
import com.genexus.android.core.ui.Coordinator

@SuppressLint("ViewConstructor")
@RequiresApi(Build.VERSION_CODES.P)
class GxImageViewGifComponentApi28(context: Context, @Suppress("UNUSED_PARAMETER") coordinator: Coordinator?, definition: LayoutItemDefinition?) : AppCompatImageView(context), IGxImageView {
    private val alignment: Int
    private var scaleType: ImageScaleType
    private var imageWidth: Int? = null
    private var imageHeight: Int? = null

    init {
        scaleType = ImageScaleType.FIT
        super.setScaleType(ScaleType.MATRIX)
        if (definition != null)
            alignment = GxImageViewImageComponent.fixDefaultValueOnAlignment(definition.cellGravity)
        else
            alignment = 0
    }

    fun start() {
        val drawable = drawable ?: return
        if (drawable is AnimatedImageDrawable)
            drawable.start()
        updateLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (drawable != null && MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            var height = width * drawable.intrinsicHeight / drawable.intrinsicWidth

            when (MeasureSpec.getMode(heightMeasureSpec)) {
                MeasureSpec.EXACTLY -> height = MeasureSpec.getSize(heightMeasureSpec)
                MeasureSpec.AT_MOST -> {
                    val maxHeight = MeasureSpec.getSize(heightMeasureSpec)
                    if (maxHeight in 1 until height)
                        height = maxHeight
                }
                MeasureSpec.UNSPECIFIED -> { }
            }

            setMeasuredDimension(width, height)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    fun updateLayout() {
        val drawable = drawable ?: return

        // Width and Height
        val width = imageWidth ?: this.width
        val height = imageHeight ?: this.height
        if (width == 0 || height == 0)
            return

        var scaleFactorX = width.toFloat() / drawable.intrinsicWidth
        var scaleFactorY = height.toFloat() / drawable.intrinsicHeight

        // Scale Type
        val matrix = Matrix()
        when (scaleType) {
            ImageScaleType.NO_SCALE -> {
                scaleFactorX = 1f
                scaleFactorY = scaleFactorX
            }
            ImageScaleType.FILL -> {
            }
            ImageScaleType.FILL_KEEPING_ASPECT -> {
                scaleFactorX = Math.max(scaleFactorX, scaleFactorY)
                scaleFactorY = scaleFactorX
            }
            ImageScaleType.FIT, ImageScaleType.TILE -> {
                scaleFactorX = Math.min(scaleFactorX, scaleFactorY)
                scaleFactorY = scaleFactorX
            }
        }
        matrix.setScale(scaleFactorX, scaleFactorY)

        // Alignment
        val scaledFreeWidth = this.width / scaleFactorX - drawable.intrinsicWidth
        val scaledFreeHeight = this.height / scaleFactorY - drawable.intrinsicHeight
        val dx = if (alignment and Alignment.HORIZONTAL_MASK == Alignment.CENTER_HORIZONTAL) scaledFreeWidth / 2 else if (alignment and Alignment.HORIZONTAL_MASK == Alignment.RIGHT) scaledFreeWidth else 0f
        val dy = if (alignment and Alignment.VERTICAL_MASK == Alignment.CENTER_VERTICAL) scaledFreeHeight / 2 else if (alignment and Alignment.VERTICAL_MASK == Alignment.BOTTOM) scaledFreeHeight else 0f
        matrix.preTranslate(dx, dy)

        imageMatrix = matrix
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateLayout()
    }

    override fun setImagePropertiesFromThemeClass(themeClass: ThemeClassDefinition) {
        scaleType = themeClass.imageScaleType
        imageWidth = themeClass.imageWidth
        imageHeight = themeClass.imageHeight
        updateLayout()
    }

    override fun setImageScaleType(type: ImageScaleType) {
        if (scaleType != type) {
            scaleType = type
            updateLayout()
        }
    }

    override fun setImageSize(width: Int, height: Int) {
        if (width != imageWidth || height != imageHeight) {
            imageWidth = width
            imageHeight = height
            updateLayout()
        }
    }

    override fun hasImageDrawable(): Boolean {
        return false
    }
}
