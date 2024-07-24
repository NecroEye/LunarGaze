package com.muratcangzm.lunargaze.extensions

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.RippleDrawable
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.muratcangzm.lunargaze.utils.SafeClickListener

fun View.showView() {
    visibility = View.VISIBLE
}

fun View.hideView() {
    visibility = View.INVISIBLE
}

fun View.goneView() {
    visibility = View.GONE
}

fun View.enable() {
    isEnabled = true
    this.alpha = 1f
}

fun View.disable() {
    isEnabled = false
    this.alpha = 0.5f
}

inline fun View.afterMeasured(crossinline f: View.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit){

    val safeClickListener = SafeClickListener{
        onSafeClick(it)
    }
   setOnClickListener(safeClickListener)
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

