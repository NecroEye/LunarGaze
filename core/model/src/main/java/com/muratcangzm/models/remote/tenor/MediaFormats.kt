package com.muratcangzm.models.remote.tenor

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.annotation.concurrent.Immutable

@Immutable
data class MediaFormats(
    @Expose
    @SerializedName("tinygif")
    val tinygif: MediaFormat?,
    @Expose
    @SerializedName("mediumgif")
    val mediumgif: MediaFormat?,
    @Expose
    @SerializedName("tinymp4")
    val tinymp4: MediaFormat?,
    @Expose
    @SerializedName("nanogif")
    val nanogif: MediaFormat?,
    @Expose
    @SerializedName("gif")
    val gif: MediaFormat?,
)
