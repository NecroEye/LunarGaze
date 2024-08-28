package com.muratcangzm.lunargaze.ui.fragments.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.muratcangzm.lunargaze.common.utils.log
import kotlinx.coroutines.CoroutineExceptionHandler

abstract class BaseFragment<VB : ViewBinding> : Fragment() {


    private var _binding: VB? = null
    protected val binding: VB
            by lazy(LazyThreadSafetyMode.NONE) {
                _binding ?: error("fragment view not initialized")
            }

    protected var fragmentName: String? = null

    protected val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        fragmentName?.let {
            log("$fragmentName Coroutine Error ${throwable.message.toString()}")
        }
    }

    @get:LayoutRes
    abstract val layoutId: Int

    protected abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VB


    protected abstract fun VB.initializeViews()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = inflateBinding(inflater, container)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.initializeViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}