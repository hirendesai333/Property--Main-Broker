package com.soboft.propertybroker.network

import com.soboft.propertybroker.model.AllCustomerModel
import com.soboft.propertybroker.model.LoginModel
import com.soboft.propertybroker.utils.Params
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.*

private const val BASE_URL = "http://realestateapi.lamproskids.com/api/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(JacksonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface ApiKtService{

    @FormUrlEncoded
    @POST(LOGIN)
    suspend fun login(@Field("email")email : String,
                      @Field("password") password : String) : Response<LoginModel>

    @POST(SIGNUP)
    suspend fun signup(@Body data : Map<String,String>) : Response<Any>

    @FormUrlEncoded
    @POST(GETCUSTOMERS)
    suspend fun getAllCustomer(@Field("userId") userId : Int) : Response<AllCustomerModel>

}

object ServiceApi{

    val retrofitService : ApiKtService by lazy { retrofit.create(ApiKtService::class.java) }
}

private const val LOGIN = "users/Users_Login"
private const val SIGNUP = "users/Users_Upsert"
private const val GETCUSTOMERS = "customerMaster/CustomerMaster_All"