package com.genexus.android.core.common

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import com.genexus.android.content.FileProviderUtils
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition
import com.genexus.android.core.base.services.IImagesService
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.common.handlers.images.ImageHelperHandlers
import com.genexus.android.core.common.handlers.images.OnReceiveImageHandler
import com.genexus.android.core.controls.common.IViewDisplayGifSupport
import com.genexus.android.core.controls.common.IViewDisplayImage
import com.genexus.android.core.resources.MediaTypes
import com.genexus.android.core.resources.StandardImages
import org.apache.commons.io.FilenameUtils
import java.io.File
import java.io.IOException
import java.util.concurrent.Executors

internal class ImageHelper : IImagesService {
    private val executorService = Executors.newCachedThreadPool()

    override fun getStaticImage(context: Context, imageName: String) =
        getStaticImage(context, imageName, false, null, false)

    override fun getStaticImage(context: Context, imageName: String, loadAsHdpi: Boolean) =
        getStaticImage(context, imageName, false, null, loadAsHdpi)

    override fun getStaticImage(context: Context, imageName: String, loadAsHdpi: Boolean, waitForImage: Boolean) =
        getStaticImage(context, imageName, waitForImage, null, loadAsHdpi)

    override fun getStaticImage(context: Context, imageName: String, loadAsHdpi: Boolean, onReceive: OnReceiveImageHandler?) =
        getStaticImage(context, imageName, false, onReceive, loadAsHdpi)

    /**
     * Gets the specified "static" image, given it image name. Tries to load it from
     * the embedded resources, and if that fails, from the cache of downloaded images.
     *
     * If that also fails, then if the waitForServer parameter is true, the method
     * blocks until the server returns the image. Otherwise a thread is launched to
     * request the image from the server, though this call returns null.
     */
    private fun getStaticImage(context: Context, imageName: String, waitForImage: Boolean, onReceive: OnReceiveImageHandler?, loadAsHdpi: Boolean): Drawable? {
        if (imageName.isEmpty()) return null

        // 1) Try to get from resources.
        Services.Resources.getImageDrawable(context, imageName)?.let { return it }

        // 1.1) Accept alternative resource format (Resources/<filename>).
        // When loading a variable with a static image they can be in either format!
        val resourceId = Services.Resources.getDataImageResourceId(imageName)
        if (resourceId != 0)
            return ContextCompat.getDrawable(context, resourceId)

        // 2) Try to get from cache.
        getImageFromCache(context, imageName)?.let { return it }

        return if (waitForImage) {
            // 3.a) Download the image and return it.
            getImageFromServer(context, imageName, loadAsHdpi)
        } else {
            // 3.b) Launch a thread to get the image.
            executorService.execute {
                val drawable = getImageFromServer(context, imageName, loadAsHdpi)
                onReceive?.receive(drawable)
            }
            null
        }
    }

    /** Cache key is image URL  */
    private fun getImageFromCache(context: Context, imageName: String): Drawable? {
        val imageFile = Services.Resources.getImage(context, imageName)
        val imageUrl = imageFile?.uri ?: return null
        return ImageLoader.getCachedDrawable(context, imageUrl)
    }

    /** Load Image with image loader  */
    private fun getImageFromServer(context: Context, imageName: String, loadAsHdpi: Boolean): Drawable? {
        val imageFile = Services.Resources.getImage(context, imageName)
        val imageUrl = imageFile?.uri ?: return null
        return ImageLoader.getDrawable(context, imageUrl, imageUrl, loadAsHdpi)
    }

    override fun displayImage(view: IViewDisplayImage, imageName: String) {
        if (imageName.isNotEmpty()) {
            val handler = ImageHelperHandlers.SetImageViewHandler(view)
            val drawable = getStaticImage(view.context, imageName, false, handler, false)
            handler.receive(drawable)
        }
    }

    override fun displayBackground(view: View, imageName: String) {
        displayBackgroundWithClass(view, null, imageName)
    }

    override fun displayBackgroundWithClass(view: View, themeClassDefinition: ThemeClassDefinition?, imageName: String) {
        if (imageName.isNotEmpty()) {
            val handler = ImageHelperHandlers.SetViewBackgroundWithClassHandler(view, themeClassDefinition)
            val drawable = getStaticImage(view.context, imageName, false, handler, false)
            handler.receive(drawable)
        }
    }

    override fun getSharedImage(context: Context, imageName: String): Uri? {
        if (imageName.isNotEmpty()) {
            // Get the image file (either from cache, or downloading it now).
            val imageFile = ImageLoader.getImageFile(context, imageName)
            try {
                if (imageFile.exists()) {
                    try {
                        return FileProviderUtils.getSharedUriFromFile(context, imageFile.file)
                    } catch (e: IOException) {
                        Services.Log.error("Error sharing image", e)
                    }
                }
            } finally {
                // (Possibly) delete temporary file.
                imageFile.cleanup()
            }
        }
        return null
    }

    override fun showImage(context: Context, imageView: IViewDisplayImage, imageUrl: String, showPlaceholder: Boolean, fullResolution: Boolean) {
        if (imageUrl.isEmpty()) {
            if (showPlaceholder) {
                imageView.imageTag = null
                StandardImages.stopLoading(imageView)
                val drawable = Services.Resources.getContentDrawableFor(context, MediaTypes.IMAGE_STUB)
                StandardImages.showPlaceholderImage(imageView, drawable, true)
            }
            return
        }
        StandardImages.startLoading(imageView)

        // TODO: check if url is a resources file in a better way.
        if (imageUrl.startsWith("/") && !StorageHelper.isLocalFile(imageUrl)) { // It's a URL relative to the server endpoint.
            val resourceName = FilenameUtils.getBaseName(imageUrl)
            showStaticImage(context, imageView, resourceName)
        } else {
            showDataImage(context, imageView, imageUrl, showPlaceholder, fullResolution)
        }
    }

    override fun showStaticImage(context: Context, icon: IViewDisplayImage, imageName: String) {
        if (imageName.isEmpty())
            return

        // 1) Check if it is a gif
        val file = Services.Resources.getImage(context, imageName)
        if (file != null && file.uri.endsWith(".gif") && icon is IViewDisplayGifSupport) {
            val id = Services.Resources.getResourceId(imageName, "raw")
            if (id != 0)
                (icon as IViewDisplayGifSupport).setGifImageResource(id)
            return
        }

        // 2) Check if it is a svg
        if (file != null && file.uri.endsWith(".svg")) {
            try {
                val id = Services.Resources.getResourceId(imageName, "raw")
                val svg = SVG.getFromResource(context, id)
                if (id != 0) {
                    val drawable: Drawable = PictureDrawable(svg.renderToPicture())
                    icon.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                    icon.setImageDrawable(drawable)
                }
            } catch (e: SVGParseException) {
                Services.Log.error(e)
            }
            return
        }

        // 3) Try to get from resources.
        Services.Resources.getImageDrawable(context, imageName)?.let {
            icon.setImageDrawable(it)
            return
        }

        // 4) Try to get from cache.
        getImageFromCache(context, imageName)?.let {
            icon.setImageDrawable(it)
            return
        }

        // 5) Try to get with image loader.
        file?.uri?.let { imageUrl ->
            icon.imageTag = imageUrl
            val loader = ImageLoader.fromContext(context)
            loader.displayImage(imageUrl, icon, false, false, file.hasAutoMirror())
        }
    }

    // TODO: If it´s a local image, we should check if we have storage permission and ask for it if we don't.
    override fun showDataImage(
        context: Context,
        imageView: IViewDisplayImage,
        imageUri: String,
        placeholderRequired: Boolean,
        fullResolution: Boolean
    ) {
        if (imageUri.isNotEmpty()) {
            // Try to load from resources first, in case it is embedded.
            if (imageUri.endsWith(".svg")) {
                val resourceId = Services.Resources.getDataImageResourceId(imageUri, "raw")
                if (resourceId != 0) {
                    try {
                        val svg = SVG.getFromResource(context, resourceId)
                        val drawable: Drawable = PictureDrawable(svg.renderToPicture())
                        imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                        imageView.setImageDrawable(drawable)
                    } catch (e: SVGParseException) {
                        Services.Log.error(e)
                    }
                    return
                }
            } else {
                val resourceId = Services.Resources.getDataImageResourceId(imageUri)
                if (resourceId != 0) {
                    if (imageUri.endsWith(".gif") && imageView is IViewDisplayGifSupport) (imageView as IViewDisplayGifSupport).setGifImageResource(
                        resourceId
                    ) else imageView.setImageResource(resourceId)
                    return
                }
            }

            // It's an image file, either remote or in the local filesystem. Load Image with image loader.
            var showLoading = true
            val imageFullPath = when {
                StorageHelper.isLocalFile(imageUri) -> {
                    showLoading = false
                    imageUri
                }
                !imageUri.contains("://") -> {
                    // Try getting the url from metadata
                    val file = Services.Resources.getDataImage(context, imageUri)
                    file?.uri ?: Services.Application.get().UriMaker.getImageUrl(imageUri)
                }
                else -> imageUri // http://, content://, &c
            }
            imageView.imageTag = imageFullPath

            // Enqueue the request to load.
            imageView.setImageBitmap(null)
            val loader = ImageLoader.fromContext(context)
            loader.displayImage(imageFullPath, imageView, showLoading, fullResolution, false)
        } else {
            // Set image identifier to null, to clear it in list when reusing views.
            imageView.imageTag = null
            StandardImages.stopLoading(imageView)
            val drawable = Services.Resources.getContentDrawableFor(context, MediaTypes.IMAGE_STUB)
            StandardImages.showPlaceholderImage(imageView, drawable, placeholderRequired)
        }
    }

    override fun getBitmap(context: Context, imageUrl: String) =
        getBitmap(context, imageUrl, false)

    override fun getBitmap(context: Context, imageUrl: String, getFullImage: Boolean): Bitmap? {
        val finalUrl = getImageUrl(imageUrl)
        return ImageLoader.fromContext(context).getBitmap(finalUrl, getFullImage)
    }

    override fun clearCache() {
        ImageLoader.clearCache()
    }

    override fun getCachedImageFile(imageIdentifier: String): File? {
        if (imageIdentifier.isEmpty())
            return null
        val imageRemoteUri = Services.Application.get().UriMaker.getImageUrl(imageIdentifier)
        return ImageLoader.getCachedImageFile(imageRemoteUri)
    }

    override fun getImageFile(context: Context, imageUrl: String): File? {
        val finalUrl = getImageUrl(imageUrl)
        return ImageLoader.fromContext(context).getInputFile(finalUrl)
    }

    private fun getImageUrl(imageUrl: String): String {
        return if (!StorageHelper.isLocalFile(imageUrl) &&
            !ContentResolver.SCHEME_CONTENT.equals(Uri.parse(imageUrl).scheme, ignoreCase = true)
        )
            Services.Application.get().UriMaker.getImageUrl(imageUrl)
        else
            imageUrl
    }
}
