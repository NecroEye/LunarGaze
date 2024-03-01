package com.muratcangzm.lunargaze.ui.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
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

    @SuppressLint("NotifyDataSetChanged")
    fun bindRoomArray(array: Array<FavoriteModel>) {

        if (array.isNotEmpty()) {
            roomList = array
            notifyDataSetChanged()
        }
    }


    inner class BookMarkFileHolder : RecyclerView.ViewHolder(binding.root) {


        @SuppressLint("NotifyDataSetChanged")
        fun setData(favoriteModel: FavoriteModel, position: Int) {

            binding.apply {

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
                            .subscribe()

                        notifyItemRemoved(position)
                        notifyDataSetChanged()

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

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

        if (disposable!!.isDisposed) {
            compositeDisposable.add(disposable!!)
            compositeDisposable.clear()
        }


    }

}