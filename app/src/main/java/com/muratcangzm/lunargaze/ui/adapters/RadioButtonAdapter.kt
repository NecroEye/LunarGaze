package com.muratcangzm.lunargaze.ui.adapters

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muratcangzm.lunargaze.databinding.RadiobuttonAdapterLayoutBinding
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.jvm.Throws

class RadioButtonAdapter
@Inject
constructor(
    @ApplicationContext private val context: Context,
    private val sharedPreferences: SharedPreferences
) :
    RecyclerView.Adapter<RadioButtonAdapter.RadioButtonHolder>() {

    private lateinit var binding: RadiobuttonAdapterLayoutBinding
    private val savedLists: Map<String, *> = sharedPreferences.all
    private var allFileNames: List<String>? = null
    var whichChecked = mutableListOf<String>()

    init {
        allFileNames = savedLists.values.filterIsInstance<String>()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioButtonHolder {

        binding =
            RadiobuttonAdapterLayoutBinding.inflate(LayoutInflater.from(context), parent, false)

        return RadioButtonHolder()
    }

    @Throws(ArrayIndexOutOfBoundsException::class)
    override fun getItemCount(): Int =
        allFileNames?.size ?: 0

    override fun onBindViewHolder(holder: RadioButtonHolder, position: Int) {

        holder.setData(allFileNames!![position])

    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }


    inner class RadioButtonHolder() : RecyclerView.ViewHolder(binding.root) {


        fun setData(fileName: String) {

            binding.apply {
                saveRadioButton.text = fileName ?: "boş"

                saveRadioButton.setOnCheckedChangeListener { _, isChecked ->

                    if (isChecked)
                        whichChecked.add(fileName)


                }

            }

        }


    }


    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

        allFileNames = null
    }

}