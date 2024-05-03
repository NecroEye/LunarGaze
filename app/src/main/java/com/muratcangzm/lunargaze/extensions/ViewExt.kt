package com.muratcangzm.lunargaze.extensions

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.RippleDrawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun View.showView(){
    visibility = View.VISIBLE
}

fun View.hideView(){
    visibility = View.INVISIBLE
}

fun View.goneView(){
    visibility = View.GONE
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

fun View.setBackgroundColorRes(@ColorRes colorRes: Int) {
    setBackgroundColor(ContextCompat.getColor(context, colorRes))
}

fun ImageView.setTint(@ColorRes colorRes: Int) {
    setColorFilter(ContextCompat.getColor(context, colorRes), PorterDuff.Mode.SRC_IN)
}

fun View.fadeIn(duration: Long = 300) {
    alpha = 0f
    visibility = View.VISIBLE
    animate().alpha(1f).setDuration(duration).start()
}

fun View.fadeOut(duration: Long = 300) {
    animate().alpha(0f).setDuration(duration).withEndAction { visibility = View.GONE }.start()
}

fun View.setRippleBackground(@ColorInt color: Int) {
    background = RippleDrawable(ColorStateList.valueOf(color), background, null)
}

