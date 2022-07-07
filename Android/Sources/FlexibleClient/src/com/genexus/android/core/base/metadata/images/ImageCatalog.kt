package com.genexus.android.core.base.metadata.images

import com.genexus.android.core.base.services.Services

class ImageCatalog {
    private val collections = mutableListOf<ImageCollection>()
    var defaultLanguage: String? = null

    fun add(collection: ImageCollection) {
        collections.add(collection)
    }

    fun getImage(imageName: String, screenDensity: Float): ImageFile? {
        return imageCollection?.get(imageName, screenDensity)
    }

    // TODO: Cache this, but must be refreshed when system language changes!
    private val imageCollection: ImageCollection?
        get() = currentCollection ?: defaultCollection

    // Filter by theme and language to get best one.
    private val currentCollection: ImageCollection?
        get() {
            // Filter by theme and language to get best one.
            val deviceTheme = Services.Themes.currentThemeName
            val deviceLanguage = Services.Language.currentLanguage ?: defaultLanguage

            // If theme and language are unknown, the return null, so that the default image collection is used.
            if (deviceTheme.isNullOrEmpty() || deviceLanguage.isNullOrEmpty())
                return null

            return collections.firstOrNull {
                deviceLanguage.equals(it.language, ignoreCase = true) &&
                    deviceTheme.equals(it.theme, ignoreCase = true)
            }
        }

    private val defaultCollection get() = collections.firstOrNull { it.isDefault }
}
