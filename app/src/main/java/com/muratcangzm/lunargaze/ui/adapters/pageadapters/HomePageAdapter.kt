package com.muratcangzm.lunargaze.ui.adapters.pageadapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomePageAdapter(
    fragmentActivity: FragmentActivity,
    private val fragmentList: List<Fragment>
) : FragmentStateAdapter(fragmentActivity) {

    @Throws(ArrayIndexOutOfBoundsException::class)
    override fun getItemCount(): Int = fragmentList.size ?: 0

    @Throws(ArrayIndexOutOfBoundsException::class)
    override fun createFragment(position: Int): Fragment = fragmentList[position]
}