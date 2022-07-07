package com.genexus.android.core.base.metadata.images

import com.genexus.android.core.base.metadata.theme.ThemeOptions
import com.genexus.android.core.base.services.Services
import java.lang.StringBuilder

class ImageFile(
    parent: ImageCollection,
    val name: String,
    type: Type,
    location: String,
    val options: ThemeOptions,
    val density: Float,
    private val autoMirror: Boolean
) {
    enum class Type {
        Internal,
        External
    }

    fun hasAutoMirror() = autoMirror

    val uri by lazy {
        if (type == Type.Internal) {
            // Relative, for internal images.
            val sb = StringBuilder()
            sb.append(Services.Application.get().UriMaker.baseImagesUrl)
            sb.append("/")
            if (Services.Strings.hasValue(parent.baseDirectory)) {
                sb.append(parent.baseDirectory)
                sb.append("/")
            }
            sb.append(location)
            sb.toString()
        } else {
            // Absolute, for external images.
            location
        }
    }

    val resourceName by lazy {
        if (type == Type.External)
            null // External images cannot have been embedded as resources.
        else
            location.lowercase()
                .replace(".", RESOURCE_CHAR_REPLACER)
                .replace("/", RESOURCE_CHAR_REPLACER)
                .replace("\\", RESOURCE_CHAR_REPLACER)
    }

    companion object {
        private const val RESOURCE_CHAR_REPLACER = "_"
    }
}
