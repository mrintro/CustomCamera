package aniket.testapplication.viewmodel

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import aniket.testapplication.model.UserData
import aniket.testapplication.repository.ProjectRepository
import arrow.core.Either
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel : ViewModel(), DefaultLifecycleObserver {

    private val nameTextField = MutableLiveData<String>()
    private val emailTextField = MutableLiveData<String>()

    @Inject
    lateinit var projectRepository: ProjectRepository

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
    }

    fun setNameTextField(nameValue : String) {
        nameTextField.value = nameValue
    }

    fun setEmailTextField(emailValue: String) {
        emailTextField.value = emailValue
    }

    fun onTakeTestClicked() : Unit {
        createUserObject()?.let {
            CoroutineScope(Dispatchers.Default).launch {
                populateUserInDB(it)
            }
        }
    }

    private suspend fun populateUserInDB(userData: UserData) {
        projectRepository.populateUserInDB(userData).fold({
            Log.e("ERROR", "Failed to save user data")
        }, {
            projectRepository.fetchUserFromDB().fold({

            }, {
                Log.d("DEBUG", "user list $it")
            })
        })
    }

    private fun createUserObject() = nameTextField.value?.let { userName ->
            emailTextField.value?.let { email ->
                UserData(userName, email)
            }
        }
}