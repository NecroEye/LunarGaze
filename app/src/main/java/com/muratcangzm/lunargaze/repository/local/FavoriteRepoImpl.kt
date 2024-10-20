package com.muratcangzm.lunargaze.repository.local

import com.muratcangzm.models.local.FavoriteDao
import com.muratcangzm.models.local.FavoriteModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import timber.log.Timber
import javax.inject.Inject

class FavoriteRepoImpl
@Inject
constructor(private val favoriteDao: FavoriteDao) : FavoriteRepo {

    override fun insertFavImage(favoriteModel: FavoriteModel): Completable {
        return favoriteDao.insertFavImage(favoriteModel)
    }

    override fun deleteFavImage(favoriteModel: FavoriteModel): Completable {
        return favoriteDao.deleteFavImageOne(favoriteModel)
            .doOnComplete {
                Timber.tag("FavoriteRepo").d("Image deleted from database: %s", favoriteModel.id)
            }
    }

    override fun getAllFavImages(): Flowable<List<FavoriteModel>> {
        return favoriteDao.getAllFavImages()
    }
}
