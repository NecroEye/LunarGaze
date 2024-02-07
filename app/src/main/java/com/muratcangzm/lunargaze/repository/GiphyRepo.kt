package com.muratcangzm.lunargaze.repository

import android.util.Log
import com.muratcangzm.lunargaze.models.remote.CategoryModel
import com.muratcangzm.lunargaze.service.GiphyAPI
import com.muratcangzm.lunargaze.utils.DataResponse

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class GiphyRepo(private val api: GiphyAPI) {

    suspend fun fetchCategories(): Flow<DataResponse<CategoryModel>> = flow {
        try {

            val response = api.getCategory()

            if (response.isSuccessful)
                emit(DataResponse.success(response.body()))
            else
                emit(DataResponse.error("Network error, please try again later!"))


        } catch (e: Exception) {
            Log.d("Api Error", "${e.message}")
        }

    }


}