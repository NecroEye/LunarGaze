package com.muratcangzm.lunargaze.ui.fragments.core

import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.viewbinding.ViewBinding
import com.muratcangzm.lunargaze.R
import com.muratcangzm.lunargaze.common.utils.tryOrLog
import com.muratcangzm.lunargaze.ui.common.navigation.NavigationData
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _binding: VB? = null

    protected val binding: VB
        get() = _binding ?: error("Activity view not initialized")

    @get:LayoutRes
    protected abstract val getLayoutResId: Int

    open val compositeDisposable by lazy(LazyThreadSafetyMode.NONE) {
        CompositeDisposable()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        _binding = inflateBinding(layoutInflater)
        setContentView(binding.root)

    }

    protected abstract fun inflateBinding(layoutInflater: LayoutInflater): VB

    open fun buildNavOptions(
        @AnimRes @AnimatorRes enterAnim: Int? = null,
        @AnimRes @AnimatorRes exitAnim: Int? = null,
        @AnimRes @AnimatorRes popEnterAnim: Int? = null,
        @AnimRes @AnimatorRes popExitAnim: Int? = null,
        @IdRes popupTo: Int? = null,
        popupToInclusive: Boolean? = false
    ): NavOptions {

        val builder = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(enterAnim ?: androidx.navigation.ui.R.anim.nav_default_enter_anim)
            .setExitAnim(exitAnim ?: androidx.navigation.ui.R.anim.nav_default_exit_anim)
            .setPopEnterAnim(
                popEnterAnim ?: androidx.navigation.ui.R.anim.nav_default_pop_enter_anim
            )
            .setPopExitAnim(popExitAnim ?: androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)

        popupTo?.let {
            if (popupToInclusive != null) {
                builder.setPopUpTo(it, popupToInclusive, false)
            }
        }
        return builder.build()
    }

    open fun navigationToDestination(navController: NavController, navigationData: NavigationData) {
        tryOrLog {
            navController.navigate(
                navigationData.destinationInt,
                navigationData.args,
                buildNavOptions(
                    enterAnim = navigationData.navigationAnimator?.enterAnim
                        ?: R.anim.slide_in_left_anim,
                    exitAnim = navigationData.navigationAnimator?.exitAnim
                        ?: R.anim.slide_out_left_anim,
                    popEnterAnim = navigationData.navigationAnimator?.popEnterAnim
                        ?: R.anim.slide_in_right_anim,
                    popExitAnim = navigationData.navigationAnimator?.popExitAnim
                        ?: R.anim.slide_out_right_anim,
                    popupTo = navigationData.popupTo,
                    popupToInclusive = navigationData.popupToInclusive
                )
            )
        }
    }

    open fun setFullScreenFlag() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = WindowInsetsControllerCompat(window, window.decorView)
            controller.hide(WindowInsetsCompat.Type.statusBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    open fun clearFullScreenFlag() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = WindowInsetsControllerCompat(window, window.decorView)
            controller.show(WindowInsetsCompat.Type.statusBars())
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}