package com.iambulanva.camera.data

import android.content.Context
import android.content.SharedPreferences
import com.iambulanva.camera.extension.*

class LocalDBManager(private val context: Context) {

    private var localDB:SharedPreferences = context.getSharedPreferences("Local DB", Context.MODE_PRIVATE)

    fun addToSet(string:String){
        val currentSet = localDB.get("PathSet", setOf<String>())
        if (currentSet == setOf<String>()){
            localDB.put("PathSet", setOf(string))
        } else {
            currentSet.plus(string)
            localDB.put("PathSet", currentSet)
        }

    }
}