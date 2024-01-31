package com.iambulanva.camera.fragment

import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import java.io.File

class CameraFragment: Fragment() {

    private var imageCapture: ImageCapture? = null

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Налаштування Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            // Налаштування ImageCapture
            imageCapture = ImageCapture.Builder()
                .build()

            // З'єднання камери та Preview
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
                    lastSavedImagePath = outputFileResults.savedUri?.path
                    CoroutineScope(Dispatchers.IO).launch {
                        // Не забудьте викликати функцію в основному потоці
                        withContext(Dispatchers.Main) {
                            updateGalleryAdapter()
                        }
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    // Обробка помилок
                }
            })
    }
}