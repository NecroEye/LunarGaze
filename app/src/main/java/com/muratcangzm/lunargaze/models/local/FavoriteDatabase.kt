package com.muratcangzm.lunargaze.models.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [FavoriteModel::class], version = 1, exportSchema = false)
@TypeConverters(FolderConverter::class)
abstract class FavoriteDatabase : RoomDatabase() {

    abstract fun getDao() : FavoriteDao

}