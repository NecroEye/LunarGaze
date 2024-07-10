package com.muratcangzm.lunargaze.ui.fragments.core

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavOptions
import com.muratcangzm.lunargaze.R
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseActivity<DB : ViewDataBinding> : AppCompatActivity() {


    open val compositeDisposable by lazy(LazyThreadSafetyMode.NONE){
        CompositeDisposable()
    }

    lateinit var binding: DB


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        binding = DataBindingUtil.setContentView(this, getLayoutResId())

    }

    @LayoutRes
    protected abstract fun getLayoutResId(): Int


    open fun buildNavOptions(
        @AnimRes @AnimatorRes enterAnim:Int? = null,
        @AnimRes @AnimatorRes exitAnim:Int? = null,
        @AnimRes @AnimatorRes popEnterAnim: Int? = null,
        @AnimRes @AnimatorRes popExitAnim:Int? = null,
        @IdRes popupTo: Int? = null,
        popupToInclusive:Boolean? = false
    ) : NavOptions{

        val builder = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(enterAnim ?: androidx.navigation.ui.R.anim.nav_default_enter_anim)
            .setExitAnim(exitAnim ?: androidx.navigation.ui.R.anim.nav_default_exit_anim)
            .setPopEnterAnim(popEnterAnim ?: androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(popExitAnim ?: androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)

        popupTo?.let {
            if (popupToInclusive != null) {
                builder.setPopUpTo(it, popupToInclusive, false)
            }
        }
        return builder.build()
    }

}