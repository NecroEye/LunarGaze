package com.muratcangzm.lunargaze.ui.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.muratcangzm.models.remote.tenor.TenorCategoryModel
import com.muratcangzm.lunargaze.repository.remote.TenorRepo
import javax.inject.Inject

class TenorPagingSource
@Inject constructor(private val tenorRepo: TenorRepo) : PagingSource<Int, com.muratcangzm.models.remote.tenor.TenorCategoryModel>(){


    override fun getRefreshKey(state: PagingState<Int, com.muratcangzm.models.remote.tenor.TenorCategoryModel>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, com.muratcangzm.models.remote.tenor.TenorCategoryModel> {
        TODO("Not yet implemented")
    }


}