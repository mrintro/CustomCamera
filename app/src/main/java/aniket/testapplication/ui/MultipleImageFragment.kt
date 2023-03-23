package aniket.testapplication.ui

import android.graphics.Camera
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.view.TextureView
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import aniket.testapplication.CameraApplication
import aniket.testapplication.R
import aniket.testapplication.databinding.FragmentMultipleImageBinding
import aniket.testapplication.viewmodel.GlobalViewModel
import java.io.File

@Suppress("Deprecation")
class MultipleImageFragment : BaseCameraFragment(R.layout.fragment_multiple_image) {

    private val globalViewModel by activityViewModels<GlobalViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with((requireActivity().application as CameraApplication).applicationComponent) {
            inject(globalViewModel)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DataBindingUtil.bind<FragmentMultipleImageBinding>(view)?.apply {
            setCameraPreviewInitialized {
                capturePhoto(it)
            }
            attachListener(textureView)
        }
    }

    private fun capturePhoto(camera: android.hardware.Camera) {
        camera.takePicture(null, null, PhotoHandler {
            onSuccessCallback(it)
        })
    }

    private fun onSuccessCallback(file: File) {

    }

}