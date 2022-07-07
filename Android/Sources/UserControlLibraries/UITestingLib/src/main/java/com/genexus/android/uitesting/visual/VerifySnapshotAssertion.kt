package com.genexus.android.uitesting.visual

import android.view.View
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class VerifySnapshotAssertion(private val reference: String) : ViewAssertion {
    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        if (view == null && noViewFoundException != null)
            throw noViewFoundException

        val files = FilesOperations(reference)
        val remoteBitmap = files.getRemote() ?: throw ImageNotFoundException()
        val localBitmap = files.getLocal()
        if (!Bitmap(remoteBitmap).sameAs(localBitmap)) {
            files.getLocalFile()?.let {
                runBlocking(Dispatchers.IO) {
                    NetworkOperations(reference).setResource(it)
                }
            }

            throw ScreenshotNotMatchingException(reference)
        }
    }
}
