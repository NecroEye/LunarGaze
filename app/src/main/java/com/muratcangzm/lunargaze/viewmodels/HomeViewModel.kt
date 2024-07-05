package com.muratcangzm.lunargaze.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muratcangzm.lunargaze.models.remote.giphy.CategoryModel
import com.muratcangzm.lunargaze.models.remote.tenor.TenorCategoryModel
import com.muratcangzm.lunargaze.repository.remote.GiphyRepo
import com.muratcangzm.lunargaze.repository.remote.TenorRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(private val repo: GiphyRepo,
            private val tenorRepo: TenorRepo
) : ViewModel() {


    private var _tenorCategoryResult = MutableStateFlow<TenorCategoryModel?>(null)
    val tenorCategoryResult: StateFlow<TenorCategoryModel?> get() = _tenorCategoryResult.asStateFlow()


    private val _categoriesResult = MutableStateFlow<CategoryModel?>(null)
    val categoriesResult: StateFlow<CategoryModel?>
        get() = _categoriesResult.asStateFlow()

    private val _mutableDataLoading = MutableStateFlow(false)
    val mutableDataLoading: StateFlow<Boolean>
        get() = _mutableDataLoading.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->

        Timber.tag("Data Error").d("something isnt right: %s", throwable.message)

    }

    init {
        fetchCategories()
        fetchTenorCategory()
    }

    private fun fetchCategories() {

        _mutableDataLoading.value = true

        viewModelScope.launch(exceptionHandler) {

            supervisorScope {
                repo.fetchCategories().collectLatest { result ->

                    _categoriesResult.value = result.data
                    _mutableDataLoading.value = false


                }
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
        viewModelScope.cancel()
    }

}