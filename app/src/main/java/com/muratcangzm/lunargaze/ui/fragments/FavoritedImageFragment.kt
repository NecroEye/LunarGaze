package com.muratcangzm.lunargaze.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.muratcangzm.lunargaze.databinding.FavoritedImageLayoutBinding
import com.muratcangzm.lunargaze.extensions.hideView
import com.muratcangzm.lunargaze.extensions.showView
import com.muratcangzm.lunargaze.ui.adapters.BookMarkFileAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class FavoritedImageFragment : Fragment() {

    private var _binding: FavoritedImageLayoutBinding? = null
    private val binding
        by lazy(LazyThreadSafetyMode.NONE) { _binding!! }

    @Inject
    lateinit var bookmarkAdapter: BookMarkFileAdapter

    companion object{
        private const val TAG = "FavoriteImageFragment"
    }

    init {
        //Empty Constructor
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoritedImageLayoutBinding.inflate(inflater, container, false)

        val receivedData =
            requireArguments().getParcelableArray("roomData") as Array<com.muratcangzm.models.local.FavoriteModel>?

        Timber.tag(TAG).d("Yerel Data: ${receivedData?.size}")

        if (receivedData!!.isNotEmpty()) {
            bookmarkAdapter.bindRoomArray(receivedData)
            binding.favoritedText.hideView()
        } else {
            bookmarkAdapter.bindRoomArray(emptyArray())
            binding.favoritedText.showView()
        }
        binding.bookmarkRecycler.adapter = bookmarkAdapter
        binding.bookmarkRecycler.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.bookmarkRecycler.hasFixedSize()

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