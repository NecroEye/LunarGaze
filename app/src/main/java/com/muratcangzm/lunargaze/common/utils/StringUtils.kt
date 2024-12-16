package com.muratcangzm.lunargaze.common.utils

import android.net.Uri
import android.text.Html
import android.text.Spanned
import android.util.Patterns
import java.util.Locale

fun String.toHtml(): Spanned = Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)

fun String?.asUri(): Uri? = tryOrNull<Uri?> { return Uri.parse(this) }

fun String.removeWhiteSpaces(): String {
    return this.replace("\\s".toRegex(), "")
}

fun String.lenEqOrGt(length: Int): Boolean = this.length >= length

fun String.isEmailValid() = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.toVersionNumber(): Int = this.replace(".", "").toInt()

fun String.capitalized(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase())
            it.titlecase(Locale.getDefault())
        else it.toString()
    }
}