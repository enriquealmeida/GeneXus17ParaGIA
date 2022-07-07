package com.genexus.android.core.base.metadata.loader.steps

import android.content.Context
import com.genexus.android.core.base.metadata.images.ImageCatalog
import com.genexus.android.core.base.metadata.languages.LanguageCatalog
import com.genexus.android.core.base.metadata.loader.ImagesMetadataLoader
import com.genexus.android.core.base.metadata.loader.LanguagesMetadataLoader
import com.genexus.android.core.base.metadata.loader.MetadataLoadStep
import com.genexus.android.core.base.metadata.loader.MetadataLoader
import com.genexus.android.core.base.services.Services

class ResourcesLoadStep(private val context: Context) : MetadataLoadStep {
    override fun load() {
        val languagesFile = MetadataLoader.getDefinition(context, "languages")
        val languages = if (languagesFile != null)
            LanguagesMetadataLoader.loadFrom(context, languagesFile)
        else
            LanguageCatalog()

        val imagesFile = MetadataLoader.getDefinition(context, "GXImages")
        val images = if (imagesFile != null)
            ImagesMetadataLoader.loadFrom(context, imagesFile)
        else
            ImageCatalog()

        Services.Language.initialize(languages, images)
    }
}
