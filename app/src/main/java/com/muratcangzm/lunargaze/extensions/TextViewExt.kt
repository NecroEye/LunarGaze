package com.muratcangzm.lunargaze.extensions

import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView

fun TextView.underline() {
    this.paintFlags = this.paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

fun TextView.removeUnderline() {
    this.paintFlags = this.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
}

fun TextView.writeTextSlowly(text: String, delay: Long = 0, onStart: (() -> Unit)? = null, onComplete: (() -> Unit)? = null) {
    var charIndex = 0
    val handler = Handler(Looper.getMainLooper())
    val typingRunnable = object : Runnable {

        override fun run() {
            if (charIndex == 0) {
                onStart?.invoke()
            }
            val currentText = text.substring(0, charIndex++)
            this@writeTextSlowly.text = currentText
            if (charIndex <= text.length) {
                handler.postDelayed(this, delay)
            } else {
                handler.removeCallbacks(this)
                onComplete?.invoke()
            }
        }
    }

    handler.post(typingRunnable)
}

fun TextView.htmlText(text: String) {
    this.text = text.toHtml()
    this.movementMethod = LinkMovementMethod.getInstance()
}

fun TextView.handleUrlClicks(onClicked: ((String) -> Unit)? = null) {
    //create span builder and replaces current text with it
    text = SpannableStringBuilder.valueOf(text).apply {
        //search for all URL spans and replace all spans with our own clickable spans
        getSpans(0, length, URLSpan::class.java).forEach {
            //add new clickable span at the same position
            setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        onClicked?.invoke(it.url)
                    }
                },
                getSpanStart(it),
                getSpanEnd(it),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
            //remove old URLSpan
            removeSpan(it)
        }
    }
    //make sure movement method is set
    movementMethod = LinkMovementMethod.getInstance()
}