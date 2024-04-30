package com.muratcangzm.lunargaze.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muratcangzm.lunargaze.models.remote.ChannelModel
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
class DisplayViewModel
@Inject
constructor(
    private val giphyRepo: GiphyRepo,
) : ViewModel() {

    private val _mutableChannelResult = MutableStateFlow<ChannelModel?>(null)
    val channelResult: StateFlow<ChannelModel?>
        get() = _mutableChannelResult

    private val _mutableDataLoading = MutableStateFlow<Boolean>(false)
    val dataLoading: StateFlow<Boolean>
        get() = _mutableDataLoading


    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.tag("Data Error").d("something isnt right: %s", throwable.message)
    }

    fun getChannels(search: String, offset: Int?) {

        _mutableDataLoading.value = true

        viewModelScope.launch(exceptionHandler) {
            supervisorScope {

                giphyRepo.fetchChannels(search, offset).collect { result ->

                    Timber.tag("DisplayFragment Data0: ").d("$result")


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