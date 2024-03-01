package com.muratcangzm.lunargaze.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ActivityContext

import javax.inject.Inject

class NetworkChecking
@Inject
constructor(@ActivityContext private val context: Context) {

    fun isNetworkAvailable() : Boolean{

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        return capabilities!!.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }


}