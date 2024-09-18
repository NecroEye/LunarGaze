package com.muratcangzm.models.local

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favImages")
data class FavoriteModel(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id:Int?,
    @ColumnInfo("table_folder")
    var folder:List<String>?,
    @ColumnInfo("table_imageUrl")
    var imageUrl:String,
    @ColumnInfo("table_userName")
    var userName:String?,
    @ColumnInfo("table_rating")
    var rating:String?,
    @ColumnInfo("table_type")
    var type:String?,
    @ColumnInfo("table_updateTime")
    var updateTime:String?,
    @ColumnInfo("table_description")
    var description:String?,

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.createStringArrayList(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeStringList(folder)
        parcel.writeString(imageUrl)
        parcel.writeString(userName)
        parcel.writeString(rating)
        parcel.writeString(type)
        parcel.writeString(updateTime)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FavoriteModel> {
        override fun createFromParcel(parcel: Parcel): FavoriteModel {
            return FavoriteModel(parcel)
        }

        override fun newArray(size: Int): Array<FavoriteModel?> {
            return arrayOfNulls(size)
        }
    }
}


