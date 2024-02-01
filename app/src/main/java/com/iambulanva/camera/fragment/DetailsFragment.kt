package com.iambulanva.camera.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import com.iambulanva.camera.R
import com.iambulanva.camera.base.BaseFragment
import com.iambulanva.camera.databinding.FragmentDetailBinding
import com.iambulanva.camera.extension.viewBinding

class DetailsFragment: BaseFragment(R.layout.fragment_detail) {
    override val fragment: BaseFragment
        get() = this

    private val binding: FragmentDetailBinding by viewBinding<FragmentDetailBinding>()

    fun newInstance(path:String): DetailsFragment = DetailsFragment().apply{
        arguments = Bundle().apply {
            putString(FRAGMENT_ARGUMENT, path)
        }
    }

    override fun showUI() {
        val path = arguments?.getString(FRAGMENT_ARGUMENT, "")
        if (!path.isNullOrEmpty()){
            with(binding){
                iv.setImageBitmap(BitmapFactory.decodeFile(path))
            }
        }
    }
}