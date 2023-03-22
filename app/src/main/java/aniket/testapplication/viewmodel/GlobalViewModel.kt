package aniket.testapplication.viewmodel

import android.net.Uri
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import aniket.testapplication.model.AuthResponseHeader

class GlobalViewModel : ViewModel(), DefaultLifecycleObserver {

    private val _authTokenData = MutableLiveData<AuthResponseHeader>()
    val authTokenData: LiveData<AuthResponseHeader> = _authTokenData

    private val _singleImageUri = MutableLiveData<Uri>()
    val singleImageUri: LiveData<Uri> = _singleImageUri

    fun setTokenData(authResponseHeader: AuthResponseHeader) {
        _authTokenData.postValue(authResponseHeader)
    }

    fun setSingleImageUri(uri: Uri) {
        _singleImageUri.postValue(uri)
    }

}