package com.muratcangzm.lunargaze.ui.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.muratcangzm.lunargaze.R
import com.muratcangzm.lunargaze.databinding.DisplayAdapterFragmentBinding
import com.muratcangzm.lunargaze.models.local.FavoriteModel
import com.muratcangzm.lunargaze.repository.FavoriteRepo
import dagger.hilt.android.qualifiers.ActivityContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import kotlin.jvm.Throws

class BookMarkFileAdapter
@Inject
constructor(
    @ActivityContext private val context: Context,
    private val glide: RequestManager,
    private val favoriteRepo: FavoriteRepo
) :
    RecyclerView.Adapter<BookMarkFileAdapter.BookMarkFileHolder>() {


    private lateinit var binding: DisplayAdapterFragmentBinding
    private var roomList = emptyArray<FavoriteModel>()
    private var compositeDisposable = CompositeDisposable()
    private var disposable: Disposable? = null
    private var componentCallbacks: ComponentCallbacks2? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookMarkFileHolder {

        binding = DisplayAdapterFragmentBinding.inflate(LayoutInflater.from(context), parent, false)

        return BookMarkFileHolder()
    }

    @Throws(ArrayIndexOutOfBoundsException::class)
    override fun getItemCount(): Int {
        return roomList.size ?: 0
    }

    override fun onBindViewHolder(holder: BookMarkFileHolder, position: Int) {

        holder.setData(roomList[position], position)


    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    inner class BookMarkFileHolder : RecyclerView.ViewHolder(binding.root) {


        @SuppressLint("NotifyDataSetChanged")
        fun setData(favoriteModel: FavoriteModel, position: Int) {

            binding.apply {

                Glide.get(context).clearMemory()

                glide
                    .load(favoriteModel.imageUrl)
                    .into(displayImage)


                displayCard.setOnClickListener {

                    val bundle = bundleOf("roomModelData" to favoriteModel)
                    Navigation.findNavController(it).navigate(
                        R.id.action_favoritedImageFragment_to_fullScreenImageFragment2,
                        bundle
                    )


                }
                displayCard.setOnLongClickListener {

                    val dialog = androidx.appcompat.app.AlertDialog.Builder(context)

                    dialog.setIcon(R.drawable.baseline_warning_amber_24)
                    dialog.setTitle("Delete process")
                    dialog.setMessage("Do you wanna delete?")

                    dialog.setPositiveButton("Delete") { _, _ ->
                        disposable = favoriteRepo.deleteFavImage(favoriteModel)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                Log.d("Adapter", "Image deleted from database for position: $position")
                                notifyItemRemoved(position)
                                roomList = roomList.filter { it.id != favoriteModel.id }.toTypedArray() // Update internal data structure
                            }, { error ->
                                error.printStackTrace()
                            })

                    }

                    dialog.setNegativeButton("Do Not") { dialog2, _ ->

                        dialog2.dismiss()

                    }

                    dialog.show()

                    return@setOnLongClickListener true
                }
            }
        }
    }

    inner class BookMarkDiffCallback(
        private val oldList: Array<FavoriteModel>,
        private val newList: Array<FavoriteModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // Check for same item based on unique identifier (e.g., model ID)
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldModel = oldList[oldItemPosition]
            val newModel = newList[newItemPosition]
            // Check if content (image URL, title, etc.) is the same
            return oldModel.imageUrl == newModel.imageUrl &&
                    oldModel.id == newModel.id
        }
    }

    fun bindRoomArray(array: Array<FavoriteModel>) {
        val diffResult = DiffUtil.calculateDiff(
            BookMarkDiffCallback(roomList, array),
            true // Batch updates for better performance
        )
        roomList = array
        diffResult.dispatchUpdatesTo(this@BookMarkFileAdapter)
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

        if (disposable!!.isDisposed) {
            compositeDisposable.add(disposable!!)
            compositeDisposable.clear()
        }


    }

}