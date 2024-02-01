package com.iambulanva.camera.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.iambulanva.camera.MainViewModel
import com.iambulanva.camera.base.BaseFragment
import com.iambulanva.camera.R
import com.iambulanva.camera.databinding.FragmentCameraBinding
import com.iambulanva.camera.extension.*
import java.io.File

class CameraFragment: BaseFragment(R.layout.fragment_camera) {

    private val viewModel: MainViewModel by activityViewModels<MainViewModel>()

    override val fragment: BaseFragment
        get() = this
    private val binding by viewBinding<FragmentCameraBinding>()
    private var imageCapture: ImageCapture? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startCamera()
        with(binding){
            ivCamera.setOnClickListener {
                takePhoto()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder()
                .build()
            cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture
            )
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        // Налаштування параметрів зйомки
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            File(requireContext().filesDir, "${System.currentTimeMillis()}" + ".jpg")
        ).build()

        // Зйомка фото та обробка результату
        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    // Фото успішно збережено, отримати шлях та оновити галерею
                    val path = outputFileResults.savedUri?.path
                    if (path !=null){
                        viewModel.setList(path)
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    // Обробка помилок
                    Toast.makeText(requireContext(), exception.toString(), Toast.LENGTH_SHORT).show()
                }
            })
    }
}