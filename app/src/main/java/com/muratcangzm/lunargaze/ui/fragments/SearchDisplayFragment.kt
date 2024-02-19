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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.muratcangzm.lunargaze.databinding.SearchDisplayFragmentLayoutBinding
import com.muratcangzm.lunargaze.ui.adapters.CategoryAdapter
import com.muratcangzm.lunargaze.ui.adapters.DisplayAdapter
import com.muratcangzm.lunargaze.viewmodels.DisplayViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchDisplayFragment : Fragment() {


    private var _binding: SearchDisplayFragmentLayoutBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var searchAdapter: DisplayAdapter


    private val viewModel: DisplayViewModel by viewModels()

    init {

        //Empty Constructor

    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchDisplayFragmentLayoutBinding.inflate(inflater, container, false)

        val receivedData = requireArguments().getString("searchData")

        binding.searchAdapter.adapter = searchAdapter
        binding.searchAdapter.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.searchAdapter.hasFixedSize()


        viewModel.getChannels(receivedData!!.lowercase())
        observeDataChange()


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    private fun observeDataChange() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.channelResult.collect {

                // if pagination's total count is above 50 then create a random number generator its size demands pagination count and only 50 image picks

                it?.let {
                    if (it.pagination!!.totalCount == 0) {
                        binding.searchFragmentEmpty.visibility = View.VISIBLE
                    } else {
                        binding.searchFragmentEmpty.visibility = View.INVISIBLE
                        searchAdapter.submitData(it, this@SearchDisplayFragment)
                    }

                }

            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null

    }


}
