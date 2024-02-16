package com.muratcangzm.lunargaze.ui.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muratcangzm.lunargaze.databinding.HomeFragmentLayoutBinding
import com.muratcangzm.lunargaze.ui.adapters.CategoryAdapter
import com.muratcangzm.lunargaze.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {


    private var _binding: HomeFragmentLayoutBinding? = null
    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var categoryAdapter: CategoryAdapter

    private val binding
        get() = _binding!!

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

    private fun observeDataChange(){

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.categoriesResult.collect {

                it?.let { result ->

                    Log.d("Veri", "Gelen Veri: $result")
                    categoryAdapter.submitCategory(result)

                }

            }

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