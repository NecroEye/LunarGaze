package com.muratcangzm.lunargaze.repository

import android.util.Log
import com.muratcangzm.lunargaze.models.remote.CategoryModel
import com.muratcangzm.lunargaze.models.remote.ChannelModel
import com.muratcangzm.lunargaze.models.remote.SearchModel
import com.muratcangzm.lunargaze.service.GiphyAPI
import com.muratcangzm.lunargaze.utils.DataResponse
import com.muratcangzm.lunargaze.utils.log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import timber.log.Timber

class GiphyRepo(private val api: GiphyAPI) {

    suspend fun fetchCategories(): Flow<DataResponse<CategoryModel>> = flow {

        try {

            val response = api.getCategory()

            if (response.isSuccessful)
                emit(DataResponse.success(response.body()))
            else
                emit(DataResponse.error("Network error, please try again later!"))


        } catch (e: Exception) {
            Timber.tag("Api Error").d(e)
        }

    }


    suspend fun fetchChannels(search: String, offset: Int?): Flow<DataResponse<ChannelModel>> = flow {

        try {

            val response = api.getChannels(query = search, offset = offset)

            if (response.isSuccessful)
                emit(DataResponse.success(response.body()))
            else
                emit(DataResponse.error("Network error, please try again later!"))


        } catch (e: Exception) {
            Timber.tag("Api Error").d(e)

        }

    }

    suspend fun fetchSearch(search: String): Flow<DataResponse<SearchModel>> = flow {

        try{

            val response = api.getSearch(query = search)

            if (response.isSuccessful)
                emit(DataResponse.success(response.body()))
            else
                emit(DataResponse.error("Network error, please try again later!"))

        }
        catch (e:Exception){
            log(e.message.toString())

        }


    }



}