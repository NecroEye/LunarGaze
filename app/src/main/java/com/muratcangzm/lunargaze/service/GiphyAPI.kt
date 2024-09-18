package com.muratcangzm.lunargaze.service

import com.muratcangzm.lunargaze.BuildConfig
import com.muratcangzm.models.remote.giphy.CategoryModel
import com.muratcangzm.models.remote.giphy.ChannelModel
import com.muratcangzm.models.remote.giphy.SearchModel
import com.muratcangzm.lunargaze.common.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Locale

interface GiphyAPI {

    @GET(Constants.CATEGORIES)
    suspend fun getCategory(
        @Query("api_key") apiKey: String = BuildConfig.GIPHY_KEY,
    )
            : Response<CategoryModel>

    @GET(Constants.CHANNELS)
    suspend fun getChannels(
        @Query("api_key") apiKey: String = BuildConfig.GIPHY_KEY,
        @Query("q") query: String,
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int?
    )
            : Response<ChannelModel>

    @GET(Constants.SEARCH)
    suspend fun getSearch(
        @Query("api_key") apiKey: String = BuildConfig.GIPHY_KEY,
        @Query("q") query:String,
        @Query("limit") limit: Int = 50,
        @Query("rating") rating: String = "g",
        @Query("lang") language: String = Locale.getDefault().language.lowercase(),
        @Query("bundle") bundle: String = "messaging_non_clips",
    )
            : Response<SearchModel>
}
