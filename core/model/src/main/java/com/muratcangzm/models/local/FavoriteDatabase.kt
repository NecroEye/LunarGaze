package com.muratcangzm.models.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [FavoriteModel::class], version = 2, exportSchema = false)
@TypeConverters(FolderConverter::class)
abstract class FavoriteDatabase : RoomDatabase() {

    abstract fun getDao() : FavoriteDao

    companion object{

         // if you dont give a fuck about old db just dont use below

        /*
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // if you added a new column, you'd run an ALTER TABLE query without losing old db
                db.execSQL("ALTER TABLE favImages ADD COLUMN table_userName TEXT DEFAULT ''")
            }
        }
         */

    }
}