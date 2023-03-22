package aniket.testapplication

import aniket.testapplication.model.AuthResponseHeader

sealed class HomeFragmentState {
    object NavigateToCameraScreen : HomeFragmentState()
    data class SaveToken(val authResponseHeader: AuthResponseHeader): HomeFragmentState()
}

sealed class SingleImageFragmentState {
    object TimerFinished : SingleImageFragmentState()
}