package com.muratcangzm.lunargaze.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.muratcangzm.lunargaze.databinding.DisplayAdapterFragmentBinding
import com.muratcangzm.lunargaze.models.local.FavoriteModel
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject
import kotlin.jvm.Throws

class BookMarkFileAdapter
@Inject
constructor(
    @ActivityContext private val context: Context,
    private val glide: RequestManager
) :
    RecyclerView.Adapter<BookMarkFileAdapter.BookMarkFileHolder>() {


    private lateinit var binding: DisplayAdapterFragmentBinding
    private var roomList = emptyArray<FavoriteModel>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookMarkFileHolder {

        binding = DisplayAdapterFragmentBinding.inflate(LayoutInflater.from(context), parent, false)

        return BookMarkFileHolder()
    }

    @Throws(ArrayIndexOutOfBoundsException::class)
    override fun getItemCount(): Int {
        return roomList.size ?: 0
    }

    override fun onBindViewHolder(holder: BookMarkFileHolder, position: Int) {

        holder.setData(roomList[position])


    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun bindRoomArray(array: Array<FavoriteModel>) {

        if (array.isNotEmpty()) {
            roomList = array
            notifyDataSetChanged()
        }
    }


    inner class BookMarkFileHolder : RecyclerView.ViewHolder(binding.root) {


        fun setData(favoriteModel: FavoriteModel) {

            binding.apply {

                glide
                    .load(favoriteModel.imageUrl)
                    .into(displayImage)


                displayCard.setOnClickListener {


                }
            }
        }
    }
}