package com.muratcangzm.lunargaze.viewmodels

import androidx.lifecycle.viewModelScope
import com.muratcangzm.models.remote.giphy.SearchModel
import com.muratcangzm.lunargaze.repository.remote.GiphyRepo
import com.muratcangzm.lunargaze.viewmodels.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
@Inject
constructor(private val repo: GiphyRepo) : BaseViewModel() {


    private val mutableSearchResult = MutableStateFlow<com.muratcangzm.models.remote.giphy.SearchModel?>(null)
    val searchResult: StateFlow<com.muratcangzm.models.remote.giphy.SearchModel?>
        get() = mutableSearchResult.asStateFlow()

    init {
        viewModelName = "SearchViewModel"
    }

    fun fetchSearchData(search: String) {

        viewModelScope.launch(exceptionHandler) {
            supervisorScope {

                repo.fetchSearch(search).collectLatest {

                    mutableSearchResult.value = it.data

                }
            }
        }

    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}