package com.muratcangzm.lunargaze.service

import com.muratcangzm.lunargaze.models.remote.CategoryModel
import com.muratcangzm.lunargaze.models.remote.ChannelModel
import com.muratcangzm.lunargaze.models.remote.SearchModel
import com.muratcangzm.lunargaze.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyAPI {

    @GET(Constants.CATEGORIES)
    suspend fun getCategory(
        @Query("api_key") apiKey: String = "00OYOh871nJ0XtRFIbWgQzvORuV7M3kx"
    )
            : Response<CategoryModel>


    @GET(Constants.CHANNELS)
    suspend fun getChannels(
        @Query("api_key") apiKey: String = "00OYOh871nJ0XtRFIbWgQzvORuV7M3kx",
        @Query("q") query: String,
        @Query("limit") limit: Int = 50
    )
            : Response<ChannelModel>

    @GET(Constants.SEARCH)
    suspend fun getSearch(
        @Query("api_key") apiKey: String = "00OYOh871nJ0XtRFIbWgQzvORuV7M3kx",
        @Query("q") query:String,
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0,
        @Query("rating") rating: String = "g",
        @Query("lang") language: String = "en",
        @Query("bundle") bundle: String = "messaging_non_clips"
    )
            : Response<SearchModel>
}
