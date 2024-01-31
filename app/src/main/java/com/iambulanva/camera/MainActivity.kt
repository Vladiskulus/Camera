package com.iambulanva.camera


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

    // Останній шлях до зображення, щоб пізніше вивантажити його в галерею
    private var lastSavedImagePath: String? = null

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
            rv.layoutManager = GridLayoutManager(this@MainActivity, 2)
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
                val photoPaths = getPhotoPaths()

                // Оновлення адаптера зі списком шляхів до фотографій
                val photoAdapter = GalleryAdapter(photoPaths)
                rv.adapter = photoAdapter
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

    private suspend fun updateGalleryAdapter() {
        lastSavedImagePath?.let { path ->
            // Оновлення адаптера з новим шляхом до фотографії
            val photoAdapter = binding.rv.adapter as? GalleryAdapter
            photoAdapter?.addPhoto(path)
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return mediaDir ?: filesDir
    }

    private fun getCameraPermission(): Boolean {
        return Camera(this).checkPermission()
    }

    private suspend fun getPhotoPaths(): List<String> = withContext(Dispatchers.IO) {
        val appSpecificDir = File(filesDir, "photos")
        if (!appSpecificDir.exists()) {
            appSpecificDir.mkdirs()
        }

        val photoFiles = appSpecificDir.listFiles() ?: arrayOf()
        return@withContext photoFiles.mapNotNull { it.absolutePath }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}