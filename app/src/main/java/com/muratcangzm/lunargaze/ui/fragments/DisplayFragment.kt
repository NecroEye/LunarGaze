package com.muratcangzm.lunargaze.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.muratcangzm.lunargaze.databinding.DisplayFragmentLayoutBinding
import com.muratcangzm.lunargaze.ui.adapters.DisplayAdapter
import com.muratcangzm.lunargaze.utils.NetworkChecking
import com.muratcangzm.lunargaze.viewmodels.DisplayViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.muratcangzm.lunargaze.extensions.hideView
import com.muratcangzm.lunargaze.extensions.showView
import javax.inject.Inject

@AndroidEntryPoint
class DisplayFragment : Fragment() {

    private var _binding: DisplayFragmentLayoutBinding? = null
    private val binding
        get() = _binding!!

    private var offset: Int? = null

    @Inject
    lateinit var networkChecking: NetworkChecking

    @Inject
    lateinit var displayAdapter: DisplayAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @get:VisibleForTesting
    val viewModel: DisplayViewModel by viewModels { viewModelFactory }


    init {
        //Empty Constructor
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DisplayFragmentLayoutBinding.inflate(inflater, container, false)

        val receivedData = requireArguments().getString("channelData")
        setAdapter()



        viewModel.getChannels(receivedData!!.lowercase(), offset)
        observeDataChange()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    private fun observeDataChange() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.channelResult.collect {

                it?.let { result ->

                    if (result.pagination!!.totalCount == 0) {
                        binding.displayEmptyText.showView()

                    } else {
                        displayAdapter.submitData(
                            result.channelData!!.toMutableList(),
                            this@DisplayFragment
                        )
                        binding.displayEmptyText.hideView()


                    }
                }
            }
        }

    }

    private fun setAdapter() {

        binding.displayRecycler.apply {

            adapter = displayAdapter
            layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            hasFixedSize()

        }

    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
        offset = null

    }


}