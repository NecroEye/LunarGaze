package com.muratcangzm.lunargaze.di

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.room.Room
import androidx.datastore.preferences.preferencesDataStoreFile
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.muratcangzm.lunargaze.R
import com.muratcangzm.lunargaze.models.local.FavoriteDao
import com.muratcangzm.lunargaze.models.local.FavoriteDatabase
import com.muratcangzm.lunargaze.repository.FavoriteRepo
import com.muratcangzm.lunargaze.repository.GiphyRepo
import com.muratcangzm.lunargaze.service.GiphyAPI
import com.muratcangzm.lunargaze.utils.Constants
import com.muratcangzm.lunargaze.viewmodels.HomeViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext application: Application): Context {
        return application.applicationContext
    }


    @Provides
    @Singleton
    fun provideGiphyRepository(api: GiphyAPI): GiphyRepo {
        return GiphyRepo(api)
    }

    @Provides
    @Singleton
    fun provideFavoriteRepo(dao: FavoriteDao): FavoriteRepo {
        return FavoriteRepo(dao)
    }


    @Provides
    @Singleton
    fun provideHomeViewModel(repo: GiphyRepo): HomeViewModel {

        return HomeViewModel(repo)
    }

    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context: Context): FavoriteDatabase = Room.databaseBuilder(
        context,
        FavoriteDatabase::class.java,
        "db"
    )
        .fallbackToDestructiveMigration()
        .build()


    @Provides
    @Singleton
    fun provideDao(favoriteDatabase: FavoriteDatabase): FavoriteDao {
        return favoriteDatabase.getDao()
    }

    @Provides
    @Singleton
    fun injectGlide(@ApplicationContext context: Context): RequestManager {
        return Glide.with(context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .encodeQuality(Constants.ENCODE_QUALITY)
                    .placeholder(R.drawable.not_found)
                    .error(R.drawable.not_found)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)

            )
    }

    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.SHARED_DB_NAME, Context.MODE_PRIVATE)
    }


    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<androidx.datastore.preferences.core.Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(context,Constants.SHARED_DB_NAME)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(Constants.SHARED_DB_NAME) }
        )
    }

    @Provides
    fun provideNavController(@ActivityContext activity: Activity): NavController {
        return Navigation.findNavController(activity, R.id.fragmentContainerView)
    }

}


