package com.genexus.android.core.base.services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition
import com.genexus.android.core.common.handlers.images.OnReceiveImageHandler
import com.genexus.android.core.controls.common.IViewDisplayImage
import java.io.File

interface IImagesService {
    fun getStaticImage(context: Context, imageName: String): Drawable?
    fun getStaticImage(context: Context, imageName: String, loadAsHdpi: Boolean): Drawable?
    fun getStaticImage(context: Context, imageName: String, loadAsHdpi: Boolean, waitForImage: Boolean): Drawable?
    fun getStaticImage(context: Context, imageName: String, loadAsHdpi: Boolean, onReceive: OnReceiveImageHandler?): Drawable?

    fun displayImage(view: IViewDisplayImage, imageName: String)
    fun displayBackgroundWithClass(view: View, themeClassDefinition: ThemeClassDefinition?, imageName: String)

    fun displayBackground(view: View, imageName: String)
    fun getSharedImage(context: Context, imageName: String): Uri?

    /**
     * Common utility method to load an image from an `imageUrl` source into an `imageView`.
     *
     * @param context         for retrieving the ImageLoader instance.
     * @param imageView       target view to load the image into.
     * @param imageUrl        source from which to get the image.
     * @param showPlaceholder whether a placeholder should be shown when the value is empty or invalid.
     * @param fullResolution  whether the image should be scaled to fit the view or not.
     */
    fun showImage(
        context: Context,
        imageView: IViewDisplayImage,
        imageUrl: String,
        showPlaceholder: Boolean,
        fullResolution: Boolean
    )

    fun showStaticImage(context: Context, icon: IViewDisplayImage, imageName: String)
    fun showDataImage(
        context: Context,
        imageView: IViewDisplayImage,
        imageUri: String,
        placeholderRequired: Boolean,
        fullResolution: Boolean
    )

    fun getBitmap(context: Context, imageUrl: String): Bitmap?
    fun getBitmap(context: Context, imageUrl: String, getFullImage: Boolean): Bitmap?
    fun clearCache()
    fun getImageFile(context: Context, imageUrl: String): File?
    fun getCachedImageFile(imageIdentifier: String): File?
}
