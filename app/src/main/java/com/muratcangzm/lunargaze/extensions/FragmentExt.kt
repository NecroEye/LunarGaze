package com.muratcangzm.lunargaze.extensions

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.muratcangzm.lunargaze.R


fun Fragment.snackBarWithText(
    snackBarText: String?,
    snackBarLength: Int = Snackbar.LENGTH_SHORT
) {

    val message =
        if (!snackBarText.isNullOrEmpty()) snackBarText else requireContext().getString(R.string.empty)

    Snackbar.make(
        requireView(),
        message,
        snackBarLength
    )
        .show()
}


fun Fragment.snackBarWithText(@StringRes snackBarText: Int?) {

    val message = snackBarText?.let { requireContext().getString(it) }

    snackBarWithText(message)
}


fun Fragment.showSnackBarWithAction(
    @StringRes snackBarText: Int,
    @StringRes actionText: Int,
    action: () -> Unit
) {

    Snackbar.make(
        requireView(),
        snackBarText,
        Snackbar.LENGTH_INDEFINITE
    )
        .setAction(actionText) {
            action.invoke()
        }
        .show()
}


fun Fragment.tost(tostMessage: String?, toastLength: Int = Toast.LENGTH_SHORT) {

    val message =
        if (!tostMessage.isNullOrEmpty()) tostMessage else requireContext().getString(R.string.empty)

    Toast.makeText(requireContext(), message, toastLength).show()
}


fun Fragment.tost(@StringRes tostMessage: Int?) {

    val message = tostMessage?.let {
        requireContext().getString(it)
    }
    tost(tostMessage = message)

}

