package com.iambulanva.camera

import android.os.Bundle
import com.iambulanva.camera.base.BaseActivity
import com.iambulanva.camera.databinding.ActivityMainBinding
import com.iambulanva.camera.fragment.*

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnClickCallback()
    }

    private fun setOnClickCallback(){
        val camera = Camera(this)
        with(binding){
            ivCamera.setOnClickListener {
                if (camera.checkPermission()){
                    switchToFragment(CameraFragment())
                } else {
                    camera.requestPermission()
                }
            }
            ivList.setOnClickListener {
                switchToFragment(GalleryFragment())
            }
        }
    }
}