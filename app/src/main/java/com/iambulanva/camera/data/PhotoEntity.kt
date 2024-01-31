package com.iambulanva.camera.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bitmap: ByteArray
)