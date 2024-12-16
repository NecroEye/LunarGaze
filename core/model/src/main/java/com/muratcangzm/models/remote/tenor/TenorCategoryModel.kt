package com.muratcangzm.models.remote.tenor

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.annotation.concurrent.Immutable

@Immutable
data class TenorCategoryModel(
    @Expose
    @SerializedName("locale")
    val locale: String,
    @Expose
    @SerializedName("tags")
    val tags:List<Tags>
){

    data class Tags(
        @Expose
        @SerializedName("searchterm")
        val searchterm:String,
        @Expose
        @SerializedName("path")
        val path:String,
        @Expose
        @SerializedName("image")
        val image:String,
        @Expose
        @SerializedName("name")
        val name:String,
        )
}
