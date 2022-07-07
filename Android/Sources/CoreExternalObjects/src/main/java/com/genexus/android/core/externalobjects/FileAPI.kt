package com.genexus.android.core.externalobjects

import android.util.Base64
import com.genexus.android.core.actions.ApiAction
import com.genexus.android.core.base.metadata.expressions.Expression
import com.genexus.android.core.base.model.ValueCollection
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.externalapi.ExternalApiResult
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.nio.charset.Charset
import java.util.Date

class FileAPI(action: ApiAction?) : FileBaseAPI(action) {
    private var reader: BufferedReader? = null
    private var writer: BufferedWriter? = null
    private var nextLine: String? = null // for reader

    private val getSeparator = IMethodInvoker {
        ExternalApiResult.success(File.separator)
    }

    override fun loadErrorDescription() {
        val e = when (errorCode) {
            0 -> ""
            1 -> "Invalid file instance"
            2 -> "The file does not exist"
            3 -> "The file already exists"
            100 -> "Security error"
            -1 -> "Undefined error"
            else -> null
        }
        if (e != null)
            errorDescription = e
    }

    private val methodCopy = IMethodInvoker { parameters ->
        errorCode = if (Services.Strings.hasValue(source)) {
            val file = File(source)
            if (!file.exists())
                2
            else {
                val target = File(parameters[0].toString())
                file.copyTo(target)
                0
            }
        } else {
            1
        }
        loadErrorDescription()
        ExternalApiResult.SUCCESS_CONTINUE
    }

    private val methodGetPath = IMethodInvoker {
        if (Services.Strings.hasValue(source)) {
            errorCode = 0
            loadErrorDescription()
            val file = File(source)
            ExternalApiResult.success(file.parent ?: "")
        } else {
            errorCode = 1
            loadErrorDescription()
            ExternalApiResult.success("")
        }
    }

    private val methodGetLastModified = IMethodInvoker {
        var date = Date()
        errorCode = if (Services.Strings.hasValue(source)) {
            val file = File(source)
            if (!file.exists())
                2
            else {
                date = Date(file.lastModified())
                0
            }
        } else {
            1
        }
        loadErrorDescription()
        ExternalApiResult.success(date)
    }

    private val methodGetLength = IMethodInvoker {
        var size = 0L
        errorCode = if (Services.Strings.hasValue(source)) {
            val file = File(source)
            if (!file.exists())
                2
            else {
                size = file.length()
                0
            }
        } else {
            1
        }
        loadErrorDescription()
        ExternalApiResult.success(size)
    }

    private val methodReadAllText = IMethodInvoker { parameters ->
        var string = ""
        errorCode = if (Services.Strings.hasValue(source)) {
            val file = File(source)
            string = if (parameters.size == 0)
                file.readText()
            else
                file.readText(Charset.forName(parameters[0].toString()))
            0
        } else {
            1
        }
        loadErrorDescription()
        ExternalApiResult.success(string)
    }

    private val methodReadAllLines = IMethodInvoker { parameters ->
        val stringList = ValueCollection(Expression.Type.STRING)
        errorCode = if (Services.Strings.hasValue(source)) {
            val file = File(source)
            val list = if (parameters.size == 0)
                file.readLines()
            else
                file.readLines(Charset.forName(parameters[0].toString()))
            stringList.addAll(list)
            0
        } else {
            1
        }
        loadErrorDescription()
        ExternalApiResult.success(stringList)
    }

    private val methodWriteAllText = IMethodInvoker { parameters ->
        errorCode = if (Services.Strings.hasValue(source)) {
            val file = File(source)
            if (parameters.size == 1)
                file.writeText(parameters[0].toString())
            else
                file.writeText(parameters[0].toString(), Charset.forName(parameters[1].toString()))
            0
        } else {
            1
        }
        loadErrorDescription()
        ExternalApiResult.SUCCESS_CONTINUE
    }

    private val methodWriteAllLines = IMethodInvoker { parameters ->
        errorCode = if (Services.Strings.hasValue(source)) {
            val file = File(source)
            val list = parameters[0] as List<*>
            val text = list.joinToString("\n") + if (list.isEmpty()) "" else "\n"
            if (parameters.size == 1)
                file.writeText(text)
            else
                file.writeText(text, Charset.forName(parameters[1].toString()))
            0
        } else {
            1
        }
        loadErrorDescription()
        ExternalApiResult.SUCCESS_CONTINUE
    }

    private val methodAppendAllText = IMethodInvoker { parameters ->
        errorCode = if (Services.Strings.hasValue(source)) {
            val file = File(source)
            if (parameters.size == 1)
                file.appendText(parameters[0].toString())
            else
                file.appendText(parameters[0].toString(), Charset.forName(parameters[1].toString()))
            0
        } else {
            1
        }
        loadErrorDescription()
        ExternalApiResult.SUCCESS_CONTINUE
    }

    private val methodAppendAllLines = IMethodInvoker { parameters ->
        errorCode = if (Services.Strings.hasValue(source)) {
            val file = File(source)
            val list = parameters[0] as List<*>
            val text = list.joinToString("\n") + if (list.isEmpty()) "" else "\n"
            if (parameters.size == 1)
                file.appendText(text)
            else
                file.appendText(text, Charset.forName(parameters[1].toString()))
            0
        } else {
            1
        }
        loadErrorDescription()
        ExternalApiResult.SUCCESS_CONTINUE
    }

    private val methodOpen = IMethodInvoker { parameters ->
        errorCode = if (Services.Strings.hasValue(source)) {
            nextLine = null
            val file = File(source)
            reader = when {
                !file.exists() -> null
                parameters.size == 0 -> file.bufferedReader()
                else -> file.bufferedReader(Charset.forName(parameters[0].toString()))
            }
            writer = if (parameters.size == 0)
                file.bufferedWriter()
            else
                file.bufferedWriter(Charset.forName(parameters[0].toString()))
            0
        } else {
            1
        }
        loadErrorDescription()
        ExternalApiResult.SUCCESS_CONTINUE
    }

    private val methodOpenRead = IMethodInvoker { parameters ->
        errorCode = if (Services.Strings.hasValue(source)) {
            nextLine = null
            val file = File(source)
            reader = if (parameters.size == 0)
                file.bufferedReader()
            else
                file.bufferedReader(Charset.forName(parameters[0].toString()))
            0
        } else {
            1
        }
        loadErrorDescription()
        ExternalApiResult.SUCCESS_CONTINUE
    }

    private val methodOpenWrite = IMethodInvoker { parameters ->
        errorCode = if (Services.Strings.hasValue(source)) {
            val file = File(source)
            writer = if (parameters.size == 0)
                file.bufferedWriter()
            else
                file.bufferedWriter(Charset.forName(parameters[0].toString()))
            0
        } else {
            1
        }
        loadErrorDescription()
        ExternalApiResult.SUCCESS_CONTINUE
    }

    private val methodClose = IMethodInvoker {
        errorCode = if (Services.Strings.hasValue(source)) {
            reader?.close()
            writer?.close()
            reader = null
            writer = null
            0
        } else {
            1
        }
        loadErrorDescription()
        ExternalApiResult.SUCCESS_CONTINUE
    }

    private val methodReadLine = IMethodInvoker {
        var string = ""
        errorCode = if (Services.Strings.hasValue(source)) {
            when {
                reader == null -> {
                    -1
                }
                nextLine != null -> {
                    string = nextLine ?: ""
                    nextLine = null
                    0
                }
                else -> {
                    string = reader?.readLine() ?: ""
                    0
                }
            }
        } else {
            1
        }
        loadErrorDescription()
        ExternalApiResult.success(string)
    }

    private val methodWriteLine = IMethodInvoker { parameters ->
        errorCode = if (Services.Strings.hasValue(source)) {
            if (writer == null)
                -1
            else {
                writer?.write(parameters[0].toString())
                writer?.newLine()
                0
            }
        } else {
            1
        }
        loadErrorDescription()
        ExternalApiResult.SUCCESS_CONTINUE
    }

    private val methodEndOfFile = IMethodInvoker {
        var eof = false
        errorCode = if (Services.Strings.hasValue(source)) {
            if (reader == null) {
                -1
            } else {
                if (nextLine == null)
                    nextLine = reader?.readLine()
                eof = nextLine == null
                0
            }
        } else {
            1
        }
        loadErrorDescription()
        ExternalApiResult.success(eof)
    }

    private val methodFromBase64String = IMethodInvoker { parameters ->
        errorCode = if (Services.Strings.hasValue(source)) {
            val file = File(source)
            val array = Base64.decode(parameters[0].toString(), Base64.DEFAULT)
            file.writeBytes(array)
            0
        } else {
            1
        }
        loadErrorDescription()
        ExternalApiResult.SUCCESS_CONTINUE
    }

    init {
        addReadonlyPropertyHandler(PROPERTY_SEPARATOR, getSeparator)
        addMethodHandler(METHOD_COPY, 1, methodCopy)
        addMethodHandler(METHOD_GET_PATH, 0, methodGetPath)
        addMethodHandler(METHOD_GET_URI, 0, methodGetAbsoluteName)
        addMethodHandler(METHOD_GET_LAST_MODIFIED, 0, methodGetLastModified)
        addMethodHandler(METHOD_GET_LENGTH, 0, methodGetLength)
        addMethodHandler(METHOD_READ_ALL_TEXT, 0, methodReadAllText)
        addMethodHandler(METHOD_READ_ALL_TEXT, 1, methodReadAllText)
        addMethodHandler(METHOD_READ_ALL_LINES, 0, methodReadAllLines)
        addMethodHandler(METHOD_READ_ALL_LINES, 1, methodReadAllLines)
        addMethodHandler(METHOD_WRITE_ALL_TEXT, 1, methodWriteAllText)
        addMethodHandler(METHOD_WRITE_ALL_TEXT, 2, methodWriteAllText)
        addMethodHandler(METHOD_WRITE_ALL_LINES, 1, methodWriteAllLines)
        addMethodHandler(METHOD_WRITE_ALL_LINES, 2, methodWriteAllLines)
        addMethodHandler(METHOD_APPEND_ALL_TEXT, 1, methodAppendAllText)
        addMethodHandler(METHOD_APPEND_ALL_TEXT, 2, methodAppendAllText)
        addMethodHandler(METHOD_APPEND_ALL_LINES, 1, methodAppendAllLines)
        addMethodHandler(METHOD_APPEND_ALL_LINES, 2, methodAppendAllLines)
        addMethodHandler(METHOD_OPEN, 0, methodOpen)
        addMethodHandler(METHOD_OPEN, 1, methodOpen)
        addMethodHandler(METHOD_OPEN_READ, 0, methodOpenRead)
        addMethodHandler(METHOD_OPEN_READ, 1, methodOpenRead)
        addMethodHandler(METHOD_OPEN_WRITE, 0, methodOpenWrite)
        addMethodHandler(METHOD_OPEN_WRITE, 1, methodOpenWrite)
        addMethodHandler(METHOD_CLOSE, 0, methodClose)
        addMethodHandler(METHOD_CLOSE, 1, methodClose)
        addMethodHandler(METHOD_READ_LINE, 0, methodReadLine)
        addMethodHandler(METHOD_WRITE_LINE, 1, methodWriteLine)
        addMethodHandler(METHOD_END_OF_FILE, 0, methodEndOfFile)
        addMethodHandler(METHOD_FROM_BASE_64_STRING, 1, methodFromBase64String)
    }

    companion object {
        const val OBJECT_NAME = "File"
        const val PROPERTY_SEPARATOR = "Separator"
        const val METHOD_COPY = "Copy"
        const val METHOD_GET_PATH = "GetPath"
        const val METHOD_GET_URI = "GetUri"
        const val METHOD_GET_LAST_MODIFIED = "GetLastModified"
        const val METHOD_GET_LENGTH = "GetLength"
        const val METHOD_READ_ALL_TEXT = "ReadAllText"
        const val METHOD_READ_ALL_LINES = "ReadAllLines"
        const val METHOD_WRITE_ALL_TEXT = "WriteAllText"
        const val METHOD_WRITE_ALL_LINES = "WriteAllLines"
        const val METHOD_APPEND_ALL_TEXT = "AppendAllText"
        const val METHOD_APPEND_ALL_LINES = "AppendAllLines"
        const val METHOD_OPEN = "Open"
        const val METHOD_OPEN_READ = "OpenRead"
        const val METHOD_OPEN_WRITE = "OpenWrite"
        const val METHOD_CLOSE = "Close"
        const val METHOD_READ_LINE = "ReadLine"
        const val METHOD_WRITE_LINE = "WriteLine"
        const val METHOD_END_OF_FILE = "EOF"
        const val METHOD_FROM_BASE_64_STRING = "FromBase64String"
    }
}
