package com.genexus.android.core.base.metadata.loader

import android.content.Context
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.common.ZipHelper
import com.genexus.android.core.superapps.MiniApp
import org.apache.commons.io.IOUtils
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

class MiniApplicationLoader(miniApp: MiniApp) : ApplicationLoader(miniApp) {

    override fun preloadApplication(context: Context?) {
        Services.Log.debug("MiniApps don't require preloading")
    }

    override fun processMetadata(): LoadResult {
        val miniApp = application as MiniApp
        val currentVersion = miniApp.version
        if (currentVersion == MiniApp.INVALID_VERSION)
            return error(miniApp)

        val extractedVersion = miniApp.getExtractedVersion()
        if (extractedVersion >= currentVersion)
            return LoadResult.result(LoadResult.RESULT_OK)
        else if (miniApp.metadataLocalFile != null)
            deleteExtractedFiles(miniApp)

        val metadataFile = miniApp.metadataLocalFile ?: return error(miniApp)
        FileInputStream(metadataFile).use {
            try {
                val zipper = ZipHelper(it)
                zipper.unzip(appContext, miniApp)
            } catch (e: IOException) {
                Services.Log.error(e)
                return error(miniApp)
            } finally {
                IOUtils.closeQuietly(it)
            }
        }

        miniApp.apply {
            shouldReloadMetadata = false
            setCreatedAt()
            setExtractedVersion(currentVersion)
        }

        metadataFile.apply { if (exists()) delete() }
        return LoadResult.result(LoadResult.RESULT_OK)
    }

    private fun deleteExtractedFiles(miniApp: MiniApp) {
        miniApp.getBaseDir().listFiles()?.forEach {
            if (it.isFile && it.exists() && it.extension != METADATA_EXTENSION)
                it.delete()
        }
    }

    private fun error(app: MiniApp): LoadResult {
        return LoadResult.error(MiniAppMetadataNotFoundException(app.appEntry))
    }

    private inner class MiniAppMetadataNotFoundException(miniAppName: String) :
        FileNotFoundException(String.format("MiniApp '%s' metadata not found", miniAppName))

    companion object {
        private const val METADATA_EXTENSION = "gxsd"
    }
}
