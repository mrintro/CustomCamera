package aniket.testapplication.viewmodel

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel(), DefaultLifecycleObserver {

    var num = 10;

    fun addCounter() { num++ }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
    }

}