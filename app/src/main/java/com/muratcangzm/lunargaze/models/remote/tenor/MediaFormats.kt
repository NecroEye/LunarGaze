package com.muratcangzm.lunargaze.models.remote.tenor

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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
