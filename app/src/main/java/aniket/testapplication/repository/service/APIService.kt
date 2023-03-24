package aniket.testapplication.repository.service

import aniket.testapplication.model.AuthAPIResponse
import aniket.testapplication.model.TokenRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Url

interface APIService {

    @Headers("Content-Type:application/json")
    @POST
    fun authAPI(
        @Url url: String,
        @Body tokenRequest: TokenRequest
    ) : Call<AuthAPIResponse>

    @Multipart
    @POST
    fun uploadImage(
        @Url url: String,
        @PartMap map: Map<String, RequestBody>,
        @Part file: MultipartBody.Part
    ) : Call<Any>

}