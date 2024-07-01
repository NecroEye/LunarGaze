package com.muratcangzm.lunargaze.ui.fragments.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {


    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding ?: error("fragment view not initialized")


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