package aniket.testapplication.utils

import android.hardware.Camera
import android.hardware.Camera.Parameters.FOCUS_MODE_FIXED
import android.renderscript.Sampler
import android.util.Log
import java.io.File
import java.io.FileOutputStream

fun File.postData(data: ByteArray?) = kotlin.runCatching {
    val fos = FileOutputStream(this)
    fos.write(data)
    fos.close()
    this@postData
}.onFailure {
    Log.e("ERROR", "Failed to write the image")
}.getOrNull()

@Suppress("Deprecation")
fun Camera.setISOAndFocusForCamera(isoValue: Int) {
    this.apply {
        val params = parameters
        params.set("iso", isoValue)
        params.focusMode = FOCUS_MODE_FIXED
        parameters = params
    }
}

@Suppress("Deprecation")
fun Camera.setEVValueForCamera(evValue: Int) {
    this.apply {
        val params = parameters
        if(evValue <= params.maxExposureCompensation && evValue >= params.minExposureCompensation) {
            params.exposureCompensation = evValue
        }
        parameters = params
    }
}