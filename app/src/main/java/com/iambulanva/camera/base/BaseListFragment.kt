package com.vn.iambulance.prototype_20.ui.base

import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import com.vn.iambulance.prototype_20.R
import com.vn.iambulance.prototype_20.databinding.FragmentScreenForListBinding
import com.vn.iambulance.prototype_20.extensions.gone
import com.vn.iambulance.prototype_20.extensions.invisible
import com.vn.iambulance.prototype_20.extensions.viewBinding
import com.vn.iambulance.prototype_20.extensions.visible
import com.vn.iambulance.prototype_20.ui.CategoryRepository
import com.vn.iambulance.prototype_20.ui.CategoryType

open class BaseListFragment: BaseFragment(R.layout.fragment_screen_for_list) {

    override val fragment: BaseFragment
        get() = this

    private val binding by viewBinding<FragmentScreenForListBinding>()

    override fun showUI() {
        checkSubscription()

    }

    private fun checkSubscription(){
        val subscription = true
        with(binding){
            if (!subscription){
                vBgSearch.gone()
                editTextSearch.gone()
                vSearch.gone()
                vSort.gone()
            } else {
                vBgSearch.visible()
                editTextSearch.visible()
                vSearch.visible()
                vSort.visible()
                vSort.setOnClickListener { animateSortCard(isOpen = true,  vCardSort, vSortCross, rvSort, tvSortClear, tvSortShow) }
                vSortCross.setOnClickListener { animateSortCard(isOpen = false, vCardSort, vSortCross, rvSort, tvSortClear, tvSortShow) }
            }
        }
    }

    internal fun setHeader(category: CategoryType){
        val data = CategoryRepository().getCategory(category)
        with(binding){
            if (data != null){
                vTopBar.setBackgroundResource(data.gradient)
                tvHeader.setText(data.name)
            }
        }
    }

    private fun animateSortCard(isOpen: Boolean = false, vararg views: View){
        val rightToLeft = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 1.0f,
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f
        ).apply {
            duration = 1000
            fillAfter = true
        }
        val leftToRight = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 1.0f,
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f
        ).apply {
            duration = 1000
            fillAfter = true
        }
        if (isOpen){
            rightToLeft.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationStart(p0: Animation?) {
                    views.forEach { it.visible() }
                }

                override fun onAnimationEnd(p0: Animation?) {}
                override fun onAnimationRepeat(p0: Animation?) {}
            })
            views.forEach { it.startAnimation(rightToLeft) }
        } else {
            leftToRight.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationStart(p0: Animation?) {
                    views.forEach { it.invisible() }

                }

                override fun onAnimationEnd(p0: Animation?) {}
                override fun onAnimationRepeat(p0: Animation?) {}
            })
            views.forEach { it.startAnimation(leftToRight) }
        }
    }
}