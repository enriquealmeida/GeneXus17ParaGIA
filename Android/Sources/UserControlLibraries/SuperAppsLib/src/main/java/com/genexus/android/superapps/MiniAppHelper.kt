package com.genexus.android.superapps

import android.content.ActivityNotFoundException
import android.content.Context
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.superapps.MiniApp
import com.genexus.android.superapps.security.SignatureVerifier
import org.apache.commons.io.FileUtils
import java.io.File
import java.net.URL

internal class MiniAppHelper(private val miniApp: MiniApp, private val context: Context) {
    fun launch(): Boolean {
        val intent = MiniAppStartupActivity.getLoadIntent(context, miniApp)
        return try {
            miniApp.setLastUsed()
            Services.Application.miniApp = miniApp
            context.startActivity(intent)
            true
        } catch (ex: ActivityNotFoundException) {
            Services.Log.error(ex)
            Services.Application.unloadMiniApp()
            false
        }
    }

    fun downloadMetadata(): File? {
        val metadataUrl = miniApp.metadataRemoteUrl
        if (metadataUrl.isNullOrEmpty())
            return null

        val index = metadataUrl.lastIndexOf("/")
        val metadataFile = File(miniApp.getBaseDir(), metadataUrl.substring(index))
        if (metadataFile.exists())
            return metadataFile

        if (!Services.HttpService.isReachable(metadataUrl))
            return null

        metadataFile.parentFile?.mkdirs()
        metadataFile.createNewFile()
        FileUtils.copyURLToFile(URL(metadataUrl), metadataFile)
        return metadataFile
    }

    fun setMetadataFile(file: File) {
        miniApp.metadataLocalFile = file
    }

    fun deleteCompressedMetadata() {
        val file = miniApp.metadataLocalFile ?: return
        if (file.exists())
            file.delete()
    }

    fun isSignatureValid(): Boolean {
        val file = miniApp.metadataLocalFile
        val signature = miniApp.signature
        if (signature == null || file == null)
            return false

        return SignatureVerifier(context).verify(file, signature)
    }

    fun readyToLaunch(): Boolean {
        val extractedVersion = miniApp.getExtractedVersion()
        return extractedVersion >= miniApp.version
    }

    init {
        miniApp.refresh()
    }
}
