package com.muratcangzm.lunargaze.models.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChannelModel(
    @SerializedName("data")
    @Expose
    val channelData: List<ChannelData>?,
    @SerializedName("pagination")
    @Expose
    val pagination: Pagination?,
    @SerializedName("meta")
    @Expose
    val meta: Meta?,
) {

    data class ChannelData(
        @SerializedName("id")
        @Expose
        val id: Int?,
        @SerializedName("display_name")
        @Expose
        val displayName: String?,
        @SerializedName("slug")
        @Expose
        val slug: String?,
        @SerializedName("type")
        @Expose
        val type: String?,
        @SerializedName("content_type")
        @Expose
        val contentType: String?,
        @SerializedName("featured_gif")
        @Expose
        val featuredGif: FeaturedGif?,
        @SerializedName("user")
        @Expose
        val user: User?,
    ) {

        data class FeaturedGif(
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
            val bitlyGif: String?,
            @SerializedName("embed_url")
            @Expose
            val embedUrl: String?,
            @SerializedName("username")
            @Expose
            val username: String?,
            @SerializedName("title")
            @Expose
            val title: String?,
            @SerializedName("import_datetime")
            @Expose
            val sharedDateTime: String?,
            @SerializedName("images")
            @Expose
            val imageDimensions: ImageDimensions?,

            ) {

            data class ImageDimensions(

                @SerializedName("fixed_height_small")
                @Expose
                val fixedMp4: FixedMP4?

            ) {
                data class FixedMP4(
                    @SerializedName("mp4")
                    @Expose
                    val mp4: String?,
                    @SerializedName("url")
                    @Expose
                    val url: String?,
                    @SerializedName("webp")
                    @Expose
                    val webpUrl: String?,

                    )
            }


        }


        data class User(
            @SerializedName("avatar_url")
            @Expose
            val avatarUrl: String?,
            @SerializedName("banner_image")
            @Expose
            val bannerImage: String?,
            @SerializedName("banner_url")
            @Expose
            val bannerUrl: String?,
            @SerializedName("description")
            @Expose
            val description: String?,

            )

    }


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


}



