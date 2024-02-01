package com.iambulanva.camera.extension

import android.os.*
import androidx.lifecycle.*


fun <T> LiveData<T>.debounce(duration: Long = 300L): LiveData<T> = MediatorLiveData<T>().also {
    val source = this
    val handler = Handler(Looper.getMainLooper())

    val runnable = Runnable {
        it.value = source.value
    }

    it.addSource(source) {
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, duration)
    }
}

fun <T> LiveData<T>.nonNullObserve(owner: LifecycleOwner, observer: (t: T) -> Unit) {
    this.observe(owner, Observer{
        it?.let(observer)
    })
}

fun <T> MutableLiveData<T>.nonNullObserve(owner: LifecycleOwner, observer: (t: T) -> Unit) {
    this.observe(owner, Observer{
        it?.let(observer)
    })
}
