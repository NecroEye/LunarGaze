package com.muratcangzm.lunargaze.ui.common.navigation

import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes

data class NavigationAnimator(
    @AnimRes @AnimatorRes val enterAnim: Int? = null,
    @AnimRes @AnimatorRes val exitAnim:Int? = null,
    @AnimRes @AnimatorRes val popEnterAnim:Int? = null,
    @AnimRes @AnimatorRes val popExitAnim:Int? = null,
)