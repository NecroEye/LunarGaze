package com.muratcangzm.lunargaze.utils

import timber.log.Timber

private const val DEFAULT_DEBUG_TAG = "LunarGazeLogs"

fun log(
    message: String,
    tag: String = DEFAULT_DEBUG_TAG,
    isDebug: Boolean = true
) {
    Timber.tag(tag).d(message)
}