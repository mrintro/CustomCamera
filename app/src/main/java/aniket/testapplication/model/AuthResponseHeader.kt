package aniket.testapplication.model

data class AuthResponseHeader(
    val accessToken: String,
    val uid: String,
    val client: String
)
