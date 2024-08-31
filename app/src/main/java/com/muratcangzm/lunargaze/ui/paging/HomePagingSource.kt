package com.muratcangzm.lunargaze.ui.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.muratcangzm.lunargaze.models.remote.giphy.CategoryModel
import com.muratcangzm.lunargaze.repository.remote.GiphyRepo
import com.muratcangzm.lunargaze.common.DataResponse
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class HomePagingSource
@Inject
    constructor(private val giphyRepo: GiphyRepo) : PagingSource<Int, CategoryModel.CategoryData>() {


    override fun getRefreshKey(state: PagingState<Int, CategoryModel.CategoryData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
           state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
               ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CategoryModel.CategoryData> {

        val page = params.key ?: 1
        val pageSize = params.loadSize

        return try {
            val responseFlow = giphyRepo.fetchCategories()
            var categories: CategoryModel? = null

            responseFlow.collectLatest {response ->
                when(response.status){
                    DataResponse.Status.SUCCESS -> {
                        categories = response.data
                    }
                    DataResponse.Status.ERROR ->{
                        error(response.message.toString())
                    }
                    DataResponse.Status.LOADING ->{

                    }
                }
            }

            val fromIndex = (page - 1) * pageSize
            val toIndex = minOf(fromIndex + pageSize, categories?.categories!!.size)

            val pagedCategories = if (fromIndex <= toIndex && fromIndex < categories!!.categories!!.size)
                categories!!.categories!!.subList(fromIndex, toIndex) else emptyList()


            LoadResult.Page(
              data = pagedCategories,
                prevKey = if(page == 1) null else page - 1,
                nextKey = if(toIndex < categories!!.categories!!.size) page + 1 else null
            )
        }
        catch (e:Exception){
            LoadResult.Error(e)
        }
    }


}