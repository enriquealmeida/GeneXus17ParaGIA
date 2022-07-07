package com.genexus.android.core.externalobjects

import android.net.Uri
import com.genexus.android.core.actions.ApiAction
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.externalapi.ExternalApiResult
import com.genexus.android.core.externalapi.superapps.SuperAppExternalApi
import java.io.File

abstract class FileBaseAPI(action: ApiAction?) : SuperAppExternalApi(action) {
    protected var errorCode = 0
    protected var errorDescription = ""
    private var internalSource = "" // used in this class functions
    private var externalSource = "" // assigned by the user, return it unchanged if requested
    protected val source get() = internalSource

    private val getErrorCode = IMethodInvoker {
        ExternalApiResult.success(errorCode)
    }

    private val getErrorDescription = IMethodInvoker {
        ExternalApiResult.success(errorDescription)
    }

    private val getSource = IMethodInvoker {
        ExternalApiResult.success(externalSource)
    }

    private val setSource = IMethodInvoker { parameters ->
        setSourceValue(parameters[0].toString())
        ExternalApiResult.SUCCESS_CONTINUE
    }

    fun setSourceValue(s: String) {
        internalSource = s
        externalSource = s

        // Support URI with file scheme
        val uri = Uri.parse(s)
        if (uri.scheme?.equals("file") == true)
            uri.path?.let { internalSource = it }
    }

    protected abstract fun loadErrorDescription()

    private val methodCreate = IMethodInvoker {
        errorCode = if (Services.Strings.hasValue(source)) {
            val file = File(source)
            when {
                file.exists() -> 3
                file.mkdirs() -> 0
                else -> -1
            }
        } else {
            1
        }
        loadErrorDescription()
        ExternalApiResult.SUCCESS_CONTINUE
    }

    private val methodDelete = IMethodInvoker {
        errorCode = if (Services.Strings.hasValue(source)) {
            val file = File(source)
            if (!file.exists())
                2
            else if (file.deleteRecursively())
                0
            else
                -1
        } else {
            1
        }
        loadErrorDescription()
        ExternalApiResult.SUCCESS_CONTINUE
    }

    private val methodExists = IMethodInvoker {
        if (Services.Strings.hasValue(source)) {
            errorCode = 0
            loadErrorDescription()
            val file = File(source)
            ExternalApiResult.success(file.exists())
        } else {
            errorCode = 1
            loadErrorDescription()
            ExternalApiResult.success(false)
        }
    }

    private val methodRename = IMethodInvoker { parameters ->
        errorCode = if (Services.Strings.hasValue(source)) {
            val file = File(source)
            val dest = File(file.parentFile, parameters[0].toString())
            if (file.renameTo(dest))
                0
            else
                -1
        } else {
            1
        }
        loadErrorDescription()
        ExternalApiResult.SUCCESS_CONTINUE
    }

    private val methodGetName = IMethodInvoker {
        if (Services.Strings.hasValue(source)) {
            errorCode = 0
            loadErrorDescription()
            val file = File(source)
            ExternalApiResult.success(file.name)
        } else {
            errorCode = 1
            loadErrorDescription()
            ExternalApiResult.success("")
        }
    }

    protected val methodGetAbsoluteName = IMethodInvoker {
        if (Services.Strings.hasValue(source)) {
            errorCode = 0
            loadErrorDescription()
            val file = File(source)
            ExternalApiResult.success(file.absolutePath)
        } else {
            errorCode = 1
            loadErrorDescription()
            ExternalApiResult.success("")
        }
    }

    init {
        addReadonlyPropertyHandler(PROPERTY_ERROR_CODE, getErrorCode)
        addReadonlyPropertyHandler(PROPERTY_ERROR_DESCRIPTION, getErrorDescription)
        addPropertyHandler(PROPERTY_SOURCE, getSource, setSource)
        addMethodHandler(METHOD_CREATE, 0, methodCreate)
        addMethodHandler(METHOD_DELETE, 0, methodDelete)
        addMethodHandler(METHOD_EXISTS, 0, methodExists)
        addMethodHandler(METHOD_RENAME, 1, methodRename)
        addMethodHandler(METHOD_GET_NAME, 0, methodGetName)
        addMethodHandler(METHOD_GET_ABSOLUTE_NAME, 0, methodGetAbsoluteName)
    }

    companion object {
        const val PROPERTY_ERROR_CODE = "ErrCode"
        const val PROPERTY_ERROR_DESCRIPTION = "ErrDescription"
        const val PROPERTY_SOURCE = "Source"
        const val METHOD_CREATE = "Create"
        const val METHOD_DELETE = "Delete"
        const val METHOD_EXISTS = "Exists"
        const val METHOD_RENAME = "Rename"
        const val METHOD_GET_NAME = "GetName"
        const val METHOD_GET_ABSOLUTE_NAME = "GetAbsoluteName"
    }
}
