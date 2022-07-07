package com.genexus.android.remoteconfig

import java.lang.Exception

interface RemoteConfigCompletedListener {
    fun onSuccess()
    fun onFailure(exception: Exception?)
}
