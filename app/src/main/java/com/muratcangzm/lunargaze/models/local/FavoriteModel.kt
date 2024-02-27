package com.muratcangzm.lunargaze.models.local

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
    @ColumnInfo("table_imageName")
    var imageName:String?,

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.createStringArrayList(),
        parcel.readString()!!,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeStringList(folder)
        parcel.writeString(imageUrl)
        parcel.writeString(imageName)
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