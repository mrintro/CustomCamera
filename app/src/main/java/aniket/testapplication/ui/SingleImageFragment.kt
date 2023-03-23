package aniket.testapplication.ui

import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import aniket.testapplication.CameraApplication
import aniket.testapplication.R
import aniket.testapplication.SingleImageFragmentState
import aniket.testapplication.databinding.FragmentSingleImageBinding
import aniket.testapplication.ui.imageProcessor.URIResolver
import aniket.testapplication.viewmodel.GlobalViewModel
import aniket.testapplication.viewmodel.SingleImageViewModel
import java.io.File
import java.util.*
import javax.inject.Inject

@Suppress("Deprecation")
class SingleImageFragment : BaseCameraFragment(R.layout.fragment_single_image) {

    private val globalViewModel by activityViewModels<GlobalViewModel>()
    private val singleImageViewModel by viewModels<SingleImageViewModel>()

    @Inject
    lateinit var uriResolver: URIResolver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with((requireActivity().application as CameraApplication).applicationComponent) {
            inject(this@SingleImageFragment)
            inject(globalViewModel)
            inject(singleImageViewModel)
        }
        lifecycle.addObserver(singleImageViewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Getting Auth Header here", "Auth Header ${globalViewModel.authTokenData.value}")

        DataBindingUtil.bind<FragmentSingleImageBinding>(view)?.apply {

            lifecycleOwner = viewLifecycleOwner
            vm = singleImageViewModel

            setCameraPreviewInitialized {
                capturePhoto(it)
            }
            attachListener(textureView)
        }
        observeFragmentState()

    }

    private fun capturePhoto(camera: Camera) {
        camera.takePicture(null, null, PhotoHandler {
            onSuccessCallback(it)
        })
    }

    private fun onSuccessCallback(file: File) {
        globalViewModel.setSingleImageFile(file)
        singleImageViewModel.startProgress()
    }


    private fun navigateToMultipleImageScreen() {
        findNavController().navigate(SingleImageFragmentDirections.actionSingleImageToMultipleImage())
    }


    private fun observeFragmentState() {
        singleImageViewModel.singleImageFragmentState.observe(viewLifecycleOwner) {
            when (it) {
                is SingleImageFragmentState.TimerFinished -> navigateToMultipleImageScreen()
                else -> Unit
            }
        }
    }

}