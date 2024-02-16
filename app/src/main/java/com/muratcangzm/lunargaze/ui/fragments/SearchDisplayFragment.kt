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
import com.muratcangzm.lunargaze.databinding.SearchDisplayFragmentLayoutBinding
import com.muratcangzm.lunargaze.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchDisplayFragment : Fragment() {


    private var _binding: SearchDisplayFragmentLayoutBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

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
        Log.d("ReceivedData", "$receivedData")

        binding.searchFragment.text = "you searched: $receivedData"
        viewModel.fetchSearchData(receivedData!!.lowercase())
        observeDataChange()


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    private fun observeDataChange() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchResult.collect {


                    Log.d("SearchDisplayFragment Data: ", "$it")


            }

        }


    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null

    }


}
