package com.genexus.android.uitesting.visual

class ScreenshotNotMatchingException(reference: String) : Exception("'$reference' doesn't match with previous screenshot")
