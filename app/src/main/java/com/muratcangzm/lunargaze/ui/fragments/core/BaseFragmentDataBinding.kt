package com.muratcangzm.lunargaze.ui.fragments.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragmentDataBinding<DB : ViewBinding>() : Fragment() {


    private var _binding: DB? = null

    protected val binding: DB
        get() = _binding!!


    @get:LayoutRes
    abstract val layoutId: Int


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}