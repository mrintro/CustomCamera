package aniket.testapplication.ui

import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import aniket.testapplication.CameraApplication
import aniket.testapplication.MultipleImageFragmentState
import aniket.testapplication.R
import aniket.testapplication.databinding.FragmentMultipleImageBinding
import aniket.testapplication.utils.setEVValueForCamera
import aniket.testapplication.viewmodel.GlobalViewModel
import aniket.testapplication.viewmodel.MultipleImageViewModel
import java.io.File

@Suppress("Deprecation")
class MultipleImageFragment : BaseCameraFragment(R.layout.fragment_multiple_image) {

    private val globalViewModel by activityViewModels<GlobalViewModel>()
    private val multipleImageViewModel by viewModels<MultipleImageViewModel>()
    private var binding: FragmentMultipleImageBinding? = null
    private val photoHandler by lazy {
        PhotoHandler {
            onSuccessCallback(it)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with((requireActivity().application as CameraApplication).applicationComponent) {
            inject(globalViewModel)
            inject(multipleImageViewModel)
        }
        lifecycle.addObserver(multipleImageViewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind<FragmentMultipleImageBinding>(view)?.apply {

            lifecycleOwner = viewLifecycleOwner
            vm = multipleImageViewModel

            headerTitle.text = requireContext().getString(R.string.capturing_multiple_image)

            attachListener(textureView)
            setCameraPreviewInitialized {
                if(!multipleImageViewModel.isTestStarted()) {
                    multipleImageViewModel.startTest()
                    startTest(it)
                }
            }
        }
        observeEvents()
    }

    private fun startTest(camera: Camera) {
        multipleImageViewModel.currentEV.observe(viewLifecycleOwner) {
            binding?.headerTitle?.text = String.format(getString(R.string.capturing_image_ev), it.toString())
            camera.apply {
                setEVValueForCamera(it)
                takePicture(null, null, photoHandler)
            }
        }
    }

    private fun onSuccessCallback(file: File) {
        resetPreview()
        multipleImageViewModel.setImageFile(file)
    }

    private fun postImages() {
        binding?.headerTitle?.text = getString(R.string.sending_image)
        multipleImageViewModel.getUploadFile()?.let {
            globalViewModel.postImage(it)
        } ?: run {
            Log.e("ERROR", "Cannot find image to upload.")
        }

    }

    private fun observeEvents() {
        multipleImageViewModel.multipleImageFragmentState.observe(viewLifecycleOwner) {
            when(it) {
                is MultipleImageFragmentState.ImagesCaptured -> postImages()
                else -> Unit
            }
        }
        globalViewModel.gvmMultipleImageFragmentState.observe(viewLifecycleOwner) {
            when(it) {
                is MultipleImageFragmentState.PostImageFailed -> binding?.headerTitle?.text = getString(R.string.test_successful)
                is MultipleImageFragmentState.PostImageSuccessful -> binding?.headerTitle?.text = getString(R.string.test_failed)
                else -> Unit
            }
        }
    }

}