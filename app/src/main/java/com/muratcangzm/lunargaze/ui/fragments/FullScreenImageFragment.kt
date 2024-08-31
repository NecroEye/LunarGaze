package com.muratcangzm.lunargaze.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.ClipData
import android.content.Intent
import android.content.ClipboardManager
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.content.Context
import android.view.ScaleGestureDetector
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.core.net.toUri
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.muratcangzm.lunargaze.R
import com.muratcangzm.lunargaze.databinding.ImageFullscreenLayoutBinding
import com.muratcangzm.lunargaze.extensions.goneView
import com.muratcangzm.lunargaze.extensions.setSafeOnClickListener
import com.muratcangzm.lunargaze.extensions.showView
import com.muratcangzm.lunargaze.extensions.tost
import com.muratcangzm.lunargaze.helper.Downloader
import com.muratcangzm.lunargaze.models.local.FavoriteModel
import com.muratcangzm.lunargaze.models.remote.giphy.ChannelModel
import com.muratcangzm.lunargaze.repository.local.FavoriteRepo
import com.muratcangzm.lunargaze.ui.adapters.RadioButtonAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class FullScreenImageFragment : Fragment(), Downloader {

    private var _binding: ImageFullscreenLayoutBinding? = null
    private val binding by lazy(LazyThreadSafetyMode.NONE) { _binding!! }

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

    private val downloadManager by lazy { requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager }


    companion object {
        private const val REQUEST_CODE = 1
        private const val TAG = "FullScreenFragment"
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

        with(binding) {

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

            backButton.setSafeOnClickListener {
                findNavController().navigateUp()
            }

            bottomSheetPopUp.setSafeOnClickListener {
                showBottomSheet()

            }



            bookmarkedButtonCard.setSafeOnClickListener {

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

                saveButton.setSafeOnClickListener {

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

            shareButtonCard.setSafeOnClickListener {

                val sharedIntent = Intent(Intent.ACTION_SEND)
                sharedIntent.type = "text/plain"

                if (roomData == null)
                    sharedIntent.putExtra(Intent.EXTRA_TEXT, receivedData!!.user!!.avatarUrl)
                else
                    sharedIntent.putExtra(Intent.EXTRA_TEXT, roomData?.imageUrl)
                startActivity(Intent.createChooser(sharedIntent, "Share an image/gif"))

            }

            //TODO: Add an toast message when it started to download successfully
            saveButtonCard.setSafeOnClickListener {
                if (receivedData != null)
                    requestPermissionIfHasnt(channelModel = receivedData, roomModel = null)
                else
                    requestPermissionIfHasnt(roomModel = roomData, channelModel = null)


            }

        }
    }


    private fun requestPermissionIfHasnt(
        channelModel: ChannelModel.ChannelData?,
        roomModel: FavoriteModel?
    ) {

        if (hasStoragePermission()) {

            channelModel?.let {

                val type = it.user!!.avatarUrl!!.substring(it.user.avatarUrl!!.length - 3)
                Timber.tag(TAG).d("Channel Resmin tipi: $type")
                downloadFile(it.user.avatarUrl, "image/".plus(type), it.featuredGif?.username!!)
            }
            roomModel?.let {

                val type = it.imageUrl.substring(it.imageUrl.length - 3)
                Timber.tag(TAG).d("Room Resmin tipi: $type")
                downloadFile(it.imageUrl, "image/".plus(type), it.userName!!)
            }

        } else {

            requireActivity().requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                REQUEST_CODE
            )
        }
    }

    private fun hasStoragePermission(): Boolean {

        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun downloadFile(url: String, imageType: String, imageName: String): Long {


        val request = DownloadManager.Request(url.toUri())
            .setMimeType("image/jpg")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(imageName.plus(imageType))
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                imageName.plus(imageType)
            )

        return downloadManager.enqueue(request)
    }


    @SuppressLint("InflateParams")
    private fun showBottomSheet() {

        val sheetView = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)

        val parentView = sheetView.parent as? View

        parentView?.let {
            val behavior = BottomSheetBehavior.from(it)

            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(p0: View, p1: Int) {
                    if (p1 == BottomSheetBehavior.STATE_HIDDEN) {
                        sheetView.startAnimation(
                            android.view.animation.AnimationUtils.loadAnimation(
                                requireContext(),
                                R.anim.slide_down_anim
                            )
                        )

                        sheetView.postDelayed({
                            bottomSheetDialog.dismiss()
                        }, 300)
                    }
                }

                override fun onSlide(p0: View, p1: Float) {
                    TODO("Not yet implemented")
                }

            })
        }


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


        link.setSafeOnClickListener {

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

        close.setSafeOnClickListener {

            sheetView.startAnimation(
                android.view.animation.AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.slide_down_anim
                )
            )

            sheetView.postDelayed({
                bottomSheetDialog.dismiss()
            }, 300)

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

        compositeDisposable.dispose()

    }


}