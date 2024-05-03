package com.muratcangzm.lunargaze.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muratcangzm.lunargaze.databinding.HomeFragmentLayoutBinding
import com.muratcangzm.lunargaze.extensions.goneView
import com.muratcangzm.lunargaze.extensions.showView
import com.muratcangzm.lunargaze.ui.adapters.CategoryAdapter
import com.muratcangzm.lunargaze.utils.NetworkChecking
import com.muratcangzm.lunargaze.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {


    private var _binding: HomeFragmentLayoutBinding? = null
    private val binding
        get() = _binding!!
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @get:VisibleForTesting
    val viewModel: HomeViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var categoryAdapter: CategoryAdapter

    @Inject
    lateinit var networkChecking: NetworkChecking

    init {
        //Empty Constructor
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = HomeFragmentLayoutBinding.inflate(inflater, container, false)

        setupViews()
        observeDataChange()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun observeDataChange() {

        if (networkChecking.isNetworkAvailable()) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.categoriesResult.collect {
                    it?.let { result ->

                        categoryAdapter.submitCategory(result)
                        binding.categoryRecycler.showView()
                        binding.loadingScreen.loadingScreenLayout.goneView()

                    }
                }
            }
        } else {
            binding.loadingScreen.loadingScreenLayout.showView()
            binding.categoryRecycler.goneView()

        }
    }

    private fun setupViews() {

        binding.categoryRecycler.apply {

            layoutManager = GridLayoutManager(
                requireContext(), 2, RecyclerView.VERTICAL, false
            )
            adapter = categoryAdapter
            hasFixedSize()

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}