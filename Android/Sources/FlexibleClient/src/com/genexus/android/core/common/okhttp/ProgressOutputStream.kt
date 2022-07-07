package com.genexus.android.core.common.okhttp

import com.genexus.android.core.common.IProgressListener
import java.io.IOException
import java.io.OutputStream
import kotlin.Throws

class ProgressOutputStream(private val stream: OutputStream, private val listener: IProgressListener, private val total: Long) : OutputStream() {

    private var totalWritten: Long = 0
    @Throws(IOException::class)
    override fun write(b: ByteArray, off: Int, len: Int) {
        stream.write(b, off, len)
        if (total < 0) {
            listener.step(-1)
            return
        }
        if (len < b.size) {
            totalWritten += len.toLong()
        } else {
            totalWritten += b.size.toLong()
        }
        val percent = totalWritten * 1.0f / total
        listener.step(percent.toInt())
    }

    @Throws(IOException::class)
    override fun write(b: Int) {
        stream.write(b)
        if (total < 0) {
            listener.step(-1)
            return
        }
        totalWritten++
        val percent = totalWritten * 1.0f / total
        listener.step(percent.toInt())
    }

    @Throws(IOException::class)
    override fun close() {
        stream.close()
    }

    @Throws(IOException::class)
    override fun flush() {
        stream.flush()
    }

    init {
        // progress in percent
        if (total != -1L) listener.setCount(100) // use percent to send progress
    }
}
