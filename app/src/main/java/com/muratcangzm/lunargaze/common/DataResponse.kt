package com.muratcangzm.lunargaze.common

import javax.annotation.concurrent.Immutable

@Immutable
class DataResponse<out T>
    (val status: Status, val data: T? = null, val message: String? = null) {

    enum class Status {
        SUCCESS, LOADING, ERROR
    }

    companion object {

        fun <T> success(data: T?): DataResponse<T> {
            return DataResponse(Status.SUCCESS, data)
        }

        fun <T> loading(): DataResponse<T> {
            return DataResponse(Status.LOADING)
        }

        fun <T> error(message: String?): DataResponse<T> {
            return DataResponse(Status.ERROR, message = message)
        }
    }
}