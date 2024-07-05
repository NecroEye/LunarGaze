package com.muratcangzm.lunargaze.di

import com.google.gson.Gson
import com.muratcangzm.lunargaze.service.GiphyAPI
import com.muratcangzm.lunargaze.utils.Constants
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit

class NetworkModuleTest {

    @Mock
    lateinit var mockGson: Gson

    @Mock
    lateinit var mockOkkHttpClient: OkHttpClient

    @Mock
    lateinit var mockRetrofit: Retrofit

    @Mock
    lateinit var networkModuleTest: NetworkModule


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(mockGson, mockOkkHttpClient, mockRetrofit)
    }

    @Test
    fun testProvideBaseUrl() {
        val baseUrl = networkModuleTest.provideGiphyBaseUrl()
        assertEquals(Constants.BASE_URL, baseUrl)
    }

    @Test
    fun testGsonProvider() {
        val gson = networkModuleTest.provideGson()
        assertEquals(gson, mockGson)
    }

    @Test
    fun testOkkHttpClientProvider() {

        val okHttpClient = networkModuleTest.provideOkhttpClient()
        assertEquals(okHttpClient, mockOkkHttpClient)

    }

    @Test
    fun `test provideApi`() {

        val mockGiphyApi = mock(GiphyAPI::class.java)
        `when`(mockRetrofit.create(GiphyAPI::class.java)).thenReturn(mockGiphyApi)

        val api = NetworkModule.provideGiphyApi(Constants.BASE_URL, mockGson, mockOkkHttpClient)
        assertEquals(api, mockGiphyApi)

    }

}