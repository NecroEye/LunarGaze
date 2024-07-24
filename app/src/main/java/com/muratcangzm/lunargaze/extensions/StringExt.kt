package com.muratcangzm.lunargaze.extensions

import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Patterns
import java.util.regex.Pattern


fun String.toHtml(): Spanned {

    return Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)

}

fun String?.asUri(): Uri?{
       return try {
              Uri.parse(this)
       } catch (e:Exception){
              null
       }
}

fun String.isEmailValid() = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()