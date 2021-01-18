package com.soboft.propertybroker.network

import com.soboft.propertybroker.model.LoginModel
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

private const val BASE_URL = "http://realestateapi.lamproskids.com/api/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(JacksonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface ApiKtService{

    @POST(LOGIN)
    suspend fun login(@Body data : String) : Response<LoginModel>

    @POST(SIGNUP)
    suspend fun signup(@Body data : Map<String,String>) : Response<Any>

}

object ServiceApi{

    val retrofitService : ApiKtService by lazy { retrofit.create(ApiKtService::class.java) }
}

private const val LOGIN = "users/Users_Login"
private const val SIGNUP = "users/Users_Upsert"