package com.muratcangzm.lunargaze.repository.local

import com.muratcangzm.models.local.FavoriteModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface FavoriteRepo {
    fun insertFavImage(favoriteModel: FavoriteModel): Completable
    fun deleteFavImage(favoriteModel: FavoriteModel): Completable
    fun getAllFavImages(): Flowable<List<FavoriteModel>>
}
