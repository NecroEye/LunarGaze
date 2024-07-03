package com.muratcangzm.lunargaze.ui.common.navigation

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.fragment.FragmentNavigator

data class NavigationData(
    @IdRes val destinationInt: Int,
    @IdRes val popupTo:Int? = null,
    val popupToInclusive:Boolean = false,
    val args: Bundle? = null,
    val navigationAnimator: NavigationAnimator? = null,
    val fragmentNavigationExtras:FragmentNavigator.Extras? = null
)
