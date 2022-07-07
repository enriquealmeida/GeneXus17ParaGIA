package com.genexus.android.core.common

import com.genexus.android.core.base.services.Services

abstract class ForPostOnUiThread<T> : OnReceiveHandler<T> {
    override fun receive(t: T?) {
        Services.Device.runOnUiThread { posted(t) }
    }

    protected abstract fun posted(t: T?)
}
