package aniket.testapplication.repository.service

import aniket.testapplication.model.AuthAPIResponse
import aniket.testapplication.model.TokenRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface APIService {

    @Headers("Content-Type:application/json")
    @POST
    fun authAPI(
        @Url url: String,
        @Body tokenRequest: TokenRequest
    ) : Call<AuthAPIResponse>

}