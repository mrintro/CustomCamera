package aniket.testapplication.ui.imageProcessor

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import aniket.testapplication.model.URIMeta


typealias URIMetaCallback = (meta: URIMeta?) -> Unit

class URIResolver {

    private val contentScheme = "content"

    fun resolve(context: Context, uri: Uri, uriMetaListener: URIMetaCallback) {
        when {
            checkValidContentScheme(uri) -> {
                uriMetaListener.invoke(getFileDataFromUri(context, uri))
            }
            else -> Unit
        }
    }

    private fun checkValidContentScheme(uri: Uri) = contentScheme.equals(uri.scheme, true)

    private fun getFileDataFromUri(context: Context, uri: Uri): URIMeta? =
        kotlin.runCatching {
            val idColumn = MediaStore.Images.Media._ID
            val nameColumn = OpenableColumns.DISPLAY_NAME
            val externalMediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            return readFileFromContentResolver(
                context, uri, arrayOf(idColumn, nameColumn)
            ) ?.let {cursor ->
                val idIndex = cursor.getColumnIndexOrThrow(idColumn)
                val nameColumnIndex = cursor.getColumnIndexOrThrow(nameColumn)

                val response = if(cursor.moveToNext()) {
                    val resultantUri = ContentUris.withAppendedId(externalMediaUri, cursor.getLong(idIndex))
                    buildUriMeta(
                        context,
                        uri,
                        resultantUri,
                        cursor.getString(nameColumnIndex),
                        0
                    )
                }
                else null
                cursor.close()
                response
            }
        }.onFailure {
            Log.e("ERROR", "Failed to get file data from content resolver, $uri")
        }.getOrNull()

    private fun readFileFromContentResolver(
        context: Context,
        uri: Uri,
        projections: Array<String>,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        order: String? = null
    ): Cursor? = context.contentResolver.query(
        uri, projections, selection, selectionArgs, order
    )

    private fun getFileMimeType(
        context: Context, uri: Uri
    ) = runCatching<String> {
        return context.contentResolver.getType(uri)
    }.onFailure {
        Log.e("ERROR","Could not fetch mime type $uri")
    }.getOrNull()

    private fun buildUriMeta(context: Context, uri: Uri, resultantUri: Uri, fileName: String, orientation: Int)=
        URIMeta(
            resultantUri, fileName, getFileMimeType(context, uri)  , orientation
        )

}