package com.muratcangzm.lunargaze.ui.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
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
import com.google.android.material.button.MaterialButton
import com.muratcangzm.lunargaze.R
import com.muratcangzm.lunargaze.databinding.ImageFullscreenLayoutBinding
import com.muratcangzm.lunargaze.ui.adapters.RadioButtonAdapter
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
    @Inject
    lateinit var radioAdapter: RadioButtonAdapter

    private var receivedData: String? = null
    private var alertDialog: AlertDialog? = null


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

    @SuppressLint("InflateParams")
    private fun setUIComponent() {

        binding.apply {

            glide
                .load(receivedData)
                .into(fullScreenImage)

            backButton.setOnClickListener {
                findNavController().navigateUp()
            }

            bookmarkedButtonCard.setOnClickListener {

                val inflater = LayoutInflater.from(requireContext())
                val popupSave = inflater.inflate(R.layout.saved_popup_layout, null)

                val radioRecycler = popupSave.findViewById<RecyclerView>(R.id.radioButtonRecycler)
                val saveButton = popupSave.findViewById<MaterialButton>(R.id.popupSaveButton)

                radioRecycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL, false)
                radioRecycler.adapter = radioAdapter
                radioRecycler.hasFixedSize()




                val builder = AlertDialog.Builder(requireContext())
                builder.setView(popupSave)
                alertDialog = builder.create()

                alertDialog?.show()

                saveButton.setOnClickListener{

                    Toast.makeText(requireContext(),"pressed", Toast.LENGTH_SHORT).show()
                    alertDialog?.dismiss()
                }

            }

            shareButtonCard.setOnClickListener {

                val sharedIntent = Intent(Intent.ACTION_SEND)
                sharedIntent.type = "text/plain"
                sharedIntent.putExtra(Intent.EXTRA_TEXT, receivedData)

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
        alertDialog = null
    }
}