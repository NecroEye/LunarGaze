package com.muratcangzm.models.remote.giphy

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CategoryModel(
    @SerializedName("data")
    @Expose
    val categories: List<CategoryData>?,
    @SerializedName("pagination")
    @Expose
    val pagination: Pagination?,
    @SerializedName("meta")
    @Expose
    val meta: Meta?,
    ) {

    data class CategoryData(
        @SerializedName("name")
        @Expose
        val name:String?,
        @SerializedName("name_encoded")
        @Expose
        val encodedName: String?,
        @SerializedName("gif")
        @Expose
        val gifObject: GifObject?,
        ){

        data class GifObject(
            @SerializedName("type")
            @Expose
            val type: String?,
            @SerializedName("id")
            @Expose
            val id: String?,
            @SerializedName("url")
            @Expose
            val url: String?,
            @SerializedName("bitly_gif_url")
            @Expose
            val bitlyUrl: String?,
            @SerializedName("title")
            @Expose
            val title: String?,
            @SerializedName("rating")
            @Expose
            val rating: String?,
            @SerializedName("create_datetime")
            @Expose
            val createdTime: String?,
            @SerializedName("Trending_datetime")
            @Expose
            val TrendedTime: String?,
            @SerializedName("images")
            @Expose
            val imageDimensions: ImageDimensions?
        ){

            data class ImageDimensions(
                @SerializedName("480w_still")
                @Expose
                val fixed480Still: Fixed480wStill?,
                @SerializedName("fixed_width_still")
                @Expose
                val fixedWidthStill: FixedWidthStill?,
            ){

                data class Fixed480wStill(
                    @SerializedName("height")
                    @Expose
                    val height: String?,
                    @SerializedName("size")
                    @Expose
                    val size: String?,
                    @SerializedName("url")
                    @Expose
                    val fixed480Url: String?,
                    @SerializedName("width")
                    @Expose
                    val width: String?,
                )

                data class FixedWidthStill(
                    @SerializedName("height")
                    @Expose
                    val height: String?,
                    @SerializedName("size")
                    @Expose
                    val size: String?,
                    @SerializedName("url")
                    @Expose
                    val fixed480Url: String?,
                    @SerializedName("width")
                    @Expose
                    val width: String?,
                )

            }
        }
    }

    data class Pagination(
        @SerializedName("total_count")
        @Expose
        val totalCount: Int?,
        @SerializedName("count")
        @Expose
        val count: Int?
    )

    data class Meta(
        @SerializedName("msg")
        @Expose
        val msg: String?,
        @SerializedName("status")
        @Expose
        val status:Int?,
        @SerializedName("response_id")
        @Expose
        val responseId: String?,
    )
}