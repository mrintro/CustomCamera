package aniket.testapplication.viewmodel

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import aniket.testapplication.MultipleImageFragmentState
import aniket.testapplication.model.AuthResponseHeader
import aniket.testapplication.model.getHeaderMap
import aniket.testapplication.model.getTextMultipartMap
import aniket.testapplication.model.toMultipart
import aniket.testapplication.repository.ProjectRepository
import aniket.testapplication.utils.SingleLiveEvent
import java.io.File
import javax.inject.Inject

class GlobalViewModel : ViewModel(), DefaultLifecycleObserver {

    @Inject
    lateinit var projectRepository: ProjectRepository

    private val _authTokenData = MutableLiveData<AuthResponseHeader>()
    val authTokenData: LiveData<AuthResponseHeader> = _authTokenData

    private val _singleImageFile = MutableLiveData<File>()
    val singleImageFile: LiveData<File> = _singleImageFile

    val gvmMultipleImageFragmentState = SingleLiveEvent<MultipleImageFragmentState>()

    fun setTokenData(authResponseHeader: AuthResponseHeader) {
        _authTokenData.postValue(authResponseHeader)
    }

    fun setSingleImageFile(file: File) {
        _singleImageFile.postValue(file)
    }

    fun postImage(file: File) {
        val fileMultipartFormData = file.toMultipart()
        val textMultipartBodyMap = getTextMultipartMap()
        val headerMap = getHeaderMap(authTokenData.value)
        projectRepository.uploadImage(headerMap, fileMultipartFormData, textMultipartBodyMap).fold(
            {
                Log.e("ERROR", "Cannot Upload Image, $it")
                gvmMultipleImageFragmentState.value = MultipleImageFragmentState.PostImageFailed
            },
            {
                /**
                 * Do onResponse here.
                 */
                gvmMultipleImageFragmentState.value = MultipleImageFragmentState.PostImageFailed
            }
        )
    }


}