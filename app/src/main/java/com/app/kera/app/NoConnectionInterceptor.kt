package com.app.kera.app

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class NoConnectionInterceptor(
    private val context: Context
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {


        return if (!isConnectionOn()) {

            throw NoConnectivityException()

        } else if(!isInternetAvailable()) {

            throw NoInternetException()
        } else {
            chain.proceed(chain.request())
        }
    }
    // ... Other Codes ...
    private fun isConnectionOn(): Boolean {
        val connectivityManager =
            context. getSystemService(Context.CONNECTIVITY_SERVICE) as
                    ConnectivityManager

        return if (android.os.Build.VERSION.SDK_INT >=
            android.os.Build.VERSION_CODES.M) {
            postAndroidMInternetCheck(connectivityManager)
        } else {
            preAndroidMInternetCheck(connectivityManager)
        }
    }
    private fun preAndroidMInternetCheck(
        connectivityManager: ConnectivityManager
    ): Boolean {
        val activeNetwork = connectivityManager.activeNetworkInfo
        if (activeNetwork != null) {
            return (activeNetwork.type == ConnectivityManager.TYPE_WIFI ||
                    activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
        }
        return false
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun postAndroidMInternetCheck(
        connectivityManager: ConnectivityManager
    ): Boolean {
        val network = connectivityManager.activeNetwork
        val connection =
            connectivityManager.getNetworkCapabilities(network)

        return connection != null && (
                connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }
    private fun isInternetAvailable(): Boolean {
        return try {
            val timeoutMs = 1500
            val sock = Socket()
            val sockaddr = InetSocketAddress("8.8.8.8", 53) //special connection to Google Public DNS (i.e. 8.8.8.8) to check.

            sock.connect(sockaddr, timeoutMs)
            sock.close()

            true
        } catch (e: IOException) {
            false
        }
    }
    class NoConnectivityException : IOException() {

        override val message: String
            get() =
                "No network available, please check your WiFi or Data connection"

    }

    class NoInternetException() : IOException() {
        override val message: String
            get() =
                "No internet available, please check your connected WIFi or Data"
    }

}