package com.muratcangzm.lunargaze.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.muratcangzm.lunargaze.R
import com.muratcangzm.lunargaze.models.local.FavoriteDao
import com.muratcangzm.lunargaze.models.local.FavoriteDatabase
import com.muratcangzm.lunargaze.repository.GiphyRepo
import com.muratcangzm.lunargaze.service.GiphyAPI
import com.muratcangzm.lunargaze.utils.Constants
import com.muratcangzm.lunargaze.viewmodels.HomeViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
    fun provideHomeViewModel(repo: GiphyRepo): HomeViewModel {

        return HomeViewModel(repo)

    }


    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext application: Application) = Room.databaseBuilder(
        application,
        FavoriteDatabase::class.java,
        "db"
    )
        .fallbackToDestructiveMigration()
        .build()


    @Provides
    @Singleton
    fun provideDao(favoriteDatabase: FavoriteDatabase) : FavoriteDao {
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
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            )
    }

    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context) : SharedPreferences{
        return context.getSharedPreferences("shared_key", Context.MODE_PRIVATE)
    }

}