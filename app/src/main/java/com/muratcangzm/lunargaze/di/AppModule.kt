package com.muratcangzm.lunargaze.di

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.room.Room
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.muratcangzm.lunargaze.R
import com.muratcangzm.models.local.FavoriteDatabase
import com.muratcangzm.lunargaze.repository.local.FavoriteRepo
import com.muratcangzm.lunargaze.repository.remote.GiphyRepo
import com.muratcangzm.lunargaze.repository.remote.TenorRepo
import com.muratcangzm.lunargaze.service.GiphyAPI
import com.muratcangzm.lunargaze.service.TenorAPI
import com.muratcangzm.lunargaze.common.utils.Constants
import com.muratcangzm.lunargaze.common.utils.DefaultDispatcher
import com.muratcangzm.lunargaze.common.utils.IoDispatcher
import com.muratcangzm.lunargaze.common.utils.MainDispatcher
import com.muratcangzm.lunargaze.helper.EncryptionHelper
import com.muratcangzm.lunargaze.repository.local.DataStoreRepo
import com.muratcangzm.lunargaze.repository.local.DataStoreRepoImpl
import com.muratcangzm.lunargaze.repository.local.FavoriteRepoImpl
import com.muratcangzm.lunargaze.viewmodels.HomeViewModel
import com.muratcangzm.lunargaze.viewmodels.ViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @MainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideGiphyRepository(
        api: GiphyAPI,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): GiphyRepo {
        return GiphyRepo(api, ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideTenorRepository(
        api: TenorAPI,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): TenorRepo {
        return TenorRepo(api, ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideFavoriteRepo(dao: com.muratcangzm.models.local.FavoriteDao): FavoriteRepoImpl {
        return FavoriteRepoImpl(dao)
    }

    @Provides
    @Singleton
    fun provideHomeViewModel(repo: GiphyRepo, tenorRepo: TenorRepo): HomeViewModel {
        return HomeViewModel(repo, tenorRepo)
    }

    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context: Context): FavoriteDatabase = Room.databaseBuilder(
        context,
        FavoriteDatabase::class.java,
        "lunar_db"
    )
        .fallbackToDestructiveMigration()
        .build()


    @Provides
    @Singleton
    fun provideDao(favoriteDatabase: FavoriteDatabase): com.muratcangzm.models.local.FavoriteDao {
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
                    .priority(Priority.HIGH)
            )
    }

    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.SHARED_DB_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(context, Constants.SHARED_DB_NAME)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(Constants.SHARED_DB_NAME) }
        )
    }

    @Provides
    @Singleton
    fun provideDataStoreRepo(
        dataStore: DataStore<Preferences>,
        encryptionHelper: EncryptionHelper
    ): DataStoreRepo {
        return DataStoreRepoImpl(dataStore, encryptionHelper)
    }

    @Provides
    @Singleton
    fun provideNavController(@ActivityContext activity: Activity): NavController {
        return Navigation.findNavController(activity, R.id.fragmentContainerView)
    }

    @Provides
    @Singleton
    fun provideEncryptionHelper(@ApplicationContext context: Context) = EncryptionHelper(context)

    @Provides
    @Singleton
    fun provideViewModelFactory(
        giphyRepo: GiphyRepo,
        tenorRepo: TenorRepo
    ): ViewModelProvider.Factory {
        return ViewModelFactory.provideFactory(giphyRepo, tenorRepo)
    }
}


