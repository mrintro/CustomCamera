package aniket.testapplication.viewmodel

import android.util.Log
import androidx.lifecycle.*
import aniket.testapplication.model.AuthAPIResponse
import aniket.testapplication.model.AuthResponseHeader
import aniket.testapplication.model.UserData
import aniket.testapplication.repository.ProjectRepository
import aniket.testapplication.utils.HomeFragmentState
import aniket.testapplication.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainViewModel : ViewModel(), DefaultLifecycleObserver {

    private val nameTextField = MutableLiveData<String>()
    private val emailTextField = MutableLiveData<String>()

    val preFetchUserData = MutableLiveData<UserData>()
    val homeFragmentState = SingleLiveEvent<HomeFragmentState>()

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
                Log.e("ERROR", "Failed to save user data, $it")
            }, {
                fetchAPIToken()
            })
        })
    }

    private suspend fun fetchAPIToken() {
        projectRepository.fetchAPIToken().fold({
            Log.e("ERROR", "Cannot fetch token for token API, $it")
        }, {
            Log.d("FetchResponse", "Response : $it")
            it.enqueue(object: Callback<AuthAPIResponse> {
                override fun onResponse(
                    call: Call<AuthAPIResponse>,
                    response: Response<AuthAPIResponse>
                ) {
                    val authResponse = response.headers().get("access-token")
                    homeFragmentState.value = HomeFragmentState.SaveToken(AuthResponseHeader(authResponse.toString()))
                    val responseBody = response.body()
                    if(responseBody?.onboarded == true){
                        homeFragmentState.value = HomeFragmentState.NavigateToCameraScreen
                    }
                }
                override fun onFailure(call: Call<AuthAPIResponse>, t: Throwable) {
                    Log.e("ERROR", "Cannot fetch token for token API after callback, $it")
                }
            } )
        })
    }

    private fun createUserObject() = nameTextField.value?.let { userName ->
            emailTextField.value?.let { email ->
                UserData(userName, email)
            }
        }
}