package com.muratcangzm.lunargaze.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ClipData
import android.content.Intent
import android.content.ClipboardManager
import android.content.ContentResolver
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
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.Settings
import android.view.ScaleGestureDetector
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.navArgs
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import com.muratcangzm.lunargaze.R
import com.muratcangzm.lunargaze.databinding.ImageFullscreenLayoutBinding
import com.muratcangzm.lunargaze.extensions.goneView
import com.muratcangzm.lunargaze.extensions.hideView
import com.muratcangzm.lunargaze.extensions.showSnackBarWithAction
import com.muratcangzm.lunargaze.extensions.showView
import com.muratcangzm.lunargaze.extensions.tost
import com.muratcangzm.lunargaze.models.local.FavoriteModel
import com.muratcangzm.lunargaze.models.remote.ChannelModel
import com.muratcangzm.lunargaze.repository.DataStoreRepo
import com.muratcangzm.lunargaze.repository.FavoriteRepo
import com.muratcangzm.lunargaze.ui.adapters.RadioButtonAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
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
    private var roomData: FavoriteModel? = null
    private var alertDialog: AlertDialog? = null
    private var favoriteModel: FavoriteModel? = null
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var scaleFactor = 1.0f

    private lateinit var activityResultLauncher: ActivityResultLauncher<String>

    private var compositeDisposable = CompositeDisposable()
    private val args: FullScreenImageFragmentArgs by navArgs()


    companion object {
        private const val REQUEST_CODE = 1
    }

    init {

        //Empty Constructor

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ImageFullscreenLayoutBinding.inflate(inflater, container, false)

        receivedData = args.imageData
        roomData = args.roomModelData

        scaleGestureDetector =
            ScaleGestureDetector(requireContext(), ScaleListener(binding.fullScreenImage))


        setUIComponent()

        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("InflateParams", "ClickableViewAccessibility")
    private fun setUIComponent() {

        binding.apply {

            if (roomData == null) {
                glide
                    .load(receivedData!!.user!!.avatarUrl)
                    .into(fullScreenImage)

                bookmarkedButtonCard.showView()

            } else {
                glide
                    .load(roomData!!.imageUrl)
                    .into(fullScreenImage)

                bookmarkedButtonCard.goneView()

            }



            fullScreenImage.setOnTouchListener { _, event ->

                scaleGestureDetector?.onTouchEvent(event)
                true
            }

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

                      tost(R.string.successful_save)

                    favoriteModel = receivedData?.user?.avatarUrl?.let { image ->
                        FavoriteModel(
                            null,
                            radioAdapter.whichChecked,
                            image,
                            receivedData!!.featuredGif?.username,
                            receivedData!!.featuredGif?.rating,
                            receivedData!!.featuredGif?.type,
                            receivedData!!.featuredGif?.sharedDateTime,
                            receivedData!!.featuredGif?.title
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

                if (roomData == null)
                    sharedIntent.putExtra(Intent.EXTRA_TEXT, receivedData!!.user!!.avatarUrl)
                else
                    sharedIntent.putExtra(Intent.EXTRA_TEXT, roomData?.imageUrl)
                startActivity(Intent.createChooser(sharedIntent, "Share an image/gif"))

            }

            saveButtonCard.setOnClickListener {
                     //TODO: Fix this
                     receivedData?.user?.let {
                         val convertedUri = Uri.parse(receivedData!!.user!!.avatarUrl)
                         requestPermissionIfHasnt(convertedUri, receivedData!!.displayName!!)

                     }
            }

        }
    }

    private fun hasStoragePermission(): Boolean {

        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissionIfHasnt(image: Uri, fileName: String) {

        if (!hasStoragePermission()) {

            requireActivity().requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE
            )
        } else if (hasStoragePermission()) {

            saveUrlToExternalStorage(image, fileName)

        } else {

            activityResultLauncher = registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->

                if (isGranted) {

                    saveUrlToExternalStorage(image, fileName)

                } else {

                     //Snackbar extension
                    showSnackBarWithAction(
                        R.string.permission_denied,
                        R.string.manage_permission
                    ) {
                        val intent = Intent()
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", requireActivity().packageName, null)
                        intent.setData(uri)
                        requireActivity().startActivity(intent)
                    }


                }
            }
        }
    }


    @SuppressLint("Recycle")
    private fun uriToBitmap(contentResolver: ContentResolver, uri: Uri): Bitmap? {

        var bitmap: Bitmap? = null

        try {

            val inputStream = contentResolver.openInputStream(uri)
            bitmap = BitmapFactory.decodeStream(inputStream)

            inputStream?.close()

        } catch (e: IOException) {
            Timber.tag("Error while saving").d(e)
        }

        return bitmap
    }


    private fun saveUrlToExternalStorage(imageUri: Uri, fileName: String) {


        val bitmapImage = uriToBitmap(requireActivity().contentResolver, imageUri)

        val directory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        val file = File(directory, fileName)
        var fos: FileOutputStream? = null

        try {

            fos = FileOutputStream(file)
            bitmapImage?.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()


        } catch (e: IOException) {
            Timber.tag("save error").d(e)
        } finally {
            fos?.close()
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


        if (roomData == null) {
            updateTime.text =
                receivedData!!.featuredGif?.sharedDateTime ?: resources.getString(R.string.empty)
            type.text = receivedData!!.type ?: resources.getString(R.string.empty)
            uploader.text =
                receivedData!!.featuredGif?.username ?: resources.getString(R.string.empty)
            rating.text = receivedData!!.featuredGif?.rating ?: resources.getString(R.string.empty)
            description.text =
                receivedData!!.featuredGif?.title ?: resources.getString(R.string.empty)
            link.text = receivedData!!.featuredGif?.embedUrl ?: resources.getString(R.string.empty)
        } else {

            updateTime.text = roomData!!.updateTime ?: resources.getString(R.string.empty)
            type.text = roomData!!.type ?: resources.getString(R.string.empty)
            uploader.text = roomData?.userName ?: resources.getString(R.string.empty)
            rating.text = roomData!!.rating ?: resources.getString(R.string.empty)
            description.text = roomData!!.description ?: resources.getString(R.string.empty)
            link.text = roomData!!.imageUrl ?: resources.getString(R.string.empty)
        }


        link.setOnClickListener {

            val clipboardManager =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val clip: ClipData?

            if (roomData == null) {
                clip = ClipData.newPlainText("URL", receivedData!!.featuredGif?.embedUrl)
                clipboardManager.setPrimaryClip(clip!!)

            } else {
                clip = ClipData.newPlainText("URL", roomData!!.imageUrl)
                clipboardManager.setPrimaryClip(clip!!)
            }

            tost(R.string.copy_url)

        }

        close.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(sheetView)
        bottomSheetDialog.show()

    }


    inner class ScaleListener(private val imageView: ImageView) :
        ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector): Boolean {

            scaleFactor *= detector.scaleFactor
            scaleFactor = scaleFactor.coerceIn(0.1f, 10.0f)

            imageView.scaleX = scaleFactor
            imageView.scaleY = scaleFactor

            return true
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        receivedData = null
        _binding = null
        alertDialog = null
        favoriteModel = null
        scaleGestureDetector = null

        compositeDisposable.clear()

    }
}