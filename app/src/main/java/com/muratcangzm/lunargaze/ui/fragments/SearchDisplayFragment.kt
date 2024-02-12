package com.muratcangzm.lunargaze.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.muratcangzm.lunargaze.databinding.SearchDisplayFragmentLayoutBinding

class SearchDisplayFragment : Fragment() {


    private var _binding: SearchDisplayFragmentLayoutBinding? = null
    private val binding
        get() = _binding!!

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

        binding.searchFragment.text = "SearchDisplay: you searched that $receivedData"

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null

    }


}