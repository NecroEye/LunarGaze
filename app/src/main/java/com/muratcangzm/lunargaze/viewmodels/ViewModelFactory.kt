package com.muratcangzm.lunargaze.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.muratcangzm.lunargaze.repository.remote.GiphyRepo
import com.muratcangzm.lunargaze.repository.remote.TenorRepo

object ViewModelFactory {

    fun provideFactory(
        giphyRepo: GiphyRepo,
        tenorRepo: TenorRepo
    ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when (modelClass) {
                DisplayViewModel::class.java -> DisplayViewModel(giphyRepo) as T
                HomeViewModel::class.java -> HomeViewModel(giphyRepo, tenorRepo) as T
                SearchViewModel::class.java -> SearchViewModel(giphyRepo) as T
                TenorViewModel::class.java -> TenorViewModel(tenorRepo) as T
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.simpleName}")
            }
        }
    }
}


