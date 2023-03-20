package aniket.testapplication.repository

import aniket.testapplication.di.annotations.ApplicationScope
import aniket.testapplication.model.TokenRequest
import aniket.testapplication.model.UserData
import aniket.testapplication.repository.db.ProjectDB
import aniket.testapplication.repository.service.APIService
import aniket.testapplication.utils.APIEndPoints
import aniket.testapplication.utils.AuthAPICredentials
import arrow.core.Either

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


}