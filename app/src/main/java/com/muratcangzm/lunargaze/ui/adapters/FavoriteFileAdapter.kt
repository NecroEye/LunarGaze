package com.muratcangzm.lunargaze.ui.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.muratcangzm.lunargaze.R
import com.muratcangzm.lunargaze.databinding.FavoriteFolderLayoutBinding
import com.muratcangzm.lunargaze.models.local.FavoriteModel
import com.muratcangzm.lunargaze.repository.DataStoreRepo
import com.muratcangzm.lunargaze.repository.FavoriteRepo
import com.muratcangzm.lunargaze.ui.fragments.FavoritesFragmentDirections
import dagger.hilt.android.qualifiers.ActivityContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.jvm.Throws

class FavoriteFileAdapter
@Inject
constructor(
    @ActivityContext private val context: Context,
    private val dataStoreRepo: DataStoreRepo,
    private val repo: FavoriteRepo,
    private val favoriteRepo: FavoriteRepo
) :
    RecyclerView.Adapter<FavoriteFileAdapter.FavFileHolder>() {

    private lateinit var binding: FavoriteFolderLayoutBinding
    private var emptyFileName = emptyList<String>()
    private var deletedOnes = emptyList<FavoriteModel>()
    private var disposable: Disposable? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavFileHolder {

        binding = FavoriteFolderLayoutBinding.inflate(LayoutInflater.from(context), parent, false)

        return FavFileHolder()

    }

    @Throws(ArrayIndexOutOfBoundsException::class)
    override fun getItemCount(): Int {

        return emptyFileName.size ?: 0

    }


    override fun onBindViewHolder(holder: FavFileHolder, position: Int) {

        holder.setData(emptyFileName[position], position)

    }


    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    inner class FavFileHolder() : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("NotifyDataSetChanged")
        fun setData(fileName: String, position: Int) {

            binding.apply {
                customFolderName.text = fileName

                fileCard.setOnClickListener { button ->


                    disposable = repo.getAllFavImages()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ roomList ->

                            Log.d("Bütün oda: ", "${roomList.size}")

                            val newData = roomList.filter { favorite ->
                                favorite.folder?.any { folderName ->
                                    folderName == fileName
                                } == true // any'nin döndüreceği boolean eşitse true'ya
                            }

                            val action =
                                FavoritesFragmentDirections.toBookMarked(newData.toTypedArray())
                            button.findNavController().navigate(action)

                        },
                            { error ->
                                error.localizedMessage?.let { Log.d("Bütün oda: ", it) }
                            })

                }

                fileCard.setOnLongClickListener {

                    AlertDialog.Builder(context)
                        .setTitle("Do you wanna delete this folder?")
                        .setMessage("your all data is gonna be deleted!")
                        .setIcon(R.drawable.baseline_warning_amber_24)
                        .setPositiveButton("Delete") { dialog, _ ->

                            CoroutineScope(Dispatchers.IO).launch{
                                dataStoreRepo.deleteNameFromPreferences(fileName.uppercase())

                            }

                            emptyFileName = emptyFileName.filter { it != fileName }
                            removeAnItemFromList(position)


                            disposable = repo.getAllFavImages()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ roomList ->

                                    deletedOnes = roomList.filter { folderName ->
                                        folderName.folder!!.any { folder ->
                                            emptyFileName.contains(
                                                folder
                                            )
                                        }
                                    }

                                    for (deleted in deletedOnes) {
                                        favoriteRepo.deleteFavImage(deleted)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe()
                                    }

                                },
                                    { error ->
                                        error.localizedMessage?.let { Log.d("Bütün oda: ", it) }
                                    })



                            dialog.dismiss()

                        }
                        .setNegativeButton("Do not") { dialog, _ ->

                            dialog.dismiss()

                        }
                        .show()


                    return@setOnLongClickListener true
                }
            }
        }


    }

    inner class FavoriteFileDiffCallback(
        private val oldList: List<String>,
        private val newList: List<String>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // Check if items are the same by comparing their positions (assuming unique positions)
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // Check if content (file names) are the same
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    fun submitFileNames(files: List<String>) {
        val diffResult = DiffUtil.calculateDiff(
            FavoriteFileDiffCallback(emptyFileName, files),
            false // Batch updates for better performance
        )
        emptyFileName = files
        diffResult.dispatchUpdatesTo(this@FavoriteFileAdapter)
    }

    fun addAnItemToList(position: Int) {

        //emptyFileName.add(position, folderName)
        notifyItemInserted(position)
        notifyItemRangeInserted(position, emptyFileName.size)


    }

    fun removeAnItemFromList(position: Int) {

        //emptyFileName.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, emptyFileName.size)

    }


    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

        if (disposable != null) {

            disposable!!.dispose()
            disposable = null

        }


    }

}