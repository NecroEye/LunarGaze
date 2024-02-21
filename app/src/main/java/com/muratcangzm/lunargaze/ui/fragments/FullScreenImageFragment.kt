package com.muratcangzm.lunargaze.ui.fragments

import android.content.Intent
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
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.muratcangzm.lunargaze.databinding.ImageFullscreenLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
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
    private var receivedData: String? = null


    init {

        //Empty Constructor

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ImageFullscreenLayoutBinding.inflate(inflater, container, false)

        receivedData = requireArguments().getString("imageData")
        Log.d("FullScreenData: ", " $receivedData")


        setUIComponent()

        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setUIComponent() {

        binding.apply {

            glide
                .load(receivedData)
                .into(fullScreenImage)

            backButton.setOnClickListener {
                findNavController().navigateUp()
            }

            bookmarkedButtonCard.setOnClickListener {

                Toast.makeText(requireContext(), "BookmarkButton", Toast.LENGTH_SHORT).show()

            }

            shareButtonCard.setOnClickListener {

                val sharedIntent = Intent(Intent.ACTION_SEND)
                sharedIntent.type = "text/plain"
                sharedIntent.putExtra(Intent.EXTRA_TEXT, receivedData)

                startActivity(Intent.createChooser(sharedIntent, "Share an image/gif"))

            }

            saveButtonCard.setOnClickListener {

                Toast.makeText(requireContext(), "SavedButton", Toast.LENGTH_SHORT).show()
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
            Log.d("izin", "girdi")
        } else {
            Log.d("izin", "izin verildi")

            downloadImage(receivedData)

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


    override fun onDestroyView() {
        super.onDestroyView()
        receivedData = null
        _binding = null
    }
}