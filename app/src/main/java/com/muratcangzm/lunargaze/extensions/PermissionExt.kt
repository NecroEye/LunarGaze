package com.muratcangzm.lunargaze.extensions

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

fun Context.isStoragePermissionGranted(): Boolean {

    return if (Build.VERSION.SDK_INT < 33) {

        val readExternalStorageGranted =
            this.checkCallingOrSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val writeExternalStorageGranted =
            this.checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        readExternalStorageGranted == PackageManager.PERMISSION_GRANTED && writeExternalStorageGranted == PackageManager.PERMISSION_GRANTED
    } else {

        val readMediaImageGranted =
            this.checkCallingOrSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES)
        readMediaImageGranted == PackageManager.PERMISSION_GRANTED

    }
}

fun Context.isNotificationPermissionGranted(): Boolean {

    return if (Build.VERSION.SDK_INT < 33)
        true
    else {
        val granted =
            this.checkCallingOrSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
        granted == PackageManager.PERMISSION_GRANTED
    }
}


fun Context.isLocationPermissionGranted(): Boolean {

    val grantedLocation =
        this.checkCallingOrSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    return grantedLocation == PackageManager.PERMISSION_GRANTED

}