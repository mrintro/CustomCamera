package aniket.testapplication.viewmodel

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import aniket.testapplication.MultipleImageFragmentState
import aniket.testapplication.utils.SingleLiveEvent
import java.io.File

class MultipleImageViewModel: ViewModel(), DefaultLifecycleObserver {

    private val _evFileMap = mutableMapOf<Int, File>()

    private val _currentEV = MutableLiveData<Int>(12)
    val currentEV : LiveData<Int> = _currentEV

    val multipleImageFragmentState = SingleLiveEvent<MultipleImageFragmentState>()

    private var testStarted = false

    fun startTest() {
        testStarted = true
    }

    fun isTestStarted() = testStarted



    fun setImageFile(file: File) {
        var ev = currentEV.value
        ev?.let {
            _evFileMap[ev] = file
            if(ev > -12) {
                _currentEV.value = --ev
            } else{
                multipleImageFragmentState.value = MultipleImageFragmentState.ImagesCaptured
            }
        }
    }

    fun getUploadFile(): File? = _evFileMap[0]


}