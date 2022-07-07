package com.genexus.android.core.tasking

abstract class Task<TResult, TError>(private val name: String) {

    private val successListeners = mutableListOf<OnSuccessListener<TResult>>()
    private val failureListeners = mutableListOf<OnFailureListener<TError>>()
    private val completeListeners = mutableListOf<OnCompleteListener<TResult, TError>>()
    private val cancelListeners = mutableListOf<OnCancelListener>()

    var isComplete = false
        private set
    var isSuccessful = false
        private set
    var isCanceled = false
        private set

    var result: TResult? = null
        private set
    var error: TError? = null
        private set

    protected fun execute(runnable: Runnable) {
        Thread(runnable, name).start()
    }

    fun addOnSuccessListener(listener: OnSuccessListener<TResult>): Task<TResult, TError> {
        successListeners.add(listener)
        return this
    }

    fun addOnFailureListener(listener: OnFailureListener<TError>): Task<TResult, TError> {
        failureListeners.add(listener)
        return this
    }

    fun addOnCompleteListener(listener: OnCompleteListener<TResult, TError>): Task<TResult, TError> {
        completeListeners.add(listener)
        return this
    }

    fun addOnCancelListener(listener: OnCancelListener): Task<TResult, TError> {
        cancelListeners.add(listener)
        return this
    }

    fun cancel() {
        isCanceled = true
        onCancel()
    }

    fun onSuccess(r: TResult) {
        result = r
        isSuccessful = true
        onComplete()
        for (l in successListeners)
            l.onSuccess(r)
    }

    fun onFailure(e: TError) {
        error = e
        isSuccessful = false
        onComplete()
        for (l in failureListeners)
            l.onFailure(e)
    }

    private fun onComplete() {
        isComplete = true
        for (l in completeListeners)
            l.onComplete(this)
    }

    private fun onCancel() {
        isComplete = false
        for (l in cancelListeners)
            l.onCanceled()
    }
}
