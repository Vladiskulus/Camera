package com.iambulanva.camera.data

import android.content.Context
import androidx.room.*

@Database(entities = [PhotoEntity::class], version = 1, exportSchema = false)
abstract class PhotoDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: PhotoDatabase? = null
        fun getInstance(context: Context): PhotoDatabase = INSTANCE
            ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                PhotoDatabase::class.java,
                "PhotoDatabase"
            ).fallbackToDestructiveMigration().build()
            INSTANCE = instance
            instance
        }
    }


    abstract fun getPhotoDAO(): PhotoDAO
}