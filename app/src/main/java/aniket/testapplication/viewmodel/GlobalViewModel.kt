package aniket.testapplication.viewmodel

import android.net.Uri
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import aniket.testapplication.model.AuthResponseHeader
import aniket.testapplication.model.createImageRequestObject
import aniket.testapplication.model.toMultipart
import aniket.testapplication.repository.ProjectRepository
import java.io.File
import javax.inject.Inject

class GlobalViewModel : ViewModel(), DefaultLifecycleObserver {

    @Inject
    lateinit var projectRepository: ProjectRepository

    private val _authTokenData = MutableLiveData<AuthResponseHeader>()
    val authTokenData: LiveData<AuthResponseHeader> = _authTokenData

    private val _singleImageFile = MutableLiveData<File>()
    val singleImageFile: LiveData<File> = _singleImageFile

    fun setTokenData(authResponseHeader: AuthResponseHeader) {
        _authTokenData.postValue(authResponseHeader)
    }

    fun setSingleImageFile(file: File) {
        _singleImageFile.postValue(file)
    }

    fun postImage() {
        singleImageFile.value?.let {
            val multipartFormData = it.toMultipart()
//            val requestObject = it.createImageRequestObject().toRequestBody()

        }
    }


}