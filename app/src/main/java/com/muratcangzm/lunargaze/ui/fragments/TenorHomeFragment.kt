package com.muratcangzm.lunargaze.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.muratcangzm.lunargaze.R
import com.muratcangzm.lunargaze.databinding.TenorHomeFragmentLayoutBinding
import com.muratcangzm.lunargaze.ui.fragments.core.BaseFragment
import com.muratcangzm.lunargaze.viewmodels.TenorViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class TenorHomeFragment : BaseFragment<TenorHomeFragmentLayoutBinding>(){


    override val layoutId: Int
        get() = R.layout.tenor_home_fragment_layout

    @Inject
    lateinit var viewModelFactory:ViewModelProvider.Factory

    private val tenorViewModel: TenorViewModel by viewModels{viewModelFactory}

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): TenorHomeFragmentLayoutBinding {
        return TenorHomeFragmentLayoutBinding.inflate(inflater, container, false)
    }


    override fun TenorHomeFragmentLayoutBinding.initializeViews() {
        //Not necessary here rn
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentName = "TenorHomeFragment"

        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewLifecycleOwner.lifecycleScope.launch(exceptionHandler) {

            tenorViewModel.tenorCategory.collectLatest {result ->
                result?.let {
                    Timber.tag("Tenor Data Fragment: ").d("${result.tags.size}")
                }
            }

        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
    }

}



