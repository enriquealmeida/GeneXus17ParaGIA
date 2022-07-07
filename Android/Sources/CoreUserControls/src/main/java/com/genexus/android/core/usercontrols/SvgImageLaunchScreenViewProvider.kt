package com.genexus.android.core.usercontrols

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.PictureDrawable
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.controls.LaunchScreenViewProvider

/**
 * Shows a static SVG image as launch screen
 *
 * The image file must be named "appwelcomedefault" or "appwelcomedefaultlandscape" and placed in
 * the raw directory.
 */
class SvgImageLaunchScreenViewProvider(private val resourceId: Int) : LaunchScreenViewProvider {
    override fun createView(context: Context, onFinishListener: LaunchScreenViewProvider.OnFinishListener): View {
        val imageView = ImageView(context)
        try {
            val svg: SVG = SVG.getFromResource(context, resourceId)
            val drawable: Drawable = PictureDrawable(svg.renderToPicture())
            imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            imageView.setImageDrawable(drawable)
        } catch (e: SVGParseException) {
            Services.Log.error(e)
        }

        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        params.gravity = Gravity.CENTER
        imageView.layoutParams = params

        onFinishListener.onFinish()

        return imageView
    }
}
