package com.muratcangzm.lunargaze.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.muratcangzm.lunargaze.models.local.FavoriteDatabase
import com.muratcangzm.lunargaze.repository.GiphyRepo
import com.muratcangzm.lunargaze.service.GiphyAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.mockito.Mockito.`when`
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations


class AppModuleTest {


    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockApplication: Application

    @Mock
    private lateinit var mockGiphyApi: GiphyAPI

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences


    @Before
    fun setup() {

        MockitoAnnotations.initMocks(this)

        `when`(mockApplication.applicationContext).thenReturn(mockContext)
        `when`(mockContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mockSharedPreferences)
    }


    @Test
    @ExperimentalCoroutinesApi
    fun testDefaultCoroutineDispatcher(){
        val dispatcher = AppModule.provideDefaultDispatcher()
        assertEquals(Dispatchers.Default, dispatcher)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun testIODispatcher(){
        val dispatcher = AppModule.provideIoDispatcher()
        assertEquals(Dispatchers.IO, dispatcher)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun testMainDispatcher(){
        val dispatcher = AppModule.provideMainDispatcher()
        assertEquals(Dispatchers.Main, dispatcher)
    }

    @Test
    fun testApplicationContext() {

        val context = AppModule.provideApplicationContext(mockApplication)
        assertNotNull(context)
    }

    @Test
    fun testGiphyRepository() {

        val dispatcher = AppModule.provideMainDispatcher()
        val repository = AppModule.provideGiphyRepository(mockGiphyApi, dispatcher)
        assertNotNull(repository)

    }


    @Test
    fun testHomeViewModel(){

        val mockRepo = mock(GiphyRepo::class.java)
        val viewModel = AppModule.provideHomeViewModel(mockRepo)

        assertNotNull(viewModel)

    }


    @Test
    fun testFavoriteDao(){

        val mockDatabase = mock(FavoriteDatabase::class.java)
        val dao  = AppModule.provideDao(mockDatabase)

        assertNotNull(dao)

    }


    @Test
    fun testRoomDatabase(){

        val db = AppModule.provideRoom(mockApplication)
        assertNotNull(db)
    }


    @Test
    fun testGlide(){

        val glide = AppModule.injectGlide(mockContext)
        assertNotNull(glide)

    }

    @Test
    fun testSharedPreference(){

        val shared = AppModule.provideSharedPreference(mockContext)
        assertNotNull(shared)

    }

}
