package com.muratcangzm.lunargaze.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.muratcangzm.lunargaze.R
import com.muratcangzm.lunargaze.databinding.FavoriteFolderLayoutBinding
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject
import kotlin.jvm.Throws

class FavoriteFileAdapter
@Inject
constructor(@ActivityContext private val context: Context) :
    RecyclerView.Adapter<FavoriteFileAdapter.FavFileHolder>() {

    private lateinit var binding: FavoriteFolderLayoutBinding
    private var emptyFileName = emptyList<String>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavFileHolder {

        binding = FavoriteFolderLayoutBinding.inflate(LayoutInflater.from(context), parent, false)

        return FavFileHolder()

    }

    @Throws(ArrayIndexOutOfBoundsException::class)
    override fun getItemCount(): Int {

        return emptyFileName.size ?: 0

    }


    override fun onBindViewHolder(holder: FavFileHolder, position: Int) {

        holder.setData(emptyFileName[position])

    }


    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitFileNames(files: List<String>) {

        files.let {
            emptyFileName = files
        }
        notifyDataSetChanged()

    }

    inner class FavFileHolder() : RecyclerView.ViewHolder(binding.root) {


        fun setData(fileName: String) {

            binding.apply {
                customFolderName.text = fileName

                fileCard.setOnClickListener{

                 it.findNavController().navigate(R.id.toBookMarked)

                }

                fileCard.setOnLongClickListener{

                    Toast.makeText(context, fileName, Toast.LENGTH_SHORT).show()

                    return@setOnLongClickListener true
                }
            }
        }


    }


}