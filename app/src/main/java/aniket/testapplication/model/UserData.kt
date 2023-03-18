package aniket.testapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserData(
    val name: String,
    @PrimaryKey(autoGenerate = false)
    val email: String
)
