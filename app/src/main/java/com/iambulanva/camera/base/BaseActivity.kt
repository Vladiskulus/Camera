package com.iambulanva.camera.base

import android.content.*
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.iambulanva.camera.R
import com.iambulanva.camera.interfaces.ActivityListener
import com.iambulanva.camera.interfaces.OnBackPressedListener
import com.iambulanva.camera.interfaces.UIControlInterface
import com.iambulanva.camera.interfaces.ViewModelInterface


open class BaseActivity: FragmentActivity(), ViewModelInterface, UIControlInterface,
    ActivityListener, OnBackPressedListener {

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


    internal fun switchToFragment(fragment: Fragment, container: Int = R.id.fragment_container) {
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