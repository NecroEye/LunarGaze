package com.muratcangzm.lunargaze.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muratcangzm.lunargaze.models.remote.SearchModel
import com.muratcangzm.lunargaze.repository.GiphyRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
@Inject constructor(private val repo: GiphyRepo) : ViewModel() {


    private val mutableSearchResult = MutableStateFlow<SearchModel?>(null)
    val searchResult: StateFlow<SearchModel?>
        get() = mutableSearchResult

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.tag("something occurred bad").d(throwable)
    }

    fun fetchSearchData(search: String) {

        viewModelScope.launch(exceptionHandler) {
            supervisorScope {

                repo.fetchSearch(search).collect {

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