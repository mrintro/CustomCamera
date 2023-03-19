package aniket.testapplication.viewmodel

import android.util.Log
import androidx.lifecycle.*
import aniket.testapplication.model.UserData
import aniket.testapplication.repository.ProjectRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel : ViewModel(), DefaultLifecycleObserver {

    private val nameTextField = MutableLiveData<String>()
    private val emailTextField = MutableLiveData<String>()

    val preFetchUserData = MutableLiveData<UserData>()
    val navigateToCameraScreen = MutableLiveData(false)

    @Inject
    lateinit var projectRepository: ProjectRepository

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        viewModelScope.launch {
            fetchUserFromDB()
        }
    }

    private suspend fun fetchUserFromDB() {
        projectRepository.fetchUserFromDB().fold({
            Log.e("ERROR", "Failed to get user data")
        }, {
            if(it.isNotEmpty())
                preFetchUserData.postValue(it[0])
        })
    }

    fun setNameTextField(nameValue : String) {
        nameTextField.value = nameValue
    }

    fun setEmailTextField(emailValue: String) {
        emailTextField.value = emailValue
    }

    fun onTakeTestClicked() {
        createUserObject()?.let {
            CoroutineScope(Dispatchers.Default).launch {
                populateUserInDB(it)
            }
        }
    }

    private suspend fun populateUserInDB(userData: UserData) {
        projectRepository.clearDB().fold({
            Log.e("ERROR", "Failed to delete old user data")
        }, {
            projectRepository.populateUserInDB(userData).fold({
                Log.e("ERROR", "Failed to save user data")
            }, {
                navigateToCameraScreen.postValue(true)
            })
        })
    }

    private fun createUserObject() = nameTextField.value?.let { userName ->
            emailTextField.value?.let { email ->
                UserData(userName, email)
            }
        }
}