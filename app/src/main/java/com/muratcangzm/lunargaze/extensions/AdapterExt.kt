package com.muratcangzm.lunargaze.extensions

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView


fun RecyclerView.scrollToTop() {
    smoothScrollToPosition(0)
}

fun RecyclerView.scrollToBottom() {
    smoothScrollToPosition(adapter?.itemCount ?: 0)
}
