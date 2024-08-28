package com.muratcangzm.lunargaze.common

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardUtil {

    fun hideSoftKeyboard(activity: Activity) {
        val focusView = activity.currentFocus
        if (focusView != null) {
            val inputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(focusView.windowToken, 0)
        }
    }

    fun showSoftKeyboard(view: View) {
        val inputMethodManager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        inputMethodManager.showSoftInput(view, 0)
    }

}