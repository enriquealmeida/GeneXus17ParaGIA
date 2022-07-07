package com.genexus.android.core.tasking

interface OnCompleteListener<TResult, TError> {
    fun onComplete(task: Task<TResult, TError>)
}
