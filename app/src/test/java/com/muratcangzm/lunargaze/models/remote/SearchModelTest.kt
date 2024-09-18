package com.muratcangzm.lunargaze.models.remote

import com.muratcangzm.models.remote.giphy.SearchModel
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchModelTest {

    @Test
    fun testDataClassCreation() {
        val searchData = listOf(
            com.muratcangzm.models.remote.giphy.SearchModel.SearchData(
                type = "gif",
                id = 1,
                url = "https://example.com/gif",
                embedUrl = "https://example.com/embed",
                source = "Example Source",
                title = "Example GIF",
                rating = "PG",
                uploadTime = "2022-04-22 10:30:00"
            )
        )

        val pagination = com.muratcangzm.models.remote.giphy.SearchModel.Pagination(totalCount = 1, count = 1, offset = 0)
        val meta = com.muratcangzm.models.remote.giphy.SearchModel.Meta(msg = "Success", status = 200, responseId = "123")

        val searchModel =
            com.muratcangzm.models.remote.giphy.SearchModel(searchData, meta, pagination)

        assertEquals(searchData, searchModel.searchData)
        assertEquals(meta, searchModel.meta)
        assertEquals(pagination, searchModel.pagination)
    }

}