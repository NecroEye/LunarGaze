package com.muratcangzm.lunargaze.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.muratcangzm.lunargaze.databinding.DisplayFragmentLayoutBinding
import com.muratcangzm.lunargaze.ui.adapters.DisplayAdapter
import com.muratcangzm.lunargaze.viewmodels.DisplayViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DisplayFragment : Fragment() {

    private var _binding: DisplayFragmentLayoutBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var displayAdapter: DisplayAdapter
    private val viewModel: DisplayViewModel by viewModels()


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

        viewModel.getChannels(receivedData!!.lowercase())
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



                    if(result.pagination!!.totalCount == 0){
                       binding.displayEmptyText.visibility = View.VISIBLE
                    }
                    else{
                    displayAdapter.submitData(result.channelData!!.toMutableList(), this@DisplayFragment)
                    binding.displayEmptyText.visibility = View.INVISIBLE
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

    }


}