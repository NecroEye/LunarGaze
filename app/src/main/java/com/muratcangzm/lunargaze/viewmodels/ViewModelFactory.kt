package com.muratcangzm.lunargaze.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.muratcangzm.lunargaze.repository.GiphyRepo

object ViewModelFactory {

    fun provideFactory(
        giphyRepo: GiphyRepo
    ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when (modelClass) {
                DisplayViewModel::class.java -> DisplayViewModel(giphyRepo) as T
                HomeViewModel::class.java -> HomeViewModel(giphyRepo) as T
                SearchViewModel::class.java -> SearchViewModel(giphyRepo) as T
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.simpleName}")
            }
        }
    }
}



