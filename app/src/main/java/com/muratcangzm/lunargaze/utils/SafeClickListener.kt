package com.muratcangzm.lunargaze.utils

import android.os.SystemClock
import android.view.View

class SafeClickListener(
    private val defaultInterval: Int = 1000,
    private val onSafeClick: (View) -> Unit
) : View.OnClickListener{

    private var lasTimeClicked: Long = 0

    override fun onClick(v: View) {
        if(SystemClock.elapsedRealtime() - lasTimeClicked < defaultInterval){
            return
        }
        lasTimeClicked = SystemClock.elapsedRealtime()
        onSafeClick(v)
    }

}