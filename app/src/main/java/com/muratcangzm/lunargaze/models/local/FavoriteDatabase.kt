package com.muratcangzm.lunargaze.models.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteModel::class], version = 1, exportSchema = false)
abstract class FavoriteDatabase : RoomDatabase() {

    abstract fun getDao() : FavoriteDao

}