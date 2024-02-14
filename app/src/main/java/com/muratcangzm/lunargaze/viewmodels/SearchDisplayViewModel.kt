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
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

@HiltViewModel
class SearchDisplayViewModel
@Inject
constructor(private val giphyRepo: GiphyRepo) : ViewModel() {


    private val _searchResult = MutableStateFlow<SearchModel?>(null)
    val searchResult
        get() = _searchResult

    private val _mutableDataLoading = MutableStateFlow<Boolean>(false)
    val mutableDataLoading
        get() = _mutableDataLoading

private val exceptionHandler = CoroutineExceptionHandler{ _, throwable ->

    Log.d("Data Error", "something isnt right: ${throwable.message}")

}

    fun fetchSearchData(search:String){

        viewModelScope.launch(exceptionHandler) {
              _mutableDataLoading.value = true

            supervisorScope {

                 giphyRepo.fetchSearch(search).collect{


                     _searchResult.value = it.data
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