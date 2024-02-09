package com.muratcangzm.lunargaze.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.muratcangzm.lunargaze.databinding.FavoritesFragmentLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {


    private var _binding: FavoritesFragmentLayoutBinding? = null
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

        _binding = FavoritesFragmentLayoutBinding.inflate(inflater, container, false)

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