package com.muratcangzm.lunargaze.viewmodels

import androidx.lifecycle.viewModelScope
import com.muratcangzm.models.remote.giphy.ChannelModel
import com.muratcangzm.lunargaze.repository.remote.GiphyRepo
import com.muratcangzm.lunargaze.viewmodels.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DisplayViewModel
@Inject
constructor(
    private val giphyRepo: GiphyRepo,
) : BaseViewModel() {

    private val _mutableChannelResult = MutableStateFlow<com.muratcangzm.models.remote.giphy.ChannelModel?>(null)
    val channelResult: StateFlow<com.muratcangzm.models.remote.giphy.ChannelModel?>
        get() = _mutableChannelResult.asStateFlow()

    private val _mutableDataLoading = MutableStateFlow<Boolean>(false)
    val dataLoading: StateFlow<Boolean>
        get() = _mutableDataLoading.asStateFlow()

    init {
        viewModelName = "DisplayViewModel"
    }

    fun getChannels(search: String, offset: Int?) {

        _mutableDataLoading.value = true

        viewModelScope.launch(exceptionHandler) {
            supervisorScope {

                giphyRepo.fetchChannels(search, offset).collectLatest { result ->

                    Timber.tag("DisplayFragment Data0: ").d("$result")


                    _mutableChannelResult.value = result.data
                    _mutableDataLoading.value = false

                }
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
    }
}