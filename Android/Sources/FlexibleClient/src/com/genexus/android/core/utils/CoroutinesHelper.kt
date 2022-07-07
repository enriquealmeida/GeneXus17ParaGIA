package com.genexus.android.core.utils

import android.os.Handler
import android.os.HandlerThread

object CoroutinesHelper {

    /**
     *	Creates a new HandlerThread and returns a new Handler instance using the
     *	HandlerThread's looper. The thread is not closed until the application is killed on purpose
     *	as it is useful for maintaining just one throughout the Visual Testing execution.
     */
    val handler by lazy {
        val handlerThread = HandlerThread("Coroutines callback").apply { start() }
        Handler(handlerThread.looper)
    }
}
