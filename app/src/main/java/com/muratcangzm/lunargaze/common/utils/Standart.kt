package com.muratcangzm.lunargaze.common.utils

import timber.log.Timber

inline fun <reified T> tryOrNull(block: () -> T): T? {
    return try {
        block()
    } catch (e: Exception) {
        Timber.e(e.message.orEmpty())
        null
    }
}

inline fun tryOrZero(block: () -> Double): Double {
    return try {
        block()
    } catch (e: Exception) {
        Timber.e(e.message.orEmpty())
        0.0
    }
}

inline fun <reified T> tryOrLog(block: () -> T) {
    try {
        block()
    } catch (e: Exception) {
        Timber.e(e.message.orEmpty())
    }
}
@Suppress("UNCHECKED_CAST")
fun <T> safeCast(value: Any?): T? {
    return value as? T
}