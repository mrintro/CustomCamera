package aniket.testapplication.model

import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.time.LocalDate

fun File.toMultipart(): MultipartBody.Part = MultipartBody.Part.createFormData(
    "file",
    this.name,
    RequestBody.create(MediaType.parse("image/jpg"), this)
)

fun File.createImageRequestObject() = ImageUploadRequest(
    doneDate = LocalDate.now().toString(),
    imageAttributes = this.name
)

fun ImageUploadRequest.toRequestBody(converter: Gson) = kotlin.run {

}
