package aniket.testapplication.viewmodel

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import aniket.testapplication.model.AuthResponseHeader

class GlobalViewModel : ViewModel(), DefaultLifecycleObserver {

    private val _authTokenData = MutableLiveData<AuthResponseHeader>()
    val authTokenData: LiveData<AuthResponseHeader> = _authTokenData

    fun setTokenData(authResponseHeader: AuthResponseHeader) {
        _authTokenData.postValue(authResponseHeader)
    }

}