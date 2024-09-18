package com.muratcangzm.models.remote.giphy

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SearchModel(
    @SerializedName("data")
    @Expose
    val searchData: List<SearchData>?,
    @SerializedName("meta")
    @Expose
    val meta: Meta?,
    @SerializedName("pagination")
    @Expose
    val pagination: Pagination?
){


    data class SearchData(
        @SerializedName("type")
        @Expose
        val type: String?,
        @SerializedName("id")
        @Expose
        val id: Int?,
        @SerializedName("url")
        @Expose
        val url:String?,
        @SerializedName("embed_url")
        @Expose
        val embedUrl:String?,
        @SerializedName("source")
        @Expose
        val source:String?,
        @SerializedName("title")
        @Expose
        val title:String?,
        @SerializedName("rating")
        @Expose
        val rating:String?,
        @SerializedName("import_datetime")
        @Expose
        val uploadTime:String?
    )

    data class Meta(
        @SerializedName("msg")
        @Expose
        val msg: String?,
        @SerializedName("status")
        @Expose
        val status: Int?,
        @SerializedName("response_id")
        @Expose
        val responseId: String?,
    )

    data class Pagination(
        @SerializedName("total_count")
        @Expose
        val totalCount: Int?,
        @SerializedName("count")
        @Expose
        val count: Int?,
        @SerializedName("offset")
        @Expose
        val offset: Int?
    )

}
