package aniket.testapplication.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import aniket.testapplication.CameraApplication
import aniket.testapplication.R
import aniket.testapplication.databinding.FragmentHomeBinding
import aniket.testapplication.utils.HomeFragmentState
import aniket.testapplication.viewmodel.GlobalViewModel
import aniket.testapplication.viewmodel.MainViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val mainViewModel by viewModels<MainViewModel>()
    private val globalViewModel by activityViewModels<GlobalViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with((requireActivity().application as CameraApplication).applicationComponent) {
            inject(this@HomeFragment)
            inject(mainViewModel)
            inject(globalViewModel)
        }
        lifecycle.addObserver(mainViewModel)
        lifecycle.addObserver(globalViewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        DataBindingUtil.bind<FragmentHomeBinding>(view)?.apply {

            lifecycleOwner = viewLifecycleOwner
            vm = mainViewModel



            nameEditText.addTextChangedListener(nameEditTextWatcher)
            emailEditText.addTextChangedListener(emailEditTextWatcher)

        }

        observeFragmentState()


    }

    val nameEditTextWatcher = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // No implementation
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            mainViewModel.setNameTextField(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {
            // No implementation
        }

    }

    private fun navigateToCameraScreen() {
        findNavController().navigate(HomeFragmentDirections.actionHomeToSingleImageFragment())
    }

    val emailEditTextWatcher = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // No implementation
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            mainViewModel.setEmailTextField(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {
            // No implementation
        }

    }


    private fun observeFragmentState() {
        mainViewModel.homeFragmentState.observe(viewLifecycleOwner) {
            when(it) {
                is HomeFragmentState.NavigateToCameraScreen -> navigateToCameraScreen()
                is HomeFragmentState.SaveToken -> globalViewModel.setTokenData(it.authResponseHeader)
                else -> Unit
            }
        }
    }

}