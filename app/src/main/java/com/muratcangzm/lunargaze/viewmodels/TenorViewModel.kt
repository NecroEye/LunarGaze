package com.muratcangzm.lunargaze.viewmodels

import androidx.lifecycle.viewModelScope
import com.muratcangzm.models.remote.tenor.TenorCategoryModel
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
class TenorViewModel
@Inject
constructor(private val tenorRepo: TenorRepo) : BaseViewModel() {


    private var _tenorCategory = MutableStateFlow<com.muratcangzm.models.remote.tenor.TenorCategoryModel?>(null)
    val tenorCategory: StateFlow<com.muratcangzm.models.remote.tenor.TenorCategoryModel?> get() = _tenorCategory.asStateFlow()


    init {

        viewModelName = "TenorViewModel"

        fetchTenorCategory()
    }


    private fun fetchTenorCategory() {

        viewModelScope.launch(exceptionHandler) {

            tenorRepo.fetchTenorCategory().collectLatest { result ->

                _tenorCategory.value = result.data

                Timber.tag("Tenor Status:").d("${result.status}")

            }
        }
    }


    override fun onCleared() {
        super.onCleared()
    }

}