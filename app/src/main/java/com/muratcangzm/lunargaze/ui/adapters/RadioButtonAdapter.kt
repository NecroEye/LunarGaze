package com.muratcangzm.lunargaze.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muratcangzm.lunargaze.databinding.RadiobuttonAdapterLayoutBinding
import com.muratcangzm.lunargaze.repository.local.DataStoreRepo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.jvm.Throws

class RadioButtonAdapter
@Inject
constructor(
    @ApplicationContext private val context: Context,
    private val dataStoreRepo: DataStoreRepo
) :
    RecyclerView.Adapter<RadioButtonAdapter.RadioButtonHolder>() {

    private lateinit var binding: RadiobuttonAdapterLayoutBinding
    private var allFileNames: List<String>? = null
    var whichChecked = mutableListOf<String>()

    init {

        loadData()

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

    private fun loadData() {

        CoroutineScope(Dispatchers.IO).launch {

            val values = dataStoreRepo.getAllValues()
            allFileNames = values?.mapNotNull { it as? String }

        }
    }


    inner class RadioButtonHolder() : RecyclerView.ViewHolder(binding.root) {


        fun setData(fileName: String) {

            with(binding) {
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