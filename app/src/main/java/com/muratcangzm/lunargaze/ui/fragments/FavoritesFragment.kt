package com.muratcangzm.lunargaze.ui.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
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
import com.google.android.material.button.MaterialButton
import com.muratcangzm.lunargaze.R
import com.muratcangzm.lunargaze.databinding.FavoritesFragmentLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {


    private var _binding: FavoritesFragmentLayoutBinding? = null
    private val binding
        get() = _binding!!

    init {

        //Empty Constructor

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FavoritesFragmentLayoutBinding.inflate(inflater, container, false)

        binding.favButton.setOnClickListener {

            showPopUp()

        }

         return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("InflateParams")
    private fun showPopUp(){


      val inflater = LayoutInflater.from(requireContext())

        val popupView = inflater.inflate(R.layout.pop_up_screen_layout, null)

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )


        popupWindow.isOutsideTouchable = false
        popupWindow.showAtLocation(popupView, Gravity.CENTER,0 ,0)

        val saveButton = popupView.findViewById<MaterialButton>(R.id.saveButton)

        saveButton.setOnClickListener{
            popupWindow.dismiss()
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null

    }

}