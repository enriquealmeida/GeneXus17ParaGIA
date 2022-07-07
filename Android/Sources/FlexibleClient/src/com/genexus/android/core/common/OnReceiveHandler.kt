package com.genexus.android.core.common

interface OnReceiveHandler<T> {
    fun receive(t: T?)
}
