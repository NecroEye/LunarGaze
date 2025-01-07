package com.muratcangzm.lunargaze.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.muratcangzm.lunargaze.common.utils.tryOrLog
import java.util.Locale


fun Context.openNotificationSettings() {
    val intent = Intent()
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, this.packageName)
        }

        else -> {
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.putExtra("app_package", this.packageName)
            intent.putExtra("app_uid", this.applicationInfo.uid)
        }
    }
    tryOrLog { this.startActivity(intent) }
}

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

fun Context.getUserCountry(): String {
    try {
        val tm: TelephonyManager =
            this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var countryIso: String = tm.simCountryIso

        if (countryIso.length == 2) { // SIM country code is available
            countryIso.lowercase(Locale.US)
        } else if (tm.phoneType != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
            val networkCountry: String = tm.networkCountryIso
            if (networkCountry.length == 2) { // network country code is available
                countryIso = networkCountry.lowercase(Locale.US)
            }
        }
        for (countryCode in countryIso) {
            val locale = Locale("", countryIso)
            val countryName = locale.displayCountry
            return countryName.substring(0, 1).uppercase() + countryName.substring(1).lowercase()
        }
    } catch (e: Exception) {
        return Locale.getDefault().displayCountry
    }
    return Locale.getDefault().displayCountry
}

fun Context.getUserCountryIso(): String {
    return try {
        val tm: TelephonyManager =
            this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var countryIso: String = tm.simCountryIso
        if (countryIso.length == 2) {
            countryIso.lowercase(Locale.US)
        } else if (tm.phoneType != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
            val networkCountry: String = tm.networkCountryIso
            if (networkCountry.length == 2) {
                countryIso = networkCountry.lowercase(Locale.US)
            }
        }
        if (countryIso.isEmpty()) {
            countryIso = Locale.getDefault().country.lowercase(Locale.US)
        }
        countryIso
    } catch (e: Exception) {
        Locale.getDefault().country.lowercase(Locale.US)
    }
}

val Context.versionName: String
    get() = packageManager
        .getPackageInfo(packageName, 0)
        .versionName.toString()

val Context.versionCode:Long
    get() = when{
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> {
            packageManager
                .getPackageInfo(packageName, 0)
                .longVersionCode
        }
        else -> {
            packageManager
                .getPackageInfo(packageName, 0)
                .versionCode.toLong()
        }
    }