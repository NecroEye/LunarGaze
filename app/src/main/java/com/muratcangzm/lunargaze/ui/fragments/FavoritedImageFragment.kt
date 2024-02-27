package com.muratcangzm.lunargaze.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.muratcangzm.lunargaze.databinding.FavoritedImageLayoutBinding
import com.muratcangzm.lunargaze.models.local.FavoriteModel

class FavoritedImageFragment : Fragment() {


    private var _binding: FavoritedImageLayoutBinding? = null
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
        _binding = FavoritedImageLayoutBinding.inflate(inflater, container, false)

        val receivedData = requireArguments().getParcelableArray("roomData") as Array<FavoriteModel>?

        Log.d("Yerel Data: ", "${receivedData?.size}")





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