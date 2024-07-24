package com.muratcangzm.lunargaze.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


fun <T> handleRequestFlow(call: suspend () -> T): Flow<DataResponse<T>> {

    return flow {
        emit(DataResponse.loading())
        emit(DataResponse.success(call()))
    }.catch {
        emit(DataResponse.error(it.message))
    }.flowOn(Dispatchers.IO)

}