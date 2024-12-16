package com.muratcangzm.lunargaze.di


import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.muratcangzm.lunargaze.common.utils.Constants
import com.muratcangzm.lunargaze.service.GiphyAPI
import com.muratcangzm.lunargaze.service.TenorAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    @Named("giphyBaseUrl")
    fun provideGiphyBaseUrl() = Constants.GIPHY_BASE

    @Singleton
    @Provides
    @Named("tenorBaseUrl")
    fun provideTenorBaseUrl() = Constants.TENOR_BASE


    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().setLenient().create()


    @Singleton
    @Provides
    @Named("standardOkHttpClient")
    fun provideOkhttpClient(@ApplicationContext context: Context): OkHttpClient {

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val cacheSize = (10 * 1024 * 1024).toLong() // 10 Mb probably...
        val cache = Cache(context.cacheDir, cacheSize)

        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)
            .addNetworkInterceptor(loggingInterceptor)
            .cache(cache)
            .build()
    }

    @Singleton
    @Provides
    @Named("proxyOkHttpClient")
    fun createOkHttpClientWithProxy(): OkHttpClient{
        val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("80.14.162.49", 80)) // if it doesnt work then check spys.one for new port
        return OkHttpClient.Builder()
            .proxy(proxy)
            .build()
    }

    @Singleton
    @Provides
    fun provideGiphyApi(
        @Named("giphyBaseUrl") baseUrl: String,
        gson: Gson,
        @Named("standardOkHttpClient") client: OkHttpClient
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
        @Named("standardOkHttpClient") client: OkHttpClient
    ): TenorAPI {

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(TenorAPI::class.java)
    }
}