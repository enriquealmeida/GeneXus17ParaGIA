package com.genexus.android.core.base.metadata.loader

import android.content.Context
import com.genexus.android.core.base.metadata.images.ImageCatalog
import com.genexus.android.core.base.metadata.images.ImageCollection
import com.genexus.android.core.base.metadata.images.ImageFile
import com.genexus.android.core.base.metadata.theme.ThemeOptions
import com.genexus.android.core.base.serialization.INodeObject

object ImagesMetadataLoader {
    @JvmStatic
    fun loadFrom(context: Context, jsonResources: INodeObject): ImageCatalog {
        val catalog = ImageCatalog().apply {
            defaultLanguage = jsonResources.optString("DefaultLanguage")
        }

        // Read all themes and languages since they may change dynamically.
        for (jsonFileReference in jsonResources.optCollection("Resources")) {
            val isDefault = jsonFileReference.optBoolean("IsDefault")
            val file = jsonFileReference.optString("ResourceFile").let {
                // Remove file extension because MetadataLoader adds it.
                if (it.endsWith(".json"))
                    it.substring(0, it.length - 5)
                else
                    it
            }
            val jsonResourceFile = MetadataLoader.getDefinition(context, file)
            if (jsonResourceFile != null)
                loadResourceFile(catalog, jsonResourceFile, isDefault)
        }
        return catalog
    }

    private fun loadResourceFile(catalog: ImageCatalog, jsonResources: INodeObject, isDefault: Boolean) {
        val baseDirectory = jsonResources.optString("ResourcesLocation")
        val theme = jsonResources.optString("Theme")
        val language = jsonResources.optString("Language")
        val imageCollection = ImageCollection(language, theme, isDefault, baseDirectory)
        catalog.add(imageCollection)
        for (jsonImage in jsonResources.optCollection("Images")) {
            val name = jsonImage.optString("Name")
            val strType = jsonImage.optString("Type")
            val type = if (strType == "E") ImageFile.Type.External else ImageFile.Type.Internal
            val location = jsonImage.optString("Location")
            val options = ThemeOptions()
            options.deserialize(jsonImage.getNode("Options"))
            val density = jsonImage.optFloat("Density")
            val autoMirror = jsonImage.optBoolean("FlipsForRTL", false)
            val imageFile = ImageFile(imageCollection, name, type, location, options, density, autoMirror)
            imageCollection.add(imageFile)
        }
    }
}
