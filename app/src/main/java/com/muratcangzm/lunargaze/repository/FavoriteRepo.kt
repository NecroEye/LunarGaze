package com.muratcangzm.lunargaze.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import com.muratcangzm.lunargaze.models.local.FavoriteDao
import com.muratcangzm.lunargaze.models.local.FavoriteModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import timber.log.Timber
import java.util.prefs.Preferences
import javax.inject.Inject

class FavoriteRepo
@Inject
constructor(private val favoriteDao: FavoriteDao) {

    //RxJava3 used for roomdb
    fun insertFavImage(favoriteModel: FavoriteModel) : Completable{
        return favoriteDao.insertFavImage(favoriteModel)
    }

    fun deleteFavImage(favoriteModel: FavoriteModel) : Completable{
        return favoriteDao.deleteFavImageOne(favoriteModel)
            .doOnComplete {
                Timber.tag("FavoriteRepo").d("Image deleted from database: " + favoriteModel.id)
        }
    }

    fun getAllFavImages() : Flowable<List<FavoriteModel>>{
        return favoriteDao.getAllFavImages()
    }


}