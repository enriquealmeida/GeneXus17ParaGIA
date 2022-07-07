package com.genexus.android.core.common

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.genexus.android.core.base.metadata.images.ImageFile
import com.genexus.android.core.base.services.IApplication
import com.genexus.android.core.base.services.IResourcesService
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.Strings.isNumeric
import com.genexus.android.core.resources.BuiltInResources

class ResourcesManager(private val application: IApplication) : IResourcesService {
    override fun getImage(context: Context, imageName: String): ImageFile? {
        val screenDensity = context.resources.displayMetrics.density
        return application.definition.imageCatalog.getImage(imageName, screenDensity)
    }

    override fun getImageDrawable(context: Context, imageName: String): Drawable? {
        val resourceId = getResourceId(imageName, "drawable")
        return if (resourceId != 0) {
            try {
                ContextCompat.getDrawable(context, resourceId)?.apply {
                    getImage(context, imageName)?.let { imageFile -> isAutoMirrored = imageFile.hasAutoMirror() }
                }
            } catch (e: OutOfMemoryError) {
                ImageLoader.clearMemoryCache()
                Services.Log.error("Out of memory loading resource '$imageName'.", e)

                // Return a stub drawable instead of null; otherwise it will try to download the image from the server.
                // We ALREADY have that image, we just couldn't load (ran out of memory).
                ColorDrawable(Color.TRANSPARENT)
            }
        } else null
    }

    override fun getResourceId(imageName: String, defType: String): Int {
        val imageFile = application.definition.imageCatalog.getImage(imageName, Float.NaN) // image without resolution

        // Return the same name when resource is not found. For now it's necessary
        // for the welcome image (which is embedded with that fixed name).
        val resourceName = imageFile?.resourceName ?: imageName
        when {
            resourceName.isNotEmpty() -> {
                val id = application.androidApplication.resources.getIdentifier(resourceName, defType, application.appContext.packageName)
                if (id == 0 || isNumeric(resourceName) && !exists(id)) {
                    // getIdentifier can return the resourceName if it is a numeric value, so it is worth
                    // checking if the resource actually exists in that case to avoid crashing later
                    if (!Services.Application.hasActiveMiniApp()) // MiniApps never have embedded resources
                        Services.Log.error("Image not found: $resourceName")
                } else {
                    return id
                }
            }
            imageFile == null -> Services.Log.error("Image requested is null")
            else -> Services.Log.error("Image resource name not found: $imageName")
        }
        return 0
    }

    override fun exists(resId: Int): Boolean {
        return try {
            application.androidApplication.resources.getResourceName(resId)
            true
        } catch (e: Resources.NotFoundException) {
            Services.Log.error(e)
            false
        }
    }

    override fun getDataImageResourceId(imageUri: String): Int {
        return getDataImageResourceId(imageUri, "drawable")
    }

    private fun getLastSegment(imageUri: String): String? {
        val isResourceUri = imageUri.lowercase().let {
            !(it.startsWith("http://") || it.startsWith("https://")) &&
                it.contains("resources")
        }
        if (isResourceUri) {
            val posSlash = imageUri.lastIndexOfAny(charArrayOf('\\', '/'))
            if (posSlash > 0) {
                return imageUri.substring(posSlash + 1)
            }
        }
        return null
    }

    private fun getResourceNames(imageUri: String) = sequence {
        getLastSegment(imageUri)?.let { lastSegment ->
            val posPoint = lastSegment.lastIndexOf(".")
            if (posPoint > 0) {
                // try with new resources format
                val resourceNameN = lastSegment.replace(".", "_").lowercase()
                yield(resourceNameN)

                // try with old resources format
                val resourceNameO = lastSegment.substring(0, posPoint)
                yield(resourceNameO)
            }
        }
    }

    private fun getImageName(imageUri: String): String? {
        getLastSegment(imageUri)?.let { lastSegment ->
            val pos = lastSegment.indexOfAny(charArrayOf('.', '-'))
            if (pos >= 0)
                return lastSegment.substring(0, pos)
        }
        return null
    }

    override fun getDataImageResourceId(imageUri: String, defType: String): Int =
        getResourceNames(imageUri).map { Services.Resources.getResourceId(it, defType) }.firstOrNull { it > 0 } ?: 0

    override fun getDataImage(context: Context, imageUri: String): ImageFile? =
        getImageName(imageUri)?.let { getImage(context, it) }

    override fun getActionBarDrawableFor(context: Context, action: String): Drawable? {
        return getDrawableFor(context, action, BuiltInResources.PLACE_ACTION_BAR)
    }

    override fun getContentDrawableFor(context: Context, action: String): Drawable? {
        return getDrawableFor(context, action, BuiltInResources.PLACE_CONTENT)
    }

    private fun getDrawableFor(context: Context, action: String, place: Int): Drawable? {
        val resId = BuiltInResources.getResId(context, action, place)
        if (resId == 0)
            return null

        return ContextCompat.getDrawable(context, resId)?.apply {
            if (BuiltInResources.shouldAutoMirror(action))
                isAutoMirrored = true
        }
    }
}
