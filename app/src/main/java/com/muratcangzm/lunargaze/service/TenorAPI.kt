package com.muratcangzm.lunargaze.service

import com.muratcangzm.lunargaze.BuildConfig
import com.muratcangzm.lunargaze.models.remote.tenor.ContentFilter
import com.muratcangzm.lunargaze.models.remote.tenor.TenorCategoryModel
import com.muratcangzm.lunargaze.models.remote.tenor.TenorSearchResultModel
import com.muratcangzm.lunargaze.utils.Constants
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Locale

interface TenorAPI {

    @GET(Constants.TENOR_CATEGORY)
    suspend fun getTenorCategories(
        @Query("key") apiKey: String = BuildConfig.TENOR_KEY,
        @Query("locale") locale: String = Locale.getDefault().language.lowercase(),
        @Query("contentfilter") contentFilter: ContentFilter = ContentFilter.low
    ): Response<TenorCategoryModel>


    @GET(Constants.TENOR_SEARCH)
    suspend fun getSearchResult(
        @Query("key") apiKey: String = BuildConfig.TENOR_KEY,
        @Query("q") q: String,
        @Query("locale") locale: String = Locale.getDefault().language.lowercase(),
        @Query("contentfilter") contentFilter: ContentFilter = ContentFilter.off,
        @Query("standard") standard: String = "standard",
        @Query("random") random: Boolean = true,
        @Query("limit") limit: Int = 50,

        ): Response<TenorSearchResultModel>

}