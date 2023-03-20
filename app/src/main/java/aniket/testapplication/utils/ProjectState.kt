package aniket.testapplication.utils

import aniket.testapplication.model.AuthResponseHeader

sealed class HomeFragmentState {
    object NavigateToCameraScreen : HomeFragmentState()
    data class SaveToken(val authResponseHeader: AuthResponseHeader): HomeFragmentState()
}