package aniket.testapplication.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.hardware.Camera.PictureCallback
import android.hardware.Camera.PreviewCallback
import android.os.Bundle
import android.os.Environment
import android.view.TextureView
import android.view.View
import androidx.fragment.app.Fragment
import aniket.testapplication.utils.Constants.PROJECT_DIR
import aniket.testapplication.utils.postData
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Suppress("Deprecation")
open class BaseCameraFragment(
    layout: Int
): Fragment(layout), TextureView.SurfaceTextureListener {

    private var isCameraSupported = false
    private var camera: Camera? = null
    private lateinit var cameraPreviewInitialized: (Camera) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCameraSupported = checkCameraHardware(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        camera = kotlin.runCatching { Camera.open() }.onFailure { throw (it) }.getOrNull()
    }

    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }

    fun attachListener(textureView: TextureView) {
        if(isCameraSupported) {
            textureView.surfaceTextureListener = this@BaseCameraFragment
        }
    }

    fun setCameraPreviewInitialized(block : (Camera) -> Unit) {
        cameraPreviewInitialized = block
    }

    class PhotoHandler(private val callbackOnSuccess: (File)->Unit): PictureCallback {
        @Deprecated("Deprecated in Java")
        override fun onPictureTaken(data: ByteArray?, camera: Camera?) {
            createProjectDir()?.let {
                val imageFile = getImageFile(it)
                imageFile.postData(data)?.let {capturedFile ->
                    callbackOnSuccess.invoke(capturedFile)
                }
            }


        }

        private fun createProjectDir() : File? {
            val rootDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val projectDir = File(rootDir, PROJECT_DIR)
            return if(!projectDir.exists() && !projectDir.mkdirs()) null else projectDir
        }
        @SuppressLint("SimpleDateFormat")
        private fun getImageFile(dirFile: File) : File {
            val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            return File("${dirFile.path}${File.separator}IMG_$dateFormat.jpg")
        }

    }

    private val previewCallBack = PreviewCallback { _, camera ->
        camera?.let{
            cameraPreviewInitialized.invoke(it)
        }
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {

        kotlin.runCatching {
            camera?.apply {
                val cameraParams = parameters
                cameraParams.setRotation(90)
                parameters = cameraParams
                setPreviewTexture(surface)
                setDisplayOrientation(90)
                setPreviewCallback(previewCallBack)
                startPreview()
            }
        }.onFailure {
            throw (it)
        }
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        kotlin.runCatching {
            camera?.apply {
                stopPreview()
            }
        }.onFailure { throw (it) }
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        camera?.release()
    }

}