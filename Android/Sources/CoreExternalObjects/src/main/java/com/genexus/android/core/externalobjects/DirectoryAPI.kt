package com.genexus.android.core.externalobjects

import com.genexus.android.core.actions.ApiAction
import com.genexus.android.core.base.model.ApiCollection
import com.genexus.android.core.common.StorageHelper
import com.genexus.android.core.externalapi.ExternalApiResult
import java.io.File

class DirectoryAPI(action: ApiAction?) : FileBaseAPI(action) {
    private val getApplicationDataPath = IMethodInvoker {
        ExternalApiResult.success(StorageHelper.getApplicationDataPath())
    }

    private val getTemporaryFilesPath = IMethodInvoker {
        ExternalApiResult.success(StorageHelper.getTemporaryFilesPath())
    }

    private val getExternalFilesPath = IMethodInvoker {
        ExternalApiResult.success(StorageHelper.getExternalFilesPath())
    }

    private val getCacheFilesPath = IMethodInvoker {
        ExternalApiResult.success(StorageHelper.getTemporaryFilesPath())
    }

    override fun loadErrorDescription() {
        val e = when (errorCode) {
            0 -> ""
            1 -> "Invalid directory instance"
            2 -> "Directory does not exist"
            3 -> "Directory already exists"
            4 -> "Directory not empty"
            100 -> "Security error"
            -1 -> "Undefined error"
            else -> null
        }
        if (e != null)
            errorDescription = e
    }

    private val methodGetFiles = IMethodInvoker {
        val list = ApiCollection()
        File(source).listFiles()?.let {
            for (file in it) {
                if (file.isFile) {
                    val f = FileAPI(action)
                    f.setSourceValue(file.absolutePath)
                    list.add(f)
                }
            }
        }
        ExternalApiResult.success(list)
    }

    private val methodGetDirectories = IMethodInvoker {
        val list = ApiCollection()
        File(source).listFiles()?.let {
            for (file in it) {
                if (file.isDirectory) {
                    val f = DirectoryAPI(action)
                    f.setSourceValue(file.absolutePath)
                    list.add(f)
                }
            }
        }
        ExternalApiResult.success(list)
    }

    init {
        addReadonlyPropertyHandler(PROPERTY_APPLICATION_DATA_PATH, getApplicationDataPath)
        addReadonlyPropertyHandler(PROPERTY_TEMPORARY_FILES_PATH, getTemporaryFilesPath)
        addReadonlyPropertyHandler(PROPERTY_EXTERNAL_FILES_PATH, getExternalFilesPath)
        addReadonlyPropertyHandler(PROPERTY_CACHE_FILES_PATH, getCacheFilesPath)
        addMethodHandler(METHOD_GET_FILES, 0, methodGetFiles)
        addMethodHandler(METHOD_GET_DIRECTORIES, 0, methodGetDirectories)
    }

    companion object {
        const val OBJECT_NAME = "Directory"
        const val PROPERTY_APPLICATION_DATA_PATH = "ApplicationDataPath"
        const val PROPERTY_TEMPORARY_FILES_PATH = "TemporaryFilesPath"
        const val PROPERTY_EXTERNAL_FILES_PATH = "ExternalFilesPath"
        const val PROPERTY_CACHE_FILES_PATH = "CacheFilesPath"
        const val METHOD_GET_FILES = "GetFiles"
        const val METHOD_GET_DIRECTORIES = "GetDirectories"
    }
}
