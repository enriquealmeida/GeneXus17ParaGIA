package com.genexus.android.core.common.okhttp

import com.genexus.android.core.common.IProgressListener
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.internal.closeQuietly
import okio.BufferedSink
import okio.Source
import okio.buffer
import okio.sink
import okio.source
import java.io.IOException
import java.io.InputStream

// implement progress stream in OkHttp based in this sample
// example https://github.com/lizhangqu/CoreProgress/tree/master/library/src/main/java/io/github/lizhangqu/coreprogress
// with custom modifications.
class ProgressInputStreamRequestBody(private val inputStream: InputStream, private val length: Long, private val mediaType: MediaType, private val listener: IProgressListener?) : RequestBody() {
    override fun contentType(): MediaType? {
        return mediaType
    }

    override fun contentLength(): Long {
        return try {
            inputStream.available().toLong()
        } catch (e: IOException) {
            0
        }
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        if (listener == null) {
            writeToBuffer(sink)
            return
        }
        val progressOutputStream = ProgressOutputStream(sink.outputStream(), listener, contentLength())
        val progressSink = progressOutputStream.sink().buffer()
        writeToBuffer(progressSink)
        progressSink.flush()
    }

    @Throws(IOException::class)
    private fun writeToBuffer(sink: BufferedSink) {
        var source: Source? = null
        try {
            source = inputStream.source()
            sink.writeAll(source)
        } finally {
            source!!.closeQuietly()
        }
    }
}
