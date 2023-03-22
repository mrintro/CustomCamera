package aniket.testapplication.ui

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import aniket.testapplication.CameraApplication
import aniket.testapplication.R
import aniket.testapplication.databinding.FragmentSingleImageBinding
import aniket.testapplication.viewmodel.GlobalViewModel
import aniket.testapplication.viewmodel.SingleImageViewModel
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
@Suppress("Deprecation")
class SingleImageFragment : Fragment(R.layout.fragment_single_image) {

    private val globalViewModel by activityViewModels<GlobalViewModel>()
    private val singleImageViewModel by viewModels<SingleImageViewModel>()

    private var mCamera: Camera? = null
    private var isCameraSuported: Boolean? = false

    private val mPicture = Camera.PictureCallback { data, _ ->

        val pictureFile: File = getOutputMediaFile(MEDIA_TYPE_IMAGE) ?: run {
            Log.d("Aniket", ("Error creating media file, check storage permissions"))
            return@PictureCallback
        }
        val Uri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE))

        try {
            val fos = FileOutputStream(pictureFile)
            fos.write(data)
            fos.close()
            singleImageViewModel.startProgress()
        } catch (e: FileNotFoundException) {
            Log.e("Aniket", "File not found: ${e.message}")
        } catch (e: IOException) {
            Log.e("Aniket", "Error accessing file: ${e.message}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with((requireActivity().application as CameraApplication).applicationComponent) {
            inject(this@SingleImageFragment)
            inject(globalViewModel)
            inject(singleImageViewModel)
        }
        lifecycle.addObserver(singleImageViewModel)
        isCameraSuported = checkCameraHardware(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Getting Auth Header here", "Auth Header ${globalViewModel.authTokenData.value}")

        DataBindingUtil.bind<FragmentSingleImageBinding>(view)?.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = singleImageViewModel
            if (isCameraSuported == true) {
                mCamera = getCameraInstance()

                mCamera?.let { context?.let { it1 ->
                    CameraPreview(it1, it) {
                        if (isCameraSuported == true) it.takePicture(null, null, mPicture)
                    }

                }}?.also {
                    val preview: FrameLayout = cameraPreview
                    preview.addView(it)
                }
            }



        }

    }

    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }

    fun getCameraInstance(): Camera? {
        return try {
            Camera.open()
        } catch (e: Exception) {
            Log.e("Aniket", "Aniket :" + e.message )
            null
        }
    }

    /** A basic Camera preview class */
    internal class CameraPreview(
        context: Context,
        private val mCamera: Camera,
        private val block: () -> Unit
    ) : SurfaceView(context), SurfaceHolder.Callback {

        private val mHolder: SurfaceHolder = holder.apply {

            addCallback(this@CameraPreview)
            setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        }

        override fun surfaceCreated(holder: SurfaceHolder) {
            mCamera.apply {
                try {
                    setPreviewDisplay(holder)
                    startPreview()
                    block.invoke()
                } catch (e: IOException) {
                    Log.d("Aniket", "Error setting camera preview: ${e.message}")
                }
            }
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, w: Int, h: Int) {

            if (mHolder.surface == null) {
                return
            }

            // stop preview before making changes
            try {
                mCamera.stopPreview()
            } catch (e: Exception) {
                Log.e("Aniket", "Error setting camera preview: ${e.message}")
                // ignore: tried to stop a non-existent preview
            }

            // set preview size and make any resize, rotate or
            // reformatting changes here

            // start preview with new settings
            mCamera.apply {
                try {
                    setPreviewDisplay(mHolder)
                    startPreview()
                    block.invoke()
                } catch (e: Exception) {
                    Log.e("Aniket", "Error starting camera preview: ${e.message}")
                }
            }
        }
    }

    val MEDIA_TYPE_IMAGE = 1
    val MEDIA_TYPE_VIDEO = 2


    /** Create a file Uri for saving an image or video */
    private fun getOutputMediaFileUri(type: Int): Uri {
        return Uri.fromFile(getOutputMediaFile(type))
    }

    /** Create a File for saving an image or video */
    private fun getOutputMediaFile(type: Int): File? {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        val mediaStorageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "MyCameraApp"
        )

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        mediaStorageDir.apply {
            if (!exists()) {
                if (!mkdirs()) {
                    Log.d("MyCameraApp", "failed to create directory")
                    return null
                }
            }
        }

        // Create a media file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        if (mediaStorageDir != null) {
            return when (type) {
                MEDIA_TYPE_IMAGE -> {
                    File("${mediaStorageDir.path}${File.separator}IMG_$timeStamp.jpg")
                }
                MEDIA_TYPE_VIDEO -> {
                    File("${mediaStorageDir.path}${File.separator}VID_$timeStamp.mp4")
                }
                else -> null
            }
        }
        Log.d("MyCameraApp", "mediaStorageDir null")

        return null
    }

    private fun releaseCamera() {
        mCamera?.release() // release the camera for other applications
        Log.d("Aniket", "releaseCamera called")
        mCamera = null
    }



    override fun onPause() {
        super.onPause()
        releaseCamera()
    }


}