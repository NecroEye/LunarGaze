package com.muratcangzm.lunargaze.repository.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import com.muratcangzm.models.local.FavoriteDao
import com.muratcangzm.models.local.FavoriteModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import timber.log.Timber
import java.util.prefs.Preferences
import javax.inject.Inject

class FavoriteRepo
@Inject
constructor(private val favoriteDao: com.muratcangzm.models.local.FavoriteDao) {

    //RxJava3 used for RoomDB
    fun insertFavImage(favoriteModel: com.muratcangzm.models.local.FavoriteModel) : Completable{
        return favoriteDao.insertFavImage(favoriteModel)
    }

    fun deleteFavImage(favoriteModel: com.muratcangzm.models.local.FavoriteModel) : Completable{
        return favoriteDao.deleteFavImageOne(favoriteModel)
            .doOnComplete {
                Timber.tag("FavoriteRepo").d("Image deleted from database: %s", favoriteModel.id)
        }
    }

    fun getAllFavImages() : Flowable<List<com.muratcangzm.models.local.FavoriteModel>>{
        return favoriteDao.getAllFavImages()
    }


}