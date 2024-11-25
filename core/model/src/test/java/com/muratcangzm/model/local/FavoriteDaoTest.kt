package com.muratcangzm.model.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.muratcangzm.models.local.FavoriteDao
import com.muratcangzm.models.local.FavoriteDatabase
import com.muratcangzm.models.local.FavoriteModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoriteDaoTest {

    private lateinit var database: FavoriteDatabase
    private lateinit var favoriteDao: FavoriteDao

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // In-memory database for testing
        database = Room.inMemoryDatabaseBuilder(
            context,
            FavoriteDatabase::class.java
        ).allowMainThreadQueries().build()

        favoriteDao = database.getDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `insert and retrieve favorite image`() {
        val favoriteModel = FavoriteModel(
            id = null,
            folder = listOf("Folder1,", "Folder2"),
            imageUrl = "http://example.com/image1.png",
            userName = "UserTest",
            rating = "5",
            type = "JPEG",
            updateTime = "2024-11-22",
            description = "Test Image"
        )

        favoriteDao.insertFavImage(favoriteModel = favoriteModel)
            .test()
            .assertComplete()

        favoriteDao.getAllFavImages()
            .test()
            .assertValue {
                it.isNotEmpty() && it[0].imageUrl == favoriteModel.imageUrl
            }
    }

    @Test
    fun `delete favorite image`() {
        val favoriteModel = FavoriteModel(
            id = null,
            folder = listOf("Folder1"),
            imageUrl = "http://example.com/image2.png",
            userName = "UserTest2",
            rating = "4",
            type = "PNG",
            updateTime = "2024-11-22",
            description = "Another test image"
        )

        favoriteDao.insertFavImage(favoriteModel = favoriteModel)
            .test()
            .assertComplete()

        favoriteDao.deleteFavImageOne(favoriteModel = favoriteModel)
            .test()
            .assertComplete()

        favoriteDao.getAllFavImages()
            .test()
            .assertValue { it.isEmpty() }

    }
}