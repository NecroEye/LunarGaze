package com.muratcangzm.lunargaze.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.muratcangzm.lunargaze.databinding.DisplayFragmentLayoutBinding

class DisplayFragment : Fragment() {


    private var _binding: DisplayFragmentLayoutBinding? = null
    private val binding
        get() = _binding!!


    init {
        //Empty Constructor
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DisplayFragmentLayoutBinding.inflate(inflater, container, false)

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