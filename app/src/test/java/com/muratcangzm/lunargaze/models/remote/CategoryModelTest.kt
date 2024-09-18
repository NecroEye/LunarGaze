package com.muratcangzm.lunargaze.models.remote

import com.muratcangzm.models.remote.giphy.CategoryModel
import org.junit.Assert.assertEquals
import org.junit.Test

class CategoryModelTest {

    @Test
    fun testDataClassCreation() {
        val categories = listOf(
            com.muratcangzm.models.remote.giphy.CategoryModel.CategoryData(
                name = "Funny",
                encodedName = "funny",
                gifObject = com.muratcangzm.models.remote.giphy.CategoryModel.CategoryData.GifObject(
                    type = "gif",
                    id = "abc123",
                    url = "https://example.com/gif",
                    bitlyUrl = "https://bit.ly/abc123",
                    title = "Funny GIF",
                    rating = "PG",
                    createdTime = "2022-04-22 10:30:00",
                    TrendedTime = "2022-04-22 11:00:00",
                    imageDimensions = com.muratcangzm.models.remote.giphy.CategoryModel.CategoryData.GifObject.ImageDimensions(
                        fixed480Still = com.muratcangzm.models.remote.giphy.CategoryModel.CategoryData.GifObject.ImageDimensions.Fixed480wStill(
                            height = "200",
                            size = "500",
                            fixed480Url = "https://example.com/fixed480.gif",
                            width = "480"
                        ),
                        fixedWidthStill = com.muratcangzm.models.remote.giphy.CategoryModel.CategoryData.GifObject.ImageDimensions.FixedWidthStill(
                            height = "200",
                            size = "500",
                            fixed480Url = "https://example.com/fixedwidth.gif",
                            width = "480"
                        )
                    )
                )
            )
        )

        val pagination = com.muratcangzm.models.remote.giphy.CategoryModel.Pagination(totalCount = 1, count = 1)
        val meta = com.muratcangzm.models.remote.giphy.CategoryModel.Meta(msg = "Success", status = 200, responseId = "123")

        val categoryModel =
            com.muratcangzm.models.remote.giphy.CategoryModel(categories, pagination, meta)

        assertEquals(categories, categoryModel.categories)
        assertEquals(pagination, categoryModel.pagination)
        assertEquals(meta, categoryModel.meta)
    }

}