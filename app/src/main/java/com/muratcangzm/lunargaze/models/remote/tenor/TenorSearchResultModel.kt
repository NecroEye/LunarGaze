package com.muratcangzm.lunargaze.models.remote.tenor

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TenorSearchResultModel(
    @Expose
    @SerializedName("id")
    val id:String,
    @Expose
    @SerializedName("title")
    val title:String,
    @Expose
    @SerializedName("media_formats")
    val mediaFormats:MediaFormats,
    @Expose
    @SerializedName("created")
    val created:Double,
    @Expose
    @SerializedName("content_description")
    val contentDescription:String,
    @Expose
    @SerializedName("itemurl")
    val itemurl:String,
    @Expose
    @SerializedName("url")
    val url:String,
    @Expose
    @SerializedName("tags")
    val tags:List<String>,
    @Expose
    @SerializedName("flags")
    val flags:List<String>,
    @Expose
    @SerializedName("hasaudio")
    val hasaudio:Boolean,
)
