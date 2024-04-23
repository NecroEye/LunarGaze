package com.muratcangzm.lunargaze.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muratcangzm.lunargaze.models.remote.CategoryModel
import com.muratcangzm.lunargaze.repository.GiphyRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(private val repo: GiphyRepo) : ViewModel() {


    private val _categoriesResult = MutableStateFlow<CategoryModel?>(null)
    val categoriesResult
        get() = _categoriesResult

    private val _mutableDataLoading = MutableStateFlow<Boolean>(false)
    val liveDataLoading =
        _mutableDataLoading

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->

        Timber.tag("Data Error").d("something isnt right: %s", throwable.message)

    }

    init {
        fetchCategories()
    }

    private fun fetchCategories() {

        _mutableDataLoading.value = true

        viewModelScope.launch(exceptionHandler) {

            supervisorScope {
                repo.fetchCategories().collect { result ->

                    _categoriesResult.value = result.data
                    _mutableDataLoading.value = false


                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}