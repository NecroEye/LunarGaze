package com.muratcangzm.lunargaze.repository

import android.content.Context
import androidx.datastore.core.DataStore
import com.muratcangzm.lunargaze.models.local.FavoriteDao
import com.muratcangzm.lunargaze.models.local.FavoriteModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import java.util.prefs.Preferences
import javax.inject.Inject

class FavoriteRepo
@Inject
constructor(private val favoriteDao: FavoriteDao) {



    fun insertFavImage(favoriteModel: FavoriteModel) : Completable{


        return favoriteDao.insertFavImage(favoriteModel)
    }

    fun deleteFavImage(favoriteModel: FavoriteModel) : Completable{
        return favoriteDao.deleteFavImageOne(favoriteModel)
    }

    fun getAllFavImages() : Flowable<List<FavoriteModel>>{
        return favoriteDao.getAllFavImages()
    }

}