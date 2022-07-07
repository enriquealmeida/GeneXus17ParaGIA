package com.genexus.android

import android.app.Activity
import com.genexus.android.core.base.utils.Function
import com.genexus.android.core.base.utils.ResultRunnable
import com.genexus.android.core.base.utils.Strings

class WithBackgroundPermission<T>(private val activity: Activity?, private val requestPermissions: Array<String>? = null, private val neededPermissions: Array<String>) {

    var blockThread = false
    var attachToController = false
    var rationale = Strings.EMPTY
    var successCode: ResultRunnable<T>? = null
    var failureCode: ResultRunnable<T>? = null
    var onPermissionRequested: Function<WithPermission<T>, T>? = null

    fun run(): T {
        return run(requestPermissions, neededPermissions, successCode)
    }

    private fun run(request: Array<String>?, needed: Array<String>, successCode: ResultRunnable<T>?): T {
        val neededList = needed.toMutableList()
        val requestList = request?.toMutableList() ?: ArrayList(neededList)
        val separateBackground = PermissionUtil.shouldRequestBackgroundLocationSeparately(activity, neededList)
        if (separateBackground) {
            neededList.remove(LocationAccuracy.FINE)
            requestList.remove(LocationAccuracy.BACKGROUND)
        }

        val finalCode = if (separateBackground)
            ResultRunnable { run(null, arrayOf(LocationAccuracy.BACKGROUND), successCode) }
        else
            successCode

        val builder = WithPermission.Builder<T>(activity)
            .require(neededList.toTypedArray())
            .request(requestList.toTypedArray())
            .setBlockThread(blockThread)
            .setRationale(rationale)
            .onFailure(failureCode)
            .onPermissionRequested(onPermissionRequested)

        if (finalCode != null)
            builder.onSuccess(finalCode)

        if (attachToController)
            builder.attachToActivityController()

        return builder.build().run()
    }
}
