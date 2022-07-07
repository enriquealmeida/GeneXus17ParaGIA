package com.genexus.android.core.tasking

interface OnFailureListener<T> {
    fun onFailure(error: T)
}
