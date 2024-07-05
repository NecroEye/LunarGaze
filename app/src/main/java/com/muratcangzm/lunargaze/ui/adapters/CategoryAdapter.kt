package com.muratcangzm.lunargaze.ui.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.muratcangzm.lunargaze.R
import com.muratcangzm.lunargaze.databinding.CategoryRecyclerLayoutBinding
import com.muratcangzm.lunargaze.models.remote.giphy.CategoryModel
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class CategoryAdapter
@Inject constructor(
    @ActivityContext private val context: Context,
    private val glide: RequestManager
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private lateinit var binding: CategoryRecyclerLayoutBinding
    private var categoryList: CategoryModel? = null
    private var componentCallbacks:ComponentCallbacks2? = null


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


                    val bundle = bundleOf("searchData" to data.name)

                    Navigation
                        .findNavController(it)
                        .navigate(R.id.action_homeFragment_to_searchDisplayFragment, bundle)

                }

            }
        }


    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        componentCallbacks = object : ComponentCallbacks2 {
            override fun onTrimMemory(level: Int) {
                Glide.get(context).onTrimMemory(level)
            }

            override fun onConfigurationChanged(newConfig: Configuration) {
                // Not implemented
            }

            override fun onLowMemory() {
                Glide.get(context).clearDiskCache()
                Glide.get(context).clearMemory()
            }
        }

    }


    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

        (context as Activity).applicationContext.unregisterComponentCallbacks(componentCallbacks)
        categoryList = null
    }


}