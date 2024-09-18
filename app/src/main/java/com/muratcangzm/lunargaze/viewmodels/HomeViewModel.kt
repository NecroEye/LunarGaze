package com.muratcangzm.lunargaze.viewmodels

import androidx.lifecycle.viewModelScope
import com.muratcangzm.models.remote.giphy.CategoryModel
import com.muratcangzm.models.remote.tenor.TenorCategoryModel
import com.muratcangzm.lunargaze.repository.remote.GiphyRepo
import com.muratcangzm.lunargaze.repository.remote.TenorRepo
import com.muratcangzm.lunargaze.viewmodels.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val repo: GiphyRepo,
    private val tenorRepo: TenorRepo
) : BaseViewModel() {

    private var _tenorCategoryResult = MutableStateFlow<com.muratcangzm.models.remote.tenor.TenorCategoryModel?>(null)
    val tenorCategoryResult: StateFlow<com.muratcangzm.models.remote.tenor.TenorCategoryModel?>
        get() = _tenorCategoryResult.asStateFlow()

    private val _categoriesResult = MutableStateFlow<com.muratcangzm.models.remote.giphy.CategoryModel?>(null)
    val categoriesResult: StateFlow<com.muratcangzm.models.remote.giphy.CategoryModel?>
        get() = _categoriesResult.asStateFlow()

    private val _mutableDataLoading = MutableStateFlow(false)
    val mutableDataLoading: StateFlow<Boolean>
        get() = _mutableDataLoading.asStateFlow()

    init {
        viewModelName = "HomeViewModel"
        fetchCategories()
        fetchTenorCategory()
    }

    private fun fetchCategories() {

        _mutableDataLoading.value = true

        viewModelScope.launch(exceptionHandler) {
            repo.fetchCategories().collectLatest { result ->
                Timber.tag("Giphy Data2:").d("${result.message}")
                _categoriesResult.value = result.data
                _mutableDataLoading.value = false
            }
        }
    }

    private fun fetchTenorCategory() {
        viewModelScope.launch(exceptionHandler) {
            tenorRepo.fetchTenorCategory().collectLatest { result ->
                _tenorCategoryResult.value = result.data
            }
        }

    }


    override fun onCleared() {
        super.onCleared()
    }
}