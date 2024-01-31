package com.iambulanva.camera.data

import androidx.room.*

@Dao
interface PhotoDAO {

    @Insert
    suspend fun insertPhoto(photo: PhotoEntity)

    @Query("SELECT * FROM photos")
    suspend fun getAllPhotos(): List<PhotoEntity>
}