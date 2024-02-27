package com.muratcangzm.lunargaze.ui.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.muratcangzm.lunargaze.R
import com.muratcangzm.lunargaze.databinding.FavoriteFolderLayoutBinding
import com.muratcangzm.lunargaze.models.local.FavoriteDao
import com.muratcangzm.lunargaze.models.local.FavoriteModel
import com.muratcangzm.lunargaze.repository.FavoriteRepo
import com.muratcangzm.lunargaze.ui.fragments.FavoritesFragmentDirections
import dagger.hilt.android.qualifiers.ActivityContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import kotlin.jvm.Throws

class FavoriteFileAdapter
@Inject
constructor(
    @ActivityContext private val context: Context,
    private val sharedPreferences: SharedPreferences,
    private val repo: FavoriteRepo
) :
    RecyclerView.Adapter<FavoriteFileAdapter.FavFileHolder>() {

    private lateinit var binding: FavoriteFolderLayoutBinding
    private var emptyFileName = emptyList<String>()
    private var sharedEditor = sharedPreferences.edit()
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


        @SuppressLint("NotifyDataSetChanged")
        fun setData(fileName: String) {

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
                                } == true
                            }

                             val action = FavoritesFragmentDirections.toBookMarked(newData.toTypedArray())
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

                            sharedEditor.remove(fileName).apply()
                            emptyFileName = emptyFileName.filter { it != fileName }
                            notifyDataSetChanged()
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

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

        if (disposable != null) {

            disposable!!.dispose()
            disposable = null

        }


    }

}