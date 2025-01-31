package com.muratcangzm.models.local

import androidx.room.TypeConverter
import com.google.gson.Gson

class FolderConverter {

    @TypeConverter
    fun fromStringList(value: String?): List<String>?{
        return Gson().fromJson(value, Array<String>::class.java)?.toList()
    }

    @TypeConverter
    fun toStringList(list: List<String>?) : String?{
        return Gson().toJson(list)
    }
}