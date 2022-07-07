package com.genexus.android.core.controls

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.SystemClock
import android.view.View
import com.genexus.android.core.base.metadata.enums.Alignment
import com.genexus.android.core.base.metadata.enums.ImageScaleType
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition
import com.genexus.android.core.ui.Coordinator

@SuppressLint("ViewConstructor")
@Suppress("deprecation") // Movie was deprecated in API 28
class GxImageViewGifComponentDeprecated(
    context: Context,
    @Suppress("UNUSED_PARAMETER") coordinator: Coordinator?,
    definition: LayoutItemDefinition?,
    private val movie: android.graphics.Movie
) :
    View(context), IGxImageView {

    private val alignment: Int
    private var scaleType: ImageScaleType
    private var imageWidth: Int? = null
    private var imageHeight: Int? = null

    private var start: Long = 0
    private val paint: Paint

    init {
        scaleType = ImageScaleType.FIT
        if (definition != null)
            alignment = GxImageViewImageComponent.fixDefaultValueOnAlignment(definition.cellGravity)
        else
            alignment = 0

        setLayerType(LAYER_TYPE_SOFTWARE, null)
        paint = Paint()
        paint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val now = SystemClock.uptimeMillis()
        if (start == 0L)
            start = now

        val duration = movie.duration()
        if (duration == 0) {
            movie.setTime(0) // Show a static image, gif with 1 image?
        } else {
            val relTime = ((now - start) % duration).toInt()
            movie.setTime(relTime)
        }

        // Width and Height
        val width = imageWidth?.toFloat() ?: width.toFloat()
        val height = imageHeight?.toFloat() ?: height.toFloat()
        val widthFactor = width / movie.width()
        val heightFactor = height / movie.height()

        // Scale Type
        val scaleFactorX: Float
        val scaleFactorY: Float
        when (scaleType) {
            ImageScaleType.NO_SCALE -> {
                scaleFactorY = 1f
                scaleFactorX = scaleFactorY
            }
            ImageScaleType.FILL -> {
                scaleFactorX = widthFactor
                scaleFactorY = heightFactor
            }
            ImageScaleType.FILL_KEEPING_ASPECT -> {
                scaleFactorY = Math.max(widthFactor, heightFactor)
                scaleFactorX = scaleFactorY
            }
            ImageScaleType.FIT, ImageScaleType.TILE -> {
                scaleFactorY = Math.min(widthFactor, heightFactor)
                scaleFactorX = scaleFactorY
            }
        }
        if (scaleFactorX != 1f || scaleFactorY != 1f)
            canvas.scale(scaleFactorX, scaleFactorY)

        // Alignment
        val scaledFreeWidth = getWidth() / scaleFactorX - movie.width()
        val scaledFreeHeight = getHeight() / scaleFactorY - movie.height()
        val x = if (alignment and Alignment.HORIZONTAL_MASK == Alignment.CENTER_HORIZONTAL) scaledFreeWidth / 2 else if (alignment and Alignment.HORIZONTAL_MASK == Alignment.RIGHT) scaledFreeWidth else 0f
        val y = if (alignment and Alignment.VERTICAL_MASK == Alignment.CENTER_VERTICAL) scaledFreeHeight / 2 else if (alignment and Alignment.VERTICAL_MASK == Alignment.BOTTOM) scaledFreeHeight else 0f

        movie.draw(canvas, x, y, paint)

        if (duration != 0) // No need to call it for static images
            invalidate()
    }

    override fun setImagePropertiesFromThemeClass(themeClass: ThemeClassDefinition) {
        scaleType = themeClass.imageScaleType
        imageWidth = themeClass.imageWidth
        imageHeight = themeClass.imageHeight
        invalidate()
    }

    override fun setImageScaleType(type: ImageScaleType) {
        if (scaleType != type) {
            scaleType = type
            invalidate()
        }
    }

    override fun setImageSize(width: Int, height: Int) {
        if (width != imageWidth || height != imageHeight) {
            imageWidth = width
            imageHeight = height
            invalidate()
        }
    }

    override fun hasImageDrawable(): Boolean {
        return false
    }
}
