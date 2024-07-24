package com.muratcangzm.lunargaze.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.muratcangzm.lunargaze.databinding.SearchDisplayFragmentLayoutBinding
import com.muratcangzm.lunargaze.extensions.goneView
import com.muratcangzm.lunargaze.extensions.hideView
import com.muratcangzm.lunargaze.extensions.showView
import com.muratcangzm.lunargaze.models.remote.giphy.ChannelModel
import com.muratcangzm.lunargaze.ui.adapters.DisplayAdapter
import com.muratcangzm.lunargaze.utils.NetworkChecking
import com.muratcangzm.lunargaze.utils.log
import com.muratcangzm.lunargaze.viewmodels.DisplayViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class SearchDisplayFragment : Fragment() {


    private var _binding: SearchDisplayFragmentLayoutBinding? = null
    private val binding
            by lazy(LazyThreadSafetyMode.NONE) {
                _binding!!
            }

    @Inject
    lateinit var searchAdapter: DisplayAdapter

    @Inject
    lateinit var networkChecking: NetworkChecking

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @get:VisibleForTesting
    val viewModel: DisplayViewModel by viewModels() { viewModelFactory }

    private var offset: Int? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        log("SearchDisplayFragment Coroutine Error ${throwable.message.toString()}")
    }

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

        binding.browseWord.text = receivedData

        binding.searchAdapter.adapter = searchAdapter
        binding.searchAdapter.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.searchAdapter.hasFixedSize()



        viewModel.getChannels(receivedData.trim().lowercase(), offset)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeDataChange()

    }

    @SuppressLint("SetTextI18n")
    private fun observeDataChange() {

        if (networkChecking.isNetworkAvailable()) {

            viewLifecycleOwner.lifecycleScope.launch(exceptionHandler) {
                viewModel.channelResult.collectLatest {

                    it?.let {

                        if (it.pagination!!.totalCount == 0) {
                            binding.searchFragmentEmpty.showView()
                            binding.searchLoadingScreen.loadingScreenLayout.goneView()
                        } else {
                            binding.searchFragmentEmpty.hideView()
                            binding.searchLoadingScreen.loadingScreenLayout.goneView()
                            searchAdapter.submitData(
                                it.channelData!!.toMutableList(),
                                this@SearchDisplayFragment
                            )
                            binding.searchAdapter.showView()
                        }
                    }
                }
            }
        } else {
            binding.searchLoadingScreen.loadingScreenLayout.showView()
            binding.searchAdapter.goneView()
        }
    }

    //Cannot get more than 50 because of call limit
    private fun randomizer(channelModel: ChannelModel) {

        val randomModel = mutableListOf<ChannelModel.ChannelData>()

        repeat(50) {

            val random = channelModel.pagination!!.totalCount?.let { Random.nextInt(0, it) }
            randomModel.add(channelModel.channelData!![random!!])

        }

        binding.searchFragmentEmpty.hideView()
        binding.searchLoadingScreen.loadingScreenLayout.goneView()
        searchAdapter.submitData(randomModel, this@SearchDisplayFragment)
        binding.searchAdapter.showView()

    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
        offset = null


    }


}
