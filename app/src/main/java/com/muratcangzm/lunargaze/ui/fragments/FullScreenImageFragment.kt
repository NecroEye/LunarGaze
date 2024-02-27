package com.muratcangzm.lunargaze.ui.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ClipData
import android.content.Intent
import android.content.ClipboardManager
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.content.Context
import android.widget.LinearLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.muratcangzm.lunargaze.R
import com.muratcangzm.lunargaze.databinding.ImageFullscreenLayoutBinding
import com.muratcangzm.lunargaze.models.local.FavoriteModel
import com.muratcangzm.lunargaze.models.remote.ChannelModel
import com.muratcangzm.lunargaze.repository.FavoriteRepo
import com.muratcangzm.lunargaze.ui.adapters.RadioButtonAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class FullScreenImageFragment : Fragment() {


    private var _binding: ImageFullscreenLayoutBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var glide: RequestManager

    @Inject
    lateinit var radioAdapter: RadioButtonAdapter

    @Inject
    lateinit var favoriteRepo: FavoriteRepo

    private var receivedData: ChannelModel.ChannelData? = null
    private var alertDialog: AlertDialog? = null
    private var favoriteModel: FavoriteModel? = null
    private var compositeDisposable = CompositeDisposable()


    init {

        //Empty Constructor

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ImageFullscreenLayoutBinding.inflate(inflater, container, false)

        receivedData = requireArguments().getParcelable<ChannelModel.ChannelData>("imageData")
        Log.d("FullScreenData: ", " $receivedData")


        setUIComponent()

        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("InflateParams")
    private fun setUIComponent() {

        binding.apply {

            glide
                .load(receivedData!!.user!!.avatarUrl)
                .into(fullScreenImage)

            backButton.setOnClickListener {
                findNavController().navigateUp()
            }

            bottomSheetPopUp.setOnClickListener {
                showBottomSheet()

            }

            bookmarkedButtonCard.setOnClickListener {

                val inflater = LayoutInflater.from(requireContext())
                val popupSave = inflater.inflate(R.layout.saved_popup_layout, null)

                val radioRecycler = popupSave.findViewById<RecyclerView>(R.id.radioButtonRecycler)
                val saveButton = popupSave.findViewById<MaterialButton>(R.id.popupSaveButton)

                radioRecycler.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                radioRecycler.adapter = radioAdapter
                radioRecycler.hasFixedSize()


                val builder = AlertDialog.Builder(requireContext())
                builder.setView(popupSave)
                alertDialog = builder.create()

                alertDialog?.show()

                saveButton.setOnClickListener {

                    Toast.makeText(requireContext(), "Successfully Saved", Toast.LENGTH_SHORT)
                        .show()
                    Log.d("Kayıtlılar: ", "${radioAdapter.whichChecked.size}")


                    favoriteModel = receivedData?.user?.avatarUrl?.let { image ->
                        FavoriteModel(
                            null,
                            radioAdapter.whichChecked,
                            image,
                            receivedData!!.featuredGif!!.username,
                            receivedData!!.featuredGif!!.rating,
                            receivedData!!.featuredGif!!.type,
                            receivedData!!.featuredGif!!.sharedDateTime,
                            receivedData!!.featuredGif!!.title
                        )
                    }

                    val disposable = favoriteRepo.insertFavImage(favoriteModel!!)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()

                    compositeDisposable.add(disposable)

                    alertDialog?.dismiss()
                }

            }

            shareButtonCard.setOnClickListener {

                val sharedIntent = Intent(Intent.ACTION_SEND)
                sharedIntent.type = "text/plain"
                sharedIntent.putExtra(Intent.EXTRA_TEXT, receivedData!!.user!!.avatarUrl)

                startActivity(Intent.createChooser(sharedIntent, "Share an image/gif"))

            }

            saveButtonCard.setOnClickListener {

                requestPermissionIfHasnt()

            }

        }
    }

    private fun hasStoragePermission(): Boolean {

        return ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissionIfHasnt() {


        if (!hasStoragePermission()) {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        } else {

            downloadImage(receivedData!!.user!!.avatarUrl)

        }


    }

    private fun downloadImage(imageUrl: String?) {

        if (imageUrl.isNullOrEmpty()) {
            Log.e("DownloadImage", "Image URL is null or empty")
            return
        }

        val target = object : CustomTarget<File>() {
            override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                saveUrlToExternalStorage(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                Log.e("DownloadImage", "Failed to download image")
            }
        }

        glide.downloadOnly().load(imageUrl).into(target)

    }

    private fun saveUrlToExternalStorage(gifFile: File) {

        val filename = "lunar_gaze_download"
        val externalStoragePublicDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        val file = File(externalStoragePublicDirectory, filename)

        try {
            gifFile.copyTo(file)
            Toast.makeText(requireContext(), "Saved successfully", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Failed to save", Toast.LENGTH_SHORT).show()
        }

    }

    @SuppressLint("InflateParams")
    private fun showBottomSheet() {

        val sheetView = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)

        val updateTime = sheetView.findViewById<MaterialTextView>(R.id.updateTime)
        val description = sheetView.findViewById<MaterialTextView>(R.id.descriptionText)
        val close = sheetView.findViewById<LinearLayout>(R.id.SheetClose)
        val uploader = sheetView.findViewById<MaterialTextView>(R.id.uploader)
        val type = sheetView.findViewById<MaterialTextView>(R.id.imageType)
        val rating = sheetView.findViewById<MaterialTextView>(R.id.imageRating)
        val link = sheetView.findViewById<MaterialTextView>(R.id.imageLinkText)



        receivedData.apply {

            updateTime.text = this!!.featuredGif?.sharedDateTime ?: "Empty"
            type.text = this.type ?: "Empty"
            uploader.text = this.featuredGif?.username ?: "Empty"
            rating.text = this.featuredGif?.rating ?: "Empty"
            description.text = this.featuredGif?.title ?: "Empty"
            link.text = this.featuredGif?.embedUrl ?: "Empty"

            link.setOnClickListener {

                val clipboardManager =
                    requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

                val clip = ClipData.newPlainText("URL", this.featuredGif?.embedUrl)
                clipboardManager.setPrimaryClip(clip)

                Toast.makeText(requireContext(), "URL Copied", Toast.LENGTH_SHORT).show()

            }

            close.setOnClickListener {
                bottomSheetDialog.dismiss()
            }


        }




        bottomSheetDialog.setContentView(sheetView)
        bottomSheetDialog.show()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        receivedData = null
        _binding = null
        alertDialog = null
        favoriteModel = null

        compositeDisposable.clear()

    }
}