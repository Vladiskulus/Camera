package com.iambulanva.camera

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Camera(private val activity: Activity) {

    fun checkPermission():Boolean{
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(){
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.CAMERA),
            1
        )
    }

//    fun getBitmap(): Bitmap {
//
//    }
//
//    fun convertBitmapToString(bitmap: Bitmap):String{
//
//    }
}