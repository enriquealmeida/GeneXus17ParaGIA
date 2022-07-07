package com.genexus.android.core.common.okhttp

import okio.Buffer
import okio.EOFException

class OkHttpBufferUtf8 {

    companion object {
        /**
         * Returns true if the body in question probably contains human readable text. Uses a small
         * sample of code points to detect unicode control characters commonly used in binary file
         * signatures.
         */
        fun isProbablyUtf8(buffer: Buffer): Boolean {
            try {
                val prefix = Buffer()
                val byteCount = buffer.size.coerceAtMost(64)
                buffer.copyTo(prefix, 0, byteCount)
                for (i in 0 until 16) {
                    if (prefix.exhausted()) {
                        break
                    }
                    val codePoint = prefix.readUtf8CodePoint()
                    if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                        return false
                    }
                }
                return true
            } catch (_: EOFException) {
                return false // Truncated UTF-8 sequence.
            }
        }
    }
}
