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
import com.muratcangzm.lunargaze.databinding.DisplayFragmentLayoutBinding
import com.muratcangzm.lunargaze.viewmodels.DisplayViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DisplayFragment : Fragment() {

    private var _binding: DisplayFragmentLayoutBinding? = null
    private val binding
        get() = _binding!!

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
        Log.d("ReceivedData", "$receivedData")

        viewModel.getChannels(receivedData!!)
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


                    Log.d("DisplayFragment Data: ", "$result")
                    if(result.channelData!!.isNotEmpty())
                    binding.displayText.text = "Display Screen ${result.channelData[0].displayName ?: "Boş"}"

                }

            }

        }

    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null

    }


}