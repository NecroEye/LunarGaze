package com.muratcangzm.lunargaze.models.remote.giphy

import android.os.Parcel
import android.os.Parcelable
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
    ) : Parcelable {

        constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            TODO("featuredGif"),
            TODO("user")
        )

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
            @SerializedName("rating")
            @Expose
            val rating: String?,
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

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(id)
            parcel.writeString(displayName)
            parcel.writeString(slug)
            parcel.writeString(type)
            parcel.writeString(contentType)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<ChannelData> {
            override fun createFromParcel(parcel: Parcel): ChannelData {
                return ChannelData(parcel)
            }

            override fun newArray(size: Int): Array<ChannelData?> {
                return arrayOfNulls(size)
            }
        }

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



