package com.muratcangzm.lunargaze.common.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.IntRange
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ActivityContext

import javax.inject.Inject

class NetworkChecking
@Inject
constructor(@ActivityContext private val context: Context) {

    fun isNetworkAvailable(): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        return capabilities!!.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("ServiceCast", "Range")
    fun getWifiSpeed(): Int {

        val wifiManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as WifiManager
        val linkSpeed = wifiManager.connectionInfo.rssi

        return wifiManager.calculateSignalLevel(linkSpeed)

    }


    fun getMobileSpeed() : Pair<Int?, Int?>{

        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities = connectivity.getNetworkCapabilities(connectivity.activeNetwork)
        val downSpeed = networkCapabilities?.linkDownstreamBandwidthKbps
        val upSpeed = networkCapabilities?.linkUpstreamBandwidthKbps

        return Pair(downSpeed, upSpeed)
    }

}