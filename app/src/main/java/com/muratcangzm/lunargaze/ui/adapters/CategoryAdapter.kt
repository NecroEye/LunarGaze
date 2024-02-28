package com.muratcangzm.lunargaze.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.muratcangzm.lunargaze.R
import com.muratcangzm.lunargaze.databinding.CategoryRecyclerLayoutBinding
import com.muratcangzm.lunargaze.models.remote.CategoryModel
import com.muratcangzm.lunargaze.ui.fragments.HomeFragmentDirections
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class CategoryAdapter
@Inject constructor(
    @ActivityContext private val context: Context,
    private val glide: RequestManager
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private lateinit var binding: CategoryRecyclerLayoutBinding
    private var categoryList: CategoryModel? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        binding = CategoryRecyclerLayoutBinding.inflate(LayoutInflater.from(context), parent, false)

        return CategoryViewHolder()

    }

    @Throws(ArrayIndexOutOfBoundsException::class)
    override fun getItemCount(): Int {

        return categoryList?.categories?.size ?: 0

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        holder.setData(categoryList?.categories!![position])

    }


    @SuppressLint("NotifyDataSetChanged")
    fun submitCategory(categories: CategoryModel) {

        categories.let {
            categoryList = categories
            notifyDataSetChanged()

        }

    }


    inner class CategoryViewHolder : RecyclerView.ViewHolder(binding.root) {


        @SuppressLint("SuspiciousIndentation")
        fun setData(data: CategoryModel.CategoryData) {

            binding.apply {

                glide
                    .asGif()
                    .load(data.gifObject!!.imageDimensions!!.fixedWidthStill!!.fixed480Url)
                    .into(categoryImage)

                categoryText.text = data.name ?: "Empty"

                categoryCard.setOnClickListener {


                    val bundle = bundleOf("searchData" to data)

                    Navigation
                        .findNavController(it)
                        .navigate(R.id.action_homeFragment_to_searchDisplayFragment, bundle)


                }


            }
        }


    }


    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

        categoryList = null
    }

}