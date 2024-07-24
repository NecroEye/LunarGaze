package com.muratcangzm.lunargaze.ui.fragments.core

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavOptions
import androidx.viewbinding.ViewBinding
import com.muratcangzm.lunargaze.R
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}