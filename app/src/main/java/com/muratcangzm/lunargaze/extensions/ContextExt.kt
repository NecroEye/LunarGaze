package com.muratcangzm.lunargaze.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

fun Context.openSettings(){
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    this.startActivity(intent)
}

fun Context.openOnBrowser(url:String){
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    this.startActivity(intent)
}