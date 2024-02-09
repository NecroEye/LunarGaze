package com.muratcangzm.lunargaze.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muratcangzm.lunargaze.models.remote.ChannelModel
import com.muratcangzm.lunargaze.repository.GiphyRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

@HiltViewModel
class DisplayViewModel
@Inject
constructor(private val giphyRepo: GiphyRepo) : ViewModel() {

    private val _mutableChannelResult = MutableStateFlow<ChannelModel?>(null)
    val channelResult
        get() = _mutableChannelResult

    private val _mutableDataLoading = MutableStateFlow<Boolean>(false)
    val dataLoading
        get() = _mutableDataLoading

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->

        Log.d("Data Error", "something isnt right: ${throwable.message}")

    }


    fun getChannels(search: String) {

        _mutableDataLoading.value = true

        viewModelScope.launch(exceptionHandler) {
            supervisorScope {

                giphyRepo.fetchChannels(search).collect { result ->

                    Log.d("DisplayFragment Data0: ", "$result")


                    _mutableChannelResult.value = result.data
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