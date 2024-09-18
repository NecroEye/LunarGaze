package com.muratcangzm.lunargaze.models.remote

import android.os.Parcel
import com.muratcangzm.models.remote.giphy.ChannelModel
import org.junit.Assert.assertEquals
import org.junit.Test

class ChannelModelTest {

    @Test
    fun testDataClassCreation() {
        val channelData = listOf(
            com.muratcangzm.models.remote.giphy.ChannelModel.ChannelData(
                id = 1,
                displayName = "Test Channel",
                slug = "test-channel",
                type = "public",
                contentType = "video",
                featuredGif = null,
                user = null
            )
        )

        val pagination = com.muratcangzm.models.remote.giphy.ChannelModel.Pagination(totalCount = 1, count = 1, offset = 0)
        val meta = com.muratcangzm.models.remote.giphy.ChannelModel.Meta(msg = "Success", status = 200, responseId = "123")

        val channelModel =
            com.muratcangzm.models.remote.giphy.ChannelModel(channelData, pagination, meta)

        assertEquals(channelData, channelModel.channelData)
        assertEquals(pagination, channelModel.pagination)
        assertEquals(meta, channelModel.meta)
    }

    @Test
    fun testParcelable() {
        val channelData = com.muratcangzm.models.remote.giphy.ChannelModel.ChannelData(
            id = 1,
            displayName = "Test Channel",
            slug = "test-channel",
            type = "public",
            contentType = "video",
            featuredGif = null,
            user = null
        )

        val parcel = Parcel.obtain()
        channelData.writeToParcel(parcel, channelData.describeContents())

        // Reset the parcel for reading
        parcel.setDataPosition(0)

        // Create a new object from the parcel
        val createdChannelData = com.muratcangzm.models.remote.giphy.ChannelModel.ChannelData.CREATOR.createFromParcel(parcel)

        // Verify that the created object is equal to the original object
        assertEquals(channelData, createdChannelData)
    }

}