package com.genexus.android.uitesting.visual

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.genexus.android.core.common.StorageHelper
import com.genexus.android.core.utils.FileUtils2
import java.io.File

class FilesOperations(private val reference: String) {

    fun saveRemote(activity: Activity, bitmap: Bitmap): String {
        return saveToFile(activity, bitmap, SUFFIX_REMOTE)
    }

    fun saveLocal(activity: Activity, bitmap: Bitmap): String {
        return saveToFile(activity, bitmap, SUFFIX_LOCAL)
    }

    private fun saveToFile(activity: Activity, bitmap: Bitmap, suffix: String): String {
        findFile(suffix)?.delete()
        return FileUtils2.saveBitmapToFile(activity, bitmap, SUBDIR, reference + suffix, null)
    }

    fun getRemote(): Bitmap? {
        return loadFromFile(SUFFIX_REMOTE)
    }

    fun getLocal(): Bitmap? {
        return loadFromFile(SUFFIX_LOCAL)
    }

    fun getLocalFile(): File? {
        return findFile(SUFFIX_LOCAL)
    }

    private fun loadFromFile(suffix: String): Bitmap? {
        val file = findFile(suffix)
        if (file != null)
            return BitmapFactory.decodeFile(file.absolutePath)

        return null
    }

    private fun findFile(suffix: String): File? {
        val fileName = reference + suffix
        val directory = File(StorageHelper.getStorageDirectory(true), SUBDIR)
        if (directory.exists()) {
            val files = directory.listFiles { _, name -> name.startsWith(fileName) }
            if (files != null && files.isNotEmpty())
                return files[0]
        }

        return null
    }

    companion object {
        private const val SUFFIX_REMOTE = "_remote"
        private const val SUFFIX_LOCAL = "_local"
        private const val SUBDIR = "testing"
    }

    init {
        require(reference.isNotEmpty()) { "Reference cannot be empty" }
    }
}
