package aniket.testapplication.repository

import aniket.testapplication.di.annotations.ApplicationScope
import aniket.testapplication.model.UserData
import aniket.testapplication.repository.db.ProjectDB
import aniket.testapplication.repository.db.ProjectDao
import aniket.testapplication.repository.service.APIService
import arrow.core.Either

@ApplicationScope
class ProjectRepository(
    private val roomDB: ProjectDB,
    private val service: APIService
) {


    suspend fun clearDB() = Either.catch{
        roomDB.getProjectDao().deleteAllData()
    }
    suspend fun populateUserInDB(userData: UserData) = Either.catch {
        roomDB.getProjectDao().addUser(userData)
    }

    suspend fun fetchUserFromDB() = Either.catch {
        roomDB.getProjectDao().getUser()
    }


}