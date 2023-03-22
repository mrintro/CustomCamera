package aniket.testapplication.model

import android.net.Uri


data class URIMeta(
    val uri: Uri,
    val fileName: String?,
    val mimeType: String?,
    val orientation: Int = 0
) {
}
