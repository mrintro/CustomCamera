package aniket.testapplication.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SingleImageViewModel: ViewModel(), DefaultLifecycleObserver {

    private val progressTimeInMilli = 20000L
    private val progressGapInMilli = 2000L

    private val _progressPercent = MutableLiveData(100)
    val progressPercent : LiveData<Int> = _progressPercent

    private val _remainingTime = MutableLiveData(progressTimeInMilli)
    val remainingTime : LiveData<Long> = _remainingTime

    fun startProgress() {
        var remainingTimeValue = progressTimeInMilli
        viewModelScope.launch {
            while(remainingTimeValue >= 0) {
                _remainingTime.value = remainingTimeValue/1000
                _progressPercent.value = (remainingTimeValue * 100 / progressTimeInMilli).toInt()
                delay(progressGapInMilli)
                remainingTimeValue -= progressGapInMilli
            }
        }
    }

}