package com.muratcangzm.lunargaze.models.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavImage(favoriteModel: FavoriteModel) : Completable

    @Delete
    fun deleteFavImageOne(favoriteModel: FavoriteModel) : Completable

    @Query("SELECT * FROM favImages")
    fun getAllFavImages() : Flowable<List<FavoriteModel>>

}