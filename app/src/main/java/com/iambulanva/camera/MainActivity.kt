package com.iambulanva.camera

import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.iambulanva.camera.data.*
import com.iambulanva.camera.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.concurrent.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var photoDao: PhotoDAO
    private lateinit var photoAdapter: GalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
        val database = PhotoDatabase.getInstance(this)
        photoDao = database.getPhotoDAO()
        setOnClickCallback()
    }

    private fun setOnClickCallback(){
        with(binding){
            photoAdapter = GalleryAdapter()
            rv.layoutManager = GridLayoutManager(this@MainActivity, 2)
            rv.adapter = photoAdapter
            ivCamera.setOnClickListener {
                if (getCameraPermission()){
                    startCamera()
                    viewFinder.visibility = View.VISIBLE
                    ivCamera.setOnClickListener { takePhoto() }
                } else {
                    Camera(this@MainActivity).requestPermission()
                }
            }
            CoroutineScope(Dispatchers.Main).launch {
                val photos = photoDao.getAllPhotos()

                // Перевірка, чи фото не мають значення null
                val nonNullPhotos = photos.filter { it.bitmap != null }

                // Використання тільки ненульових фотографій
                photoAdapter.setPhotos(nonNullPhotos)
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
                    // Збереження фото у базу даних Room
                    val byteStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteStream)
                    val byteArray = byteStream.toByteArray()

                    // Використовуйте корутину або інший спосіб для виклику в основному потоці
                    CoroutineScope(Dispatchers.IO).launch {
                        val photoEntity = PhotoEntity(bitmap = byteArray)
                        photoDao.insertPhoto(photoEntity)
                    }
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