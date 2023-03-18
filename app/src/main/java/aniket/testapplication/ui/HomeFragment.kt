package aniket.testapplication.ui

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import aniket.testapplication.CameraApplication
import aniket.testapplication.R
import aniket.testapplication.databinding.FragmentHomeBinding
import aniket.testapplication.viewmodel.MainViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with((requireActivity().application as CameraApplication).applicationComponent) {
            inject(this@HomeFragment)
            inject(mainViewModel)
        }
        lifecycle.addObserver(mainViewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DataBindingUtil.bind<FragmentHomeBinding>(view)?.apply {

            nameTextView.text = mainViewModel.num.toString()

            takeTestButton.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeToSingleImageFragment())
            }

            mainViewModel.addCounter()

        }

    }

}