package com.iambulanva.camera

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.iambulanva.camera.databinding.ActivityMainBinding
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
        setOnClickCallback()
    }

    private fun setOnClickCallback(){
        with(binding){
            ivCamera.setOnClickListener {
                if (getCameraPermission()){
                    startCamera()
                    ivCamera.setOnClickListener { takePhoto() }
                } else {
                    Camera(this@MainActivity).requestPermission()
                }
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

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

        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        // Налаштування параметрів зйомки
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            File(outputDirectory, "temp_photo.jpg")
        ).build()

        // Зйомка фото та обробка результату
        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {


                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    // Фото успішно збережено, отримати Bitmap
                    val savedUri = Uri.fromFile(File(outputDirectory, "temp_photo.jpg"))
                    val bitmap = ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(
                            contentResolver,
                            savedUri
                        )
                    )

                    // Тепер ви можете використовувати 'bitmap' як вам потрібно
                }

                override fun onError(exception: ImageCaptureException) {

                }
            })
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    private fun getCameraPermission():Boolean{
        return Camera(this).checkPermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}