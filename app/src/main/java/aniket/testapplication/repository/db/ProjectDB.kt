package aniket.testapplication.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import aniket.testapplication.model.UserData

@Database(entities = [UserData::class], version = 1)
abstract class ProjectDB : RoomDatabase() {
    abstract fun getProjectDao() : ProjectDao

}