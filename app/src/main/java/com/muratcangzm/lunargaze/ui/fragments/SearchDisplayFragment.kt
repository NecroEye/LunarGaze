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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.muratcangzm.lunargaze.databinding.SearchDisplayFragmentLayoutBinding
import com.muratcangzm.lunargaze.models.remote.ChannelModel
import com.muratcangzm.lunargaze.ui.adapters.CategoryAdapter
import com.muratcangzm.lunargaze.ui.adapters.DisplayAdapter
import com.muratcangzm.lunargaze.utils.NetworkChecking
import com.muratcangzm.lunargaze.viewmodels.DisplayViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class SearchDisplayFragment : Fragment() {


    private var _binding: SearchDisplayFragmentLayoutBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var searchAdapter: DisplayAdapter

    @Inject
    lateinit var networkChecking: NetworkChecking

    private var offset: Int? = null

    private val viewModel: DisplayViewModel by viewModels()
    private val args: SearchDisplayFragmentArgs by navArgs()

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

        val receivedData = args.searchData

        binding.searchAdapter.adapter = searchAdapter
        binding.searchAdapter.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.searchAdapter.hasFixedSize()



        viewModel.getChannels(receivedData.trim().lowercase(), null)
        observeDataChange()


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    private fun observeDataChange() {

        if (networkChecking.isNetworkAvailable()) {

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.channelResult.collect {

                    it?.let {

                        if (it.pagination!!.totalCount == 0) {
                            binding.searchFragmentEmpty.visibility = View.VISIBLE
                            binding.searchLoadingScreen.loadingScreenLayout.visibility = View.GONE
                        } else {
                            binding.searchFragmentEmpty.visibility = View.INVISIBLE
                            binding.searchLoadingScreen.loadingScreenLayout.visibility = View.GONE
                            searchAdapter.submitData(
                                it.channelData!!.toMutableList(),
                                this@SearchDisplayFragment
                            )
                            binding.searchAdapter.visibility = View.VISIBLE
                        }
                    }
                }
            }
        } else {
            binding.searchLoadingScreen.loadingScreenLayout.visibility = View.VISIBLE
            binding.searchAdapter.visibility = View.GONE
        }
    }

    //Cannot get more than 50 because of call limit
    private fun randomizer(channelModel: ChannelModel) {

        val randomModel = mutableListOf<ChannelModel.ChannelData>()

        repeat(50) {

            val random = channelModel.pagination!!.totalCount?.let { Random.nextInt(0, it) }
            randomModel.add(channelModel.channelData!![random!!])

        }

        binding.searchFragmentEmpty.visibility = View.INVISIBLE
        binding.searchLoadingScreen.loadingScreenLayout.visibility = View.GONE
        searchAdapter.submitData(randomModel, this@SearchDisplayFragment)
        binding.searchAdapter.visibility = View.VISIBLE

    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
        offset = null


    }


}
