package com.muratcangzm.lunargaze.repository.remote

import com.muratcangzm.lunargaze.models.remote.tenor.TenorCategoryModel
import com.muratcangzm.lunargaze.models.remote.tenor.TenorSearchResultModel
import com.muratcangzm.lunargaze.service.TenorAPI
import com.muratcangzm.lunargaze.utils.DataResponse
import com.muratcangzm.lunargaze.utils.IoDispatcher
import com.muratcangzm.lunargaze.utils.log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TenorRepo @Inject
constructor(
    private val api: TenorAPI,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun fetchTenorCategory(): Flow<DataResponse<TenorCategoryModel>> = flow {

        try {
            val response = api.getTenorCategories()

            if (response.isSuccessful)
                emit(DataResponse.success(response.body()))
            else
                emit(DataResponse.error("Tenor Category Fetch Error!"))

        } catch (e: Exception) {
            log(e.message.toString())

        }

    }.flowOn(ioDispatcher)


    suspend fun fetchTenorSearchResult(query:String?): Flow<DataResponse<TenorSearchResultModel>> = flow {

        try {

             val response = query?.let { api.getSearchResult(q = query) }

            if (response != null && response.isSuccessful)
                emit(DataResponse.success(response.body()))
            else
                emit(DataResponse.error("Tenor Search Result Fetch Error!"))

        }
        catch (e:Exception){
            log(e.message.toString())
        }


    }.flowOn(ioDispatcher)
}