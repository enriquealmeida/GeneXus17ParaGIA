package com.genexus.android.uitesting.visual

open class VisualTestingServiceException(message: String?) : Exception("An error occurred while communicating with Visual Testing service: $message") {
    constructor() : this(null)
}
