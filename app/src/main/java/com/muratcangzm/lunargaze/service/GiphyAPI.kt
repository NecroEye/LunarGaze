package com.muratcangzm.lunargaze.service

import com.muratcangzm.lunargaze.models.remote.CategoryModel
import com.muratcangzm.lunargaze.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyAPI {

    @GET(Constants.CATEGORIES)
    suspend fun getCategory(@Query("api_key") apiKey: String = "00OYOh871nJ0XtRFIbWgQzvORuV7M3kx" )
            : Response<CategoryModel>

}