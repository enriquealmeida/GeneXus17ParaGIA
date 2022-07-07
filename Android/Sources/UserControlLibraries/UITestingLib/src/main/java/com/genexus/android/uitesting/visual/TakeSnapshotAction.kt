package com.genexus.android.uitesting.visual

import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.view.PixelCopy
import android.view.View
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.genexus.android.core.activities.ActivityHelper
import com.genexus.android.core.utils.CoroutinesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matcher
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TakeSnapshotAction(private val reference: String) : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return isDisplayed()
    }

    override fun getDescription(): String {
        return "take snapshot"
    }

    override fun perform(uiController: UiController?, view: View?) {
        if (view == null || !ActivityHelper.hasCurrentActivity())
            throw IllegalStateException("Either no view or current activity found")

        val activity = ActivityHelper.getCurrentActivity()
        val localBitmap = getViewBitmap(view, activity.window)
        runBlocking(Dispatchers.IO) {
            val network = NetworkOperations(reference)
            val files = FilesOperations(reference)
            val localFilePath = files.saveLocal(activity, localBitmap)
            val remoteBitmap = network.getResource(activity)
            if (remoteBitmap != null)
                files.saveRemote(activity, remoteBitmap)
            else {
                network.setResource(File(localFilePath))
                throw ImageNotFoundException()
            }
        }
    }

    private fun getViewBitmap(view: View, window: Window): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            getScreenshot(view, window)
        else
            getScreenShot(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getScreenshot(view: View, window: Window): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val locationOfViewInWindow = IntArray(2)
        view.getLocationInWindow(locationOfViewInWindow)
        return runBlocking(Dispatchers.Default) {
            suspendCoroutine<Bitmap> {
                PixelCopy.request(
                    window,
                    Rect(
                        locationOfViewInWindow[0],
                        locationOfViewInWindow[1],
                        locationOfViewInWindow[0] + view.width,
                        locationOfViewInWindow[1] + view.height
                    ),
                    bitmap,
                    { copyResult ->
                        if (copyResult == PixelCopy.SUCCESS)
                            it.resume(bitmap)
                        else
                            it.resumeWithException(RuntimeException("Couldn't get view snapshot"))
                    },
                    CoroutinesHelper.handler
                )
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun getScreenShot(view: View): Bitmap {
        view.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(view.drawingCache)
        view.isDrawingCacheEnabled = false
        return bitmap
    }
}
