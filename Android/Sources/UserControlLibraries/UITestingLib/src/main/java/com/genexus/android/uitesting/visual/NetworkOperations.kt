package com.genexus.android.uitesting.visual

import android.app.Activity
import android.graphics.Bitmap
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.utils.FileUtils2
import com.genexus.android.core.utils.UITestingUtils
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.net.HttpURLConnection

class NetworkOperations(private val reference: String) {
    fun getResource(activity: Activity): Bitmap? {
        checkReachable(GET_SERVICE)
        val parameters = getServiceGetParameters()
        val response = Services.HttpService.postJson(GET_SERVICE, parameters)
        if (!response.responseOk)
            throw VisualTestingServiceException()
        else {
            val image = response.Data.optString("image")
            if (image.isNullOrEmpty())
                return null

            return Services.Images.getBitmap(activity, image, true)
        }
    }

    fun setResource(file: File) {
        checkReachable(GX_OBJECT)
        val uploadImageResponse = Services.HttpService.uploadInputStreamToServer(
            GX_OBJECT,
            FileInputStream(file),
            file.length(),
            FileUtils2.getMimeType(file),
            null
        )

        if (uploadImageResponse.HttpCode == HttpURLConnection.HTTP_CREATED) {
            val parameters = getServiceSetParameters(uploadImageResponse["object_id"])
            val setResourceResponse = Services.HttpService.postJson(SET_SERVICE, parameters)
            if (setResourceResponse.responseOk)
                return
        }

        throw VisualTestingServiceException()
    }

    private fun getServiceSetParameters(objectId: String): JSONObject {
        return getServiceGetParameters().apply {
            put("image", objectId)
        }
    }

    private fun getServiceGetParameters(): JSONObject {
        return JSONObject().apply {
            put("projectCode", UITestingUtils.currentTestClass)
            put("testCode", UITestingUtils.currentTest)
            put("resourceReference", reference)
            put("platform", PLATFORM_ANDROID)
        }
    }

    private fun checkReachable(service: String) {
        if (!Services.HttpService.isReachable(service))
            throw VisualTestingServiceException()
    }

    init {
        require(reference.isNotEmpty()) { "Reference cannot be empty" }
    }

    companion object {
        private val SERVICE = UITestingUtils.visualTestingServer
        private val GET_SERVICE = SERVICE + "GetResource"
        private val SET_SERVICE = SERVICE + "SetResource"
        private val GX_OBJECT = "$SET_SERVICE/gxobject"
        private const val PLATFORM_ANDROID = 2 // 1-iOS, 2-Android
    }
}
