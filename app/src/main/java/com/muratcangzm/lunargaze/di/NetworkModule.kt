package com.muratcangzm.lunargaze.di


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.muratcangzm.lunargaze.service.GiphyAPI
import com.muratcangzm.lunargaze.service.TenorAPI
import com.muratcangzm.lunargaze.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    @Named("giphyBaseUrl")
    fun provideGiphyBaseUrl() = Constants.BASE_URL

    @Singleton
    @Provides
    @Named("tenorBaseUrl")
    fun provideTenorBaseUrl() = Constants.TENOR_BASE


    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().setLenient().create()


    @Singleton
    @Provides
    fun provideOkhttpClient() = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .followRedirects(true)
        .followSslRedirects(true)
        .retryOnConnectionFailure(true)
        .build()


    @Singleton
    @Provides
    fun provideGiphyApi(
        @Named("giphyBaseUrl") baseUrl: String,
        gson: Gson,
        client: OkHttpClient
    ): GiphyAPI {

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(GiphyAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideTenorApi(
        @Named("tenorBaseUrl") baseUrl: String,
        gson: Gson,
        client: OkHttpClient
    ): TenorAPI {

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(TenorAPI::class.java)
    }

}