package com.genexus.android.core.base.services

import android.content.Context
import android.graphics.drawable.Drawable
import com.genexus.android.core.base.metadata.images.ImageFile

interface IResourcesService {
    fun getImageDrawable(context: Context, imageName: String): Drawable?
    fun getResourceId(imageName: String, defType: String): Int

    /**
     * Checks whether a given resource id is valid or not.
     *
     * @param resId Id of the resource to check for
     * @return `true` if the resource exists; `false` otherwise
     */
    fun exists(resId: Int): Boolean

    /**
     * @param context Activity context where the image will be used
     * @param imageName Name of the image to give information
     * @return ImageFile associated with the given name
     */
    fun getImage(context: Context, imageName: String): ImageFile?

    /**
     * If the data image is actually embedded in the app, return its resource id.
     * This can happen, for example, if the image is loaded with &var.FromImage(KB_image).
     * @return The resource id if found or 0 otherwise.
     */
    fun getDataImageResourceId(imageUri: String): Int // defType = "drawable"
    fun getDataImageResourceId(imageUri: String, defType: String): Int
    fun getDataImage(context: Context, imageUri: String): ImageFile?

    /**
     * Returns the [Drawable] associated to `action` for displaying on the
     * **toolbar**.
     *
     * @param action the name of a standard action (e.g. Insert, Refresh, Share)
     */
    fun getActionBarDrawableFor(context: Context, action: String): Drawable?

    /**
     * Returns the [Drawable] associated to `action` for displaying on the **content
     * area**.
     *
     * @param action the name of a standard action (e.g. Insert, Refresh, Share)
     */
    fun getContentDrawableFor(context: Context, action: String): Drawable?
}
