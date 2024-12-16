package com.muratcangzm.lunargaze

import android.app.Application
import com.muratcangzm.lunargaze.common.utils.Constants
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class LunarApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (Constants.DEBUG)
            Timber.plant(Timber.DebugTree())
    }
}