package aniket.testapplication.di.modules

import aniket.testapplication.di.annotations.ApplicationScope
import aniket.testapplication.repository.service.APIService
import aniket.testapplication.utils.Constants
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {

    @ApplicationScope
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    @ApplicationScope
    @Provides
    fun provideAPIService(retrofit: Retrofit) : APIService = retrofit.create(APIService::class.java)

}