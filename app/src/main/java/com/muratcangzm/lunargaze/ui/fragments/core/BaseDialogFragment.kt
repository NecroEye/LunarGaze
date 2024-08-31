package com.muratcangzm.lunargaze.ui.fragments.core

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.muratcangzm.lunargaze.R

abstract class BaseDialogFragment<DB : ViewDataBinding> : DialogFragment() {

    private var _binding: DB? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NORMAL, R.style.DialogOpeningAnimation)
        return super.onCreateDialog(savedInstanceState)
    }

    @LayoutRes
    protected abstract fun getLayoutResId(): Int

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}