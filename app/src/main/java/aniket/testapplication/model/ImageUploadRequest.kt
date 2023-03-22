package aniket.testapplication.model

data class ImageUploadRequest(
    val doneDate: String,
    val imageAttributes: String,
    val batchQRCode: String = "AAO",
    val reason: String = "NA",
    val failure: Boolean = false
)
