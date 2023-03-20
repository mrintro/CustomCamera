package aniket.testapplication.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import aniket.testapplication.CameraApplication
import aniket.testapplication.R
import aniket.testapplication.viewmodel.GlobalViewModel

class SingleImageFragment : Fragment(R.layout.fragment_single_image) {

    private val globalViewModel by activityViewModels<GlobalViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with((requireActivity().application as CameraApplication).applicationComponent) {
            inject(this@SingleImageFragment)
            inject(globalViewModel)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Getting Auth Header here", "Auth Header ${globalViewModel.authTokenData.value}")

    }

}