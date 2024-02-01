package com.iambulanva.camera

import androidx.lifecycle.*
import kotlinx.coroutines.flow.*

class MainViewModel:ViewModel() {

    private val _listOfPhotos: MutableLiveData<ArrayList<String>> = MutableLiveData()
    val listOfPhotos get() = _listOfPhotos as LiveData<ArrayList<String>>

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    fun setList(path:String){
        if (_listOfPhotos.value == null){
            _listOfPhotos.value = arrayListOf(path)
        } else {
            _listOfPhotos.value!!.add(path)
        }
    }

    fun setConnectivityStatus(isConnected: Boolean) {
        _isConnected.value = isConnected
    }
}