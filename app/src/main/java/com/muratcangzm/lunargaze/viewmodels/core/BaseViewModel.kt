package com.muratcangzm.lunargaze.viewmodels.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muratcangzm.lunargaze.utils.log
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive

open class BaseViewModel : ViewModel() {

    protected var viewModelName: String? = null

    protected val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelName?.let {
            log("$viewModelName Coroutine Error ${throwable.message.toString()}")
        }
    }

    open val compositeDisposable by lazy(LazyThreadSafetyMode.NONE) {
        CompositeDisposable()
    }


    override fun onCleared() {
        super.onCleared()

        compositeDisposable.dispose()

        if (viewModelScope.isActive)
            viewModelScope.cancel()

    }
}