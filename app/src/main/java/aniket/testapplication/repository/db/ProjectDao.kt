package aniket.testapplication.repository.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import aniket.testapplication.model.UserData

@Dao
interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: UserData)

    @Query("SELECT * from UserData")
    suspend fun getUser(): List<UserData>

    @Query("DELETE FROM UserData")
    suspend fun deleteAllData()



}