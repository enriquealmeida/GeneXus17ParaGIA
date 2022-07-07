package com.genexus.android.core.controls

import android.content.Context
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition
import com.genexus.android.core.ui.Coordinator
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

class GxImageViewFactory(val context: Context, val coordinator: Coordinator?, val definition: LayoutItemDefinition?) {

    fun createEmptyImage(): IGxImageView {
        return GxImageViewImageComponent(context, coordinator, definition)
    }

    fun getImage(oldView: IGxImageView?, drawable: Drawable): IGxImageView {
        return if (oldView is GxImageViewImageComponent) {
            oldView.setImageDrawable(drawable)
            oldView
        } else {
            val view = GxImageViewImageComponent(context, coordinator, definition)
            view.setImageDrawable(drawable)
            view
        }
    }

    fun clearImage(view: IGxImageView): Boolean {
        return if (view is GxImageViewImageComponent) {
            view.setImageDrawable(null)
            true
        } else {
            false
        }
    }

    @Suppress("deprecation")
    private fun createDeprecated(inputStream: InputStream): IGxImageView {
        val movie = android.graphics.Movie.decodeStream(inputStream)
        return GxImageViewGifComponentDeprecated(context, coordinator, definition, movie)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun createApi28(source: ImageDecoder.Source): IGxImageView {
        val drawable = ImageDecoder.decodeDrawable(source)
        val view = GxImageViewGifComponentApi28(context, coordinator, definition)
        view.setImageDrawable(drawable) // needs android:hardwareAccelerated="false" in the GenexusActivity in FlexibleClient/AndroidManifest.xml to be shown in an emulator
        view.start()
        return view
    }

    @Throws(IOException::class)
    fun createGif(id: Int): IGxImageView {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            createDeprecated(context.resources.openRawResource(id))
        } else {
            createApi28(ImageDecoder.createSource(context.resources, id))
        }
    }

    @Throws(IOException::class)
    fun createGif(file: File): IGxImageView {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            createDeprecated(FileInputStream(file))
        } else {
            // use File since createSource from ByteBuffer has a bug
            // https://stackoverflow.com/questions/56990336/how-to-use-animatedimagedrawable-for-showing-animated-gifs-with-a-bytebuffer-as
            createApi28(ImageDecoder.createSource(file))
        }
    }
}
