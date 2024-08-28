package com.muratcangzm.lunargaze.repository.remote

import com.muratcangzm.lunargaze.common.utils.DataResponse
import com.muratcangzm.lunargaze.common.utils.IoDispatcher
import com.muratcangzm.lunargaze.common.utils.log
import com.muratcangzm.lunargaze.models.remote.giphy.CategoryModel
import com.muratcangzm.lunargaze.models.remote.giphy.ChannelModel
import com.muratcangzm.lunargaze.models.remote.giphy.SearchModel
import com.muratcangzm.lunargaze.service.GiphyAPI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GiphyRepo @Inject constructor(
    private val api: GiphyAPI,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    private var categoryCache: DataResponse<CategoryModel>? = null

    suspend fun fetchCategories(): Flow<DataResponse<CategoryModel>> = flow {
        try {
            val response = api.getCategory()
            if (response.isSuccessful) {
                categoryCache = DataResponse.success(response.body())

                categoryCache?.let {
                    emit(categoryCache!!)
                    return@let
                }
                emit(DataResponse.success(response.body()))
            } else {
                emit(DataResponse.error("Network error, please try again later!"))
            }

        } catch (e: Exception) {
            Timber.tag("Api Error").d(e)
            emit(DataResponse.error("Failed to fetch categories ${e.cause} ${e.message}" ))

        }

    }.flowOn(ioDispatcher)


    suspend fun fetchChannels(search: String, offset: Int?): Flow<DataResponse<ChannelModel>> =
        flow {
            try {
                val response = api.getChannels(query = search, offset = offset)

                if (response.isSuccessful)
                    emit(DataResponse.success(response.body()))
                else
                    emit(DataResponse.error("Network error, please try again later!"))

            } catch (e: Exception) {
                Timber.tag("Api Error").d(e)
            }
        }.flowOn(ioDispatcher)

    suspend fun fetchSearch(search: String): Flow<DataResponse<SearchModel>> = flow {
        try {
            val response = api.getSearch(query = search)

            if (response.isSuccessful)
                emit(DataResponse.success(response.body()))
            else
                emit(DataResponse.error("Network error, please try again later!"))

        } catch (e: Exception) {
            log(e.message.toString())

        }
    }.flowOn(ioDispatcher)

}