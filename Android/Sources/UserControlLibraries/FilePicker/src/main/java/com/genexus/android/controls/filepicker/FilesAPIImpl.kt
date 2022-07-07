package com.genexus.android.controls.filepicker

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.common.StorageHelper
import com.genexus.android.core.utils.FileUtils2
import com.genexus.android.media.MediaUtils
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

object FilesAPIImpl {

    fun saveToFolder(context: Context, filePath: String, destination: Int, replaceFile: Boolean): Boolean {

        if (!Strings.hasValue(filePath)) {
            Services.Log.error("Error copy file, empty Uri")
            return false
        }

        // getFile URI
        val fileUriString = getFileUri(filePath)

        // Parse URI
        val fileUri = Uri.parse(fileUriString)
        var fileName = FilenameUtils.getName(fileUriString)
        // get correct filename for content uris
        if (ContentResolver.SCHEME_CONTENT.equals(fileUri.getScheme(), ignoreCase = true))
            fileName = FileUtils2.getFileNameWithExtension(context, fileUri)

        val scheme = fileUri.scheme
        if (scheme == null) {
            Services.Log.debug(String.format("%s has a no scheme declared.", fileUri))
            return false
        }

        // get mimeType, allow save file with unknow mimetype
        val mimeType = MediaUtils.getMimeType(fileUriString)

        val destinationFolder = getDestinationFolder(destination)
        if (destinationFolder.isEmpty()) {
            // No valid directory, error.
            Services.Log.error("Error copy file, unknown target folder")
            return false
        }

        // pre Q devices need copy files using old method.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val fileResult = MediaUtils.addToFolderPreQ(context, fileUri, fileName, destinationFolder, !replaceFile)
            return fileResult != null
        }

        // save using MediaStore Q (API 29) or above
        return saveToFolderQ(context, fileUri, fileName, mimeType, destinationFolder, replaceFile)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveToFolderQ(context: Context, fileUri: Uri, fileName: String, mimeType: String?, destinationFolder: String, replaceFile: Boolean): Boolean {
        // save using MediaStore Q (API 29) or above
        val resolver = context.contentResolver
        val contentUri: Uri? = MediaStore.Files.getContentUri("external")

        if (contentUri != null) {

            // try to replace if parameter said that
            if (replaceFile) {
                Services.Log.debug("Save with Replace, check if File exists")
                // check if file already exists
                val fileExistingUri = getFilePathToMediaURI(fileName, destinationFolder, context, contentUri)
                if (fileExistingUri != null) {
                    // File exists, override it
                    Services.Log.debug("File exists, override it")
                    try {
                        copyToFile(resolver, fileExistingUri, fileUri, context)
                        return true
                    } catch (ex: IOException) {
                        Services.Log.error("Error overriding file $fileExistingUri", ex)
                    }
                }
            }
            // if not replace, or replace not found existing, create a new file. Default behavior
            // File not exists, add new one
            Services.Log.debug("Save a new file or with rename ")
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            if (mimeType != null)
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, destinationFolder)

            val outputUri = resolver.insert(contentUri, contentValues)
            try {
                if (outputUri != null) {
                    copyToFile(resolver, outputUri, fileUri, context)
                    return true
                }
            } catch (ex: IOException) {
                Services.Log.error("Error copying file $outputUri", ex)
            }
        }
        return false
    }

    private fun copyToFile(resolver: ContentResolver, outputUri: Uri, fileUri: Uri, context: Context) {
        val outputStream = resolver.openOutputStream(outputUri)
        val inputStream: InputStream?
        inputStream = if (ContentResolver.SCHEME_CONTENT == fileUri.scheme) {
            context.contentResolver.openInputStream(fileUri)
        } else { // Assume it's a local file (with or without the file:// scheme).
            FileInputStream(fileUri.path)
        }
        IOUtils.copy(inputStream, outputStream)
        inputStream?.close()
        outputStream?.close()
        Services.Log.debug("Copy file to $outputUri")
    }

    @Suppress("deprecation")
    private fun getFilePathToMediaURI(fileName: String, destinationFolder: String, context: Context, contentUri: Uri): Uri? {
        val externalDir = Environment.getExternalStoragePublicDirectory(destinationFolder) // deprecated
        val outputFile = File(externalDir, fileName)

        val cr = context.contentResolver
        val selection = MediaStore.Files.FileColumns.DATA
        val selectionArgs = arrayOf(outputFile.path)
        val projection = arrayOf(MediaStore.Files.FileColumns._ID)

        val cursor: Cursor? = cr.query(contentUri, projection, "$selection=?", selectionArgs, null)
        cursor?.use {
            while (cursor.moveToNext()) {
                val idIndex: Int = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)
                return ContentUris
                    .withAppendedId(
                        contentUri,
                        cursor.getInt(idIndex).toLong()
                    )
            }
        }
        return null
    }

    fun fileExits(context: Context, filePath: String, destination: Int): Boolean {

        if (!Strings.hasValue(filePath)) {
            return false
        }
        // get file Name
        val fileName = getFileName(filePath)

        val destinationFolder = getDestinationFolder(destination)
        if (destinationFolder.isEmpty()) {
            // No valid directory, error.
            Services.Log.error("unknown target folder")
            return false
        }

        val contentUri: Uri? = MediaStore.Files.getContentUri("external")
        if (contentUri != null) {
            val fileExistingUri =
                getFilePathToMediaURI(fileName, destinationFolder, context, contentUri)
            if (fileExistingUri != null) {
                return true
            }
        }
        return false
    }

    fun getFileName(filePath: String): String {

        if (!Strings.hasValue(filePath)) {
            return Strings.EMPTY
        }
        // getFile URI
        val fileUriString = getFileUri(filePath)

        // Parse URI
        return FilenameUtils.getName(fileUriString)
    }

    private fun getFileUri(filePath: String): String {
        // 1) local file => add scheme if not have.
        val FULL_FILE_SCHEME = ContentResolver.SCHEME_FILE + "://"
        // add file scheme if missing
        var fileUriString = filePath
        if (StorageHelper.isLocalFile(fileUriString) && !Strings.starsWithIgnoreCase(fileUriString, FULL_FILE_SCHEME))
            fileUriString = FULL_FILE_SCHEME + fileUriString
        return fileUriString
    }

    private fun getDestinationFolder(destination: Int): String {
        return when (destination) {
            1 -> Environment.DIRECTORY_DOWNLOADS
            2 -> Environment.DIRECTORY_DOCUMENTS
            else -> Strings.EMPTY
        }
    }
}
