package com.muratcangzm.lunargaze.extensions

import android.view.View

fun View.showView(){
    visibility = View.VISIBLE
}

fun View.hideView(){
    visibility = View.INVISIBLE
}

fun View.goneView(){
    visibility = View.GONE
}