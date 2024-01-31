package com.vn.iambulance.prototype_20.ui.base

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.red
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.vn.iambulance.localefeature.DefaultLocaleHelper
import com.vn.iambulance.prototype_20.R
import com.vn.iambulance.prototype_20.interfaces.ActivityListener
import com.vn.iambulance.prototype_20.interfaces.OnBackPressedListener
import com.vn.iambulance.prototype_20.interfaces.UIControllInterface
import com.vn.iambulance.prototype_20.interfaces.ViewModelInterface
import com.vn.iambulance.prototype_20.ui.mainScreen.MainViewModel


open class BaseActivity: FragmentActivity(), ViewModelInterface, UIControllInterface, ActivityListener, OnBackPressedListener {

    protected val mainViewModel by viewModels<MainViewModel>()

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(DefaultLocaleHelper.getInstance(newBase!!).onAttach())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val w = window
//        w.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
        showSystemUI()

        //enableImmersiveMode()
    }

    override fun onStart() {
        super.onStart()
        setDefaultValues()
        setupViewModelCallbacks()
        showUI()
        onClickCallback()
    }


    internal fun switchToFragment(fragment: Fragment, container: Int = R.id.fcv_container) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(container, fragment)
        transaction.commit()
    }

    @Suppress("DEPRECATION")
    private fun enableImmersiveMode() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun showSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.show(WindowInsetsCompat.Type.navigationBars())
            controller.show(WindowInsetsCompat.Type.statusBars())
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    override fun showUI() {
    }

    override fun onClickCallback() {
    }

    override fun setupViewModelCallbacks() {
    }

    override fun setDefaultValues() {
    }

    override fun recreateActivity() {
        try {
            startActivity(Intent(this, this::class.java))
        } catch (e : Exception){
            e.printStackTrace()
            recreate()
        }
    }
}