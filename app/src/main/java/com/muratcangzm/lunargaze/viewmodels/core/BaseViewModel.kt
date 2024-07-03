package com.muratcangzm.lunargaze.viewmodels.core

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {

    open val compositeDisposable by lazy {
        CompositeDisposable()
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}