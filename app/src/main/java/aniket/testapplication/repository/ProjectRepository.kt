package aniket.testapplication.repository

import android.util.Log
import aniket.testapplication.HomeFragmentState
import aniket.testapplication.di.annotations.ApplicationScope
import aniket.testapplication.model.AuthAPIResponse
import aniket.testapplication.model.AuthResponseHeader
import aniket.testapplication.model.TokenRequest
import aniket.testapplication.model.UserData
import aniket.testapplication.repository.db.ProjectDB
import aniket.testapplication.repository.service.APIService
import aniket.testapplication.utils.APIEndPoints
import aniket.testapplication.utils.AuthAPICredentials
import arrow.core.Either
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@ApplicationScope
class ProjectRepository(
    private val roomDB: ProjectDB,
    private val service: APIService
) {

    private val BASE_URL = "http://apistaging.inito.com/api/v1/"


    suspend fun clearDB() = Either.catch{
        roomDB.getProjectDao().deleteAllData()
    }
    suspend fun populateUserInDB(userData: UserData) = Either.catch {
        roomDB.getProjectDao().addUser(userData)
    }

    suspend fun fetchUserFromDB() = Either.catch {
        roomDB.getProjectDao().getUser()
    }

     fun fetchAPIToken() = Either.catch {
        service.authAPI(
            BASE_URL + APIEndPoints.LOGIN,
             TokenRequest(
                 AuthAPICredentials.EMAIL,
                 AuthAPICredentials.PASSWORD
             )
        )
    }

    fun uploadImage(file: MultipartBody.Part, textMultipartBody: Map<String, RequestBody>) = Either.catch {
        service.uploadImage(
            BASE_URL + APIEndPoints.POST_IMAGE,
            textMultipartBody,
            file
        )
    }


}