package aniket.testapplication.model

import aniket.testapplication.utils.UploadImageKey
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.time.LocalDate

fun File.toMultipart(): MultipartBody.Part = MultipartBody.Part.createFormData(
    UploadImageKey.IMAGE_ATTRIBUTES,
    this.name,
    RequestBody.create(MediaType.parse("image/jpg"), this)
)

fun getTextMultipartMap(): Map<String, RequestBody> {
    val map = HashMap<String, RequestBody>()
    val createdDate = getStringPartRequest(LocalDate.now().toString())
    val batchQRCode = getStringPartRequest("AAO")
    val reason = getStringPartRequest("NA")
    val failure = getStringPartRequest("false")
    return map.apply {
        put(UploadImageKey.CREATED_DATE, createdDate)
        put(UploadImageKey.BATCH_QR_CODE, batchQRCode)
        put(UploadImageKey.REASON, reason)
        put(UploadImageKey.FAILURE, failure)
    }
}

fun getStringPartRequest(string: String) =
    RequestBody.create(MediaType.parse("multipart/form-data"), string)
