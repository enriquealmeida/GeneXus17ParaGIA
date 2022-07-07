package com.genexus.android.core.common.okhttp

import android.net.TrafficStats
import android.os.Build
import com.genexus.android.R
import com.genexus.android.core.base.services.Services
import okhttp3.CertificatePinner
import okhttp3.JavaNetCookieJar
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.CookieManager
import java.util.concurrent.TimeUnit

class OkHttpClientHelper {

    // default client.
    private var okHttpClient: OkHttpClient? = null

    // get default http client.
    var defaultClient: OkHttpClient? = null
        get() {
            if (okHttpClient == null) {
                val clientBuilder = OkHttpClient.Builder()
                //
                initHttpClient(clientBuilder)
                okHttpClient = clientBuilder.build()
            }
            return okHttpClient // as OkHttpClient
        }

    companion object {
        private const val DEFAULT_CONNECTION_TIMEOUT = 5 // 5 seconds
        private const val DEFAULT_SOCKET_TIMEOUT = 60 // 60 seconds.
        private val JSON: MediaType = "application/json; charset=utf-8".toMediaType()

        // init cookie manager
        var cookieManager: CookieManager = CookieManager()

        //
        /*
		getCustomClient , get client with custom timeout and redirect
		params
		clientSoTimeout in seconds,
		followRedirects default is true
		 */
        @JvmStatic
        fun getCustomClient(clientSoTimeout: Int, followRedirects: Boolean): OkHttpClient {
            val clientBuilder = OkHttpClient.Builder()
            //
            initHttpClient(clientBuilder)

            // init Custom httpclient:
            // follow Redirects, default true
            clientBuilder.followRedirects(followRedirects)

            // custom timeout, in seconds.
            if (clientSoTimeout > 0) {
                clientBuilder.readTimeout(clientSoTimeout.toLong(), TimeUnit.SECONDS)
                clientBuilder.writeTimeout(clientSoTimeout.toLong(), TimeUnit.SECONDS)
            }
            return clientBuilder.build()
        }

        @JvmStatic
        fun getRequestBuilder(uri: String): Request.Builder {
            val requestBuilder = Request.Builder()
            requestBuilder.url(uri)
            return requestBuilder
        }

        @JvmStatic
        fun getRequestBody(sData: String): RequestBody {
            return sData.toRequestBody(JSON)
        }

        private fun initHttpClient(clientBuilder: OkHttpClient.Builder) {

            // Use default connections parameters from OkHttp
            // SNI already supported in OkHttp 5.x and up.
            // https://square.github.io/okhttp/4.x/okhttp/okhttp3/-connection/#modern-tls

            // config socket stats, to avoid Strict Mode violation.
            // from : https://github.com/square/okhttp/issues/3537
            // and https://stackoverflow.com/a/57963770
            TrafficStats.setThreadStatsTag(10042)

            // network security config works in N or above, use custom verifier bellow N
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                if (pinSet.size > 0) {
                    // config okhttp SSL Pinning:
                    // https://square.github.io/okhttp/4.x/okhttp/okhttp3/-certificate-pinner/
                    // Set SSL pining Android bellow N
                    val hostname = Services.Application.get().UriMaker.rootUrlHostName
                    val certificatePinnerBuilder = CertificatePinner.Builder()
                    for (pinValue in pinSet) certificatePinnerBuilder.add(hostname, "sha256/" + pinValue)
                    val certificatePinner = certificatePinnerBuilder.build()
                    clientBuilder.certificatePinner(certificatePinner)
                }
            }

            // Set timeouts.
            // https://www.baeldung.com/okhttp-timeouts
            // Connection Timeout - timeout in milliseconds until a connection is established.
            // Socket Timeout - timeout in milliseconds while waiting for the server to send data.
            clientBuilder.connectTimeout(DEFAULT_CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS).readTimeout(DEFAULT_SOCKET_TIMEOUT.toLong(), TimeUnit.SECONDS)

            // GZip Compression: enabled by default in okHttp
            // https://medium.com/tech-insider/okhttps-gzip-compression-904919638458

            // Set Cookies Manager: keep session cookie.
            // https://square.github.io/okhttp/4.x/okhttp/okhttp3/-cookie-jar/
            // https://gist.github.com/scitbiz/8cb6d8484bb20e47d241cc8e117fa705
            clientBuilder.cookieJar(JavaNetCookieJar(cookieManager))
        }

        // SSL Pinning
        val pinSet: Array<String>
            get() = Services.Application.appContext.resources.getStringArray(R.array.serverPinSet)
    }
}
