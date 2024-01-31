package com.vn.iambulance.prototype_20.extensions

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

infix fun Context.loadAnim(anim: Int): Animation =
    AnimationUtils.loadAnimation(this, anim)

infix fun View.startAnim(anim: Animation) {
    this.startAnimation(anim)
}

fun View.updateVisibility(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.updateEnabled(enabled: Boolean?) {
    isEnabled = enabled == true
}

fun View.updateEnabledWithAlpha(enabled: Boolean?) {
    updateEnabled(enabled)
    updateAlpha(enabled)
}

fun View.updateAlpha(enabled: Boolean?) {
    alpha = if (enabled == true) 1f else 0.3f
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun RecyclerView.showFloatButtonOnScrollChanged(view: FloatingActionButton){
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 10 && view.isShown) {
                view.hide()
            }
            if (dy < -10 && !view.isShown) {
                view.show()
            }
            if (!recyclerView.canScrollVertically(-1)) {
                view.hide()
            }
        }
    })
}

fun Int.dp(): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics).toInt()
}

//fun vibrate(context: Context, durationMillis: Long) {
//    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        val vibrationEffect = VibrationEffect.createOneShot(durationMillis, VibrationEffect.DEFAULT_AMPLITUDE)
//        vibrator.vibrate(vibrationEffect)
//    } else {
//        vibrator.vibrate(durationMillis)
//    }
//}
//
//fun Context.onClick(){
//    onVibrate()
//    onSound()
//}
//
//fun Context.onVibrate(){
//    if (SharedPrefManager(this).vibrationStatus()){
//        (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
//    }
//}
//
//fun Context.onSound(){
//    if (SharedPrefManager(this).soundStatus()){
//        try {
//            val mediaPlayer = MediaPlayer()
//            val descriptor = assets.openFd("sounds/click.mp3")
//            mediaPlayer.apply {
//                setDataSource(descriptor.fileDescriptor, descriptor.startOffset, descriptor.length)
//                prepare()
//                start()
//                setOnCompletionListener {
//                    release()
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//}