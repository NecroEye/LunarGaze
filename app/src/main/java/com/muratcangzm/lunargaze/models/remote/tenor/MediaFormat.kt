package com.muratcangzm.lunargaze.models.remote.tenor

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MediaFormat(
    @Expose
    @SerializedName("url")
    val url:String,
    @Expose
    @SerializedName("duration")
    val duration:Double,
    @Expose
    @SerializedName("preview")
    val preview:String,
    @Expose
    @SerializedName("dims")
    val dims:List<Int>,
    @Expose
    @SerializedName("size")
    val size:Int
    )

