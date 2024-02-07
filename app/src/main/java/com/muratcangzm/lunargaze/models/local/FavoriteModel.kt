package com.muratcangzm.lunargaze.models.local


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favImages")
data class FavoriteModel(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id:Int?,
)