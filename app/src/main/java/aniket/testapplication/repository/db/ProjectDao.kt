package aniket.testapplication.repository.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import aniket.testapplication.model.UserData

@Dao
interface ProjectDao {

    @Insert
    suspend fun addUser(user: UserData)

    @Query("SELECT * from UserData")
    suspend fun getUser(): List<UserData>


}