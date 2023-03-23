package aniket.testapplication.utils

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