package com.iambulanva.camera.fragment

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.iambulanva.camera.GalleryAdapter
import com.iambulanva.camera.MainViewModel
import com.iambulanva.camera.R
import com.iambulanva.camera.base.BaseFragment
import com.iambulanva.camera.data.PhotoListModel
import com.iambulanva.camera.databinding.FragmentRvBinding
import com.iambulanva.camera.extension.nonNullObserve
import com.iambulanva.camera.extension.parcelable
import com.iambulanva.camera.extension.viewBinding

class GalleryFragment: BaseFragment(R.layout.fragment_rv) {

    private val binding by viewBinding<FragmentRvBinding>()
    private val viewModel: MainViewModel by activityViewModels<MainViewModel>()
    override val fragment: BaseFragment
        get() = this

    fun newInstance(list: PhotoListModel): GalleryFragment  = GalleryFragment().apply {
        arguments = Bundle().apply {
            putParcelable(FRAGMENT_ARGUMENT, list)
        }
    }

    override fun showUI(){
        val item = arguments?.parcelable<PhotoListModel>(FRAGMENT_ARGUMENT)
        if (item != null){
            with(binding){
                rv.adapter = GalleryAdapter(item.list){
                    switchToFragment(DetailsFragment().newInstance(it))
                }
                rv.layoutManager = GridLayoutManager(requireContext(), 3)
            }
        }
    }

    override fun setupViewModelCallbacks() {
        viewModel.listOfPhotos.nonNullObserve(fragment){ it ->
            binding.rv.adapter = GalleryAdapter(it){path ->
                switchToFragment(DetailsFragment().newInstance(path))
            }
            binding.rv.layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }
}