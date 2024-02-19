package com.muratcangzm.lunargaze.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.muratcangzm.lunargaze.R
import com.muratcangzm.lunargaze.databinding.ImageFullscreenLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FullScreenImageFragment : Fragment() {


    private var _binding: ImageFullscreenLayoutBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var glide: RequestManager
    private var receivedData: String? = null


    init {

        //Empty Constructor

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ImageFullscreenLayoutBinding.inflate(inflater, container, false)

        receivedData = requireArguments().getString("imageData")
        Log.d("FullScreenData: ", " $receivedData")


        setUIComponent()

        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setUIComponent() {

        binding.apply {

            glide
                .load(receivedData)
                .into(fullScreenImage)

            backButton.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        receivedData = null
        _binding = null
    }
}