package com.muratcangzm.lunargaze.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.muratcangzm.lunargaze.R
import com.muratcangzm.lunargaze.databinding.HomeFragmentLayoutBinding
import com.muratcangzm.lunargaze.extensions.goneView
import com.muratcangzm.lunargaze.extensions.showView
import com.muratcangzm.lunargaze.ui.MainActivity
import com.muratcangzm.lunargaze.ui.adapters.CategoryAdapter
import com.muratcangzm.lunargaze.ui.fragments.core.BaseFragment
import com.muratcangzm.lunargaze.common.NetworkChecking
import com.muratcangzm.lunargaze.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentLayoutBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @get:VisibleForTesting
    val viewModel: HomeViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var categoryAdapter: CategoryAdapter

    @Inject
    lateinit var networkChecking: NetworkChecking

    private lateinit var bottomNavigation: BottomNavigationView

    override val layoutId: Int
        get() = R.layout.home_fragment_layout

    companion object {
        private const val TAG = "HomeFragment"
    }

    init {
        //Empty Constructor
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressed)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as? MainActivity

        fragmentName = "HomeFragment"

        if (mainActivity != null)
            bottomNavigation = mainActivity.getBottomNavigationView()
        else
            Timber.tag(TAG).d("not initialized :/ ")

        setupViews()
        observeDataChange()

    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): HomeFragmentLayoutBinding {
        return HomeFragmentLayoutBinding.inflate(inflater, container, false)
    }

    override fun HomeFragmentLayoutBinding.initializeViews() {
        //Not necessary rn
    }

    private fun observeDataChange() {

        if (networkChecking.isNetworkAvailable()) {

            viewLifecycleOwner.lifecycleScope.launch(exceptionHandler) {
                supervisorScope {

                    launch {
                        viewModel.tenorCategoryResult.collectLatest {
                            it?.let { result ->
                                Timber.tag("Tenor Data:").d("${result.tags.size}")
                            }
                        }
                    }

                    launch {
                        viewModel.categoriesResult.collectLatest {
                            it?.let { result ->
                                Timber.tag("Giphy Data:").d("${result.categories?.size}")
                                categoryAdapter.submitCategory(result)
                                binding.categoryRecycler.showView()
                                binding.loadingScreen.loadingScreenLayout.goneView()

                            }
                        }
                    }
                }
            }
        } else {
            binding.loadingScreen.loadingScreenLayout.showView()
            binding.categoryRecycler.goneView()
        }
    }

    //TODO: while downloading picture or gif add a progress bar
    private fun setupViews() {
        binding.categoryRecycler.apply {
            layoutManager = GridLayoutManager(
                requireContext(), 2, RecyclerView.VERTICAL, false
            )
            adapter = categoryAdapter
            hasFixedSize()

            binding.categoryRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy > 0) {
                        bottomNavigation.animate()
                            .translationY(bottomNavigation.height.toFloat()).duration = 300
                    } else if (dy < 0) {
                        bottomNavigation.animate().translationY(0f).duration = 300
                    }
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private val onBackPressed = object : OnBackPressedCallback(enabled = true){
        override fun handleOnBackPressed() {
            TODO("Not yet implemented")
        }
    }
}