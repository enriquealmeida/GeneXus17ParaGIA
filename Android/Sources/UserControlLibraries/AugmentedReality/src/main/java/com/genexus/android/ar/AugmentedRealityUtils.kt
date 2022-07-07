package com.genexus.android.ar

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import com.genexus.android.core.base.services.Services

object AugmentedRealityUtils {
    private const val MIN_OPENGL_VERSION = 3.0

    /**
     * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
     * on this device.
     *
     * <p>Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
     *
     * <p>Finishes the activity if Sceneform can not run
     */
    fun checkIsSupportedDevice(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Services.Log.error(TAG, "Sceneform requires Android N or later")
            return false
        }

        val activityService = context.getSystemService(Context.ACTIVITY_SERVICE)
        if (activityService is ActivityManager) {
            val openGlVersionString = activityService.getDeviceConfigurationInfo().getGlEsVersion()
            if (openGlVersionString.toDouble() < MIN_OPENGL_VERSION) {
                Services.Log.error(TAG, "Sceneform requires OpenGL ES 3.0 later")
                return false
            }
        }

        return true
    }
}
