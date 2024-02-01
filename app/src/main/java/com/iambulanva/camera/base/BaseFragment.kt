package com.iambulanva.camera.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.iambulanva.camera.R
import com.iambulanva.camera.interfaces.*

abstract class BaseFragment(layout: Int): Fragment(layout), ViewModelInterface, UIControlInterface {

    abstract val fragment: BaseFragment

    companion object{
        const val FRAGMENT_ARGUMENT = "fragment_argument"
    }
    internal val activity: BaseActivity get() = requireActivity() as BaseActivity
    internal val context: Context get() = requireContext()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDefaultValues()
        setupViewModelCallbacks()
        showUI()
        onClickCallback()
    }

    protected fun switchToFragment(fragment: Fragment, container: Int = R.id.fragment_container) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(container, fragment)
        transaction.commit()
    }

    override fun showUI() {}

    override fun onClickCallback() {}

    override fun setupViewModelCallbacks() {}

    override fun setDefaultValues() {}

}