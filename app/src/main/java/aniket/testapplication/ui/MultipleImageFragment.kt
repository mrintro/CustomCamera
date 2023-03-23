package aniket.testapplication.ui

import android.hardware.Camera
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import aniket.testapplication.CameraApplication
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
    }

    private fun startTest(camera: Camera) {
        multipleImageViewModel.currentEV.observe(viewLifecycleOwner) {
            binding?.headerTitle?.text = String.format(requireContext().getString(R.string.capturing_image_ev), it.toString())
            camera.apply {
                setEVValueForCamera(it)
                takePicture(null, null, PhotoHandler {file ->
                    onSuccessCallback(file)
                })
            }
        }
    }

    private fun onSuccessCallback(file: File) {
        resetPreview()
        multipleImageViewModel.setImageFile(file)
    }

}