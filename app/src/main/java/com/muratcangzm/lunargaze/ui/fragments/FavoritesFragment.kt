package com.muratcangzm.lunargaze.ui.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.muratcangzm.lunargaze.R
import com.muratcangzm.lunargaze.databinding.FavoritesFragmentLayoutBinding
import com.muratcangzm.lunargaze.ui.adapters.FavoriteFileAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class FavoritesFragment : Fragment() {


    private var _binding: FavoritesFragmentLayoutBinding? = null
    private val binding
        get() = _binding!!
    private var overlayView: View? = null
    private var alertDialog: AlertDialog? = null
    private lateinit var savedFiles: Map<String, *>
    private lateinit var stringList: List<String>

    @Inject
    lateinit var favoriteFileAdapter: FavoriteFileAdapter

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    init {

        //Empty Constructor

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FavoritesFragmentLayoutBinding.inflate(inflater, container, false)

        binding.fileRecycler.adapter = favoriteFileAdapter
        binding.fileRecycler.layoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)

        savedFiles = sharedPreferences.all
        stringList = savedFiles.values.filterIsInstance<String>()

        if(stringList.isEmpty())
            binding.emptyFavFileText.visibility = View.VISIBLE
        else
            binding.emptyFavFileText.visibility = View.INVISIBLE

        favoriteFileAdapter.submitFileNames(stringList)
        binding.fileRecycler.hasFixedSize()

        binding.favButton.setOnClickListener {
            showPopUp()
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("InflateParams")
    private fun showPopUp() {

        val inflater = LayoutInflater.from(requireContext())
        val popupView = inflater.inflate(R.layout.pop_up_screen_layout, null)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(popupView)
        alertDialog = builder.create()

        overlayView = View(requireContext())
        overlayView?.apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.parseColor("#80000000")) // semi-transparent black
            isClickable = true
        }

        val rootView = (requireActivity().window.decorView as ViewGroup).getChildAt(0)
        (rootView as ViewGroup).addView(overlayView)

        alertDialog?.setOnDismissListener {
            (rootView as ViewGroup).removeView(overlayView)
        }

        alertDialog?.show()

        val input = popupView.findViewById<TextInputEditText>(R.id.favoriteName)
        val saveButton = popupView.findViewById<MaterialButton>(R.id.saveButton)


        saveButton.setOnClickListener {

            if (input.text.toString().isNotEmpty())
                with(sharedPreferences.edit()) {
                    putString(input.text.toString(), input.text.toString())
                        .apply()

                    val updatedList = stringList.toMutableList().apply { add(input.text.toString()) }
                    favoriteFileAdapter.submitFileNames(updatedList)
                }



            alertDialog?.dismiss()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null

    }

}