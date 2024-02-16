package com.muratcangzm.lunargaze.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.muratcangzm.lunargaze.databinding.DisplayAdapterFragmentBinding
import com.muratcangzm.lunargaze.models.remote.ChannelModel
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject
import kotlin.jvm.Throws

class DisplayAdapter
@Inject
constructor(
    @ActivityContext private val context: Context,
    private val glide: RequestManager
) : RecyclerView.Adapter<DisplayAdapter.DisplayHolder>() {

    private lateinit var binding: DisplayAdapterFragmentBinding
    private var channelModelList: ChannelModel? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisplayHolder {

        binding = DisplayAdapterFragmentBinding.inflate(LayoutInflater.from(context), parent, false)

        return DisplayHolder()
    }

    @Throws(ArrayIndexOutOfBoundsException::class)
    override fun getItemCount(): Int {
        return channelModelList?.channelData?.size ?: 0
    }

    override fun onBindViewHolder(holder: DisplayHolder, position: Int) {


        holder.setData(channelModelList?.channelData!![position])

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(channelModels: ChannelModel) {

        channelModels.let {
            channelModelList = channelModels
            notifyDataSetChanged()
        }

    }


    inner class DisplayHolder() : RecyclerView.ViewHolder(binding.root) {


        fun setData(data: ChannelModel.ChannelData) {

            binding.apply {

                glide
                    .load(data.user!!.avatarUrl)
                    .into(displayImage)

                displayCard.setOnClickListener{

                    Toast.makeText(context,"${data.displayName}", Toast.LENGTH_SHORT).show()

                }


            }

        }

    }


}