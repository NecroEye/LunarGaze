package com.muratcangzm.lunargaze.ui.common.navigation

import androidx.annotation.IdRes

data class NavigationPopupData(
    @IdRes val destinationId:Int,
    val popupToInclusive:Boolean
)
