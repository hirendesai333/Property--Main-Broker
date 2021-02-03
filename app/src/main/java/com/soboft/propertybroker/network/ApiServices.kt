package com.soboft.propertybroker.network

import com.soboft.propertybroker.model.*
import com.soboft.propertybroker.utils.Params
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

//    @POST(SIGNUP)
//    suspend fun signup(@Body data : Map<String,String>) : Response<Any>

    @FormUrlEncoded
    @POST(GETCUSTOMERS)
    suspend fun getAllCustomer(@Field("userId") userId : Int) : Response<AllCustomerModel>

    @POST(ADDCUSTOMER)
    suspend fun addCustomer(@Body data: Map<String, String>) : Response<AddCustomerModel>

    @POST(UPDATECUSTOMER)
    suspend fun updateCustomer(@Body data: Map<String, String>) : Response<AddCustomerModel>

    @POST(GETPROPERTIES)
    suspend fun getAllProperties(@Query("userId") userId: Int, @Body data: Map<String, String>) : Response<AllPropertiesModel>

    @POST(ADDPROPERTY)
    suspend fun addProperty(@Body data: Map<String, String>) : Response<AddPropertiesModel>

    @POST(UPDATEPROPERTY)
    suspend fun updateProperty(@Body data: Map<String, String>) : Response<AddPropertiesModel>

    @POST(GETPROPERTYDETAILS)
    suspend fun getPropertyDetails(@Query("Id") userId: Int) : Response<PropertyDetailsModel>

    @POST(GETPROPERTYTYPE)
    suspend fun getAllPropertyType(@Query("search") userId: Int,@Body data: Map<String, String>) : Response<AllPropertyTypeModel>

    @POST(GETAVALIBLEFOR)
    suspend fun  getAllAvailableProperty(@Query("search") userId: Int, @Body data: Map<String, String>) : Response<AllAvailablePropertyModel>

    @POST(DELETEPROPERTY)
    suspend fun deleteProperty(@Query("id") Id : Int) : Response<Any>

    @POST(GETUSERPROFILE)
    suspend fun getUserProfile(@Query("id") userId : Int) : Response<UserProfileModel>

    @POST(UPDATEPROFILE)
    suspend fun updateUserProfile(@Body data: Map<String, String>) : Response<UpdateUserProfileModel>

    @POST(GETCOUNTRY)
    suspend fun getCountry(@Query("search") userId: Int, @Body data: Map<String, String>) : Response<AllCountryModel>

    @POST(GETSTATE)
    suspend fun getState(@Query("CountryId") userId: Int, @Body data: Map<String, String>) : Response<AllStateListModel>

    @POST(GETCITY)
    suspend fun getCity(@Body data: Map<String, String>) : Response<AllCityModel>

    @POST(GETUSERLANGUAGE)
    suspend fun getUserLanguage(@Query("userId") userId: Int, @Body data: Map<String, String>) : Response<AllUserLanguageModel>

    @POST(GETUSERLOCATION)
    suspend fun getUserLocation(@Query("userId") userId: Int, @Body data: Map<String, String>) : Response<AllUserLocationModel>

    @Multipart
    @POST(UPLOAD_PROFILE_PIC)
    suspend fun uploadProfilePic(
        @Part("id") id: RequestBody,
        @Part files: MultipartBody.Part
    ): Response<UserProfileModel>

    @POST(GETAVALIBLEJOBS)
    suspend fun getAvailableJobs(@Query("userId") userId: Int,
                                 @Query("statusMasterId") statusMasterId : Int,
                                 @Body data: Map<String, String>) : Response<AllAvalibleJobsModel>
}

object ServiceApi{
    val retrofitService : ApiKtService by lazy { retrofit.create(ApiKtService::class.java) }
}

private const val LOGIN = "users/Users_Login"
private const val GETCUSTOMERS = "customerMaster/CustomerMaster_All"
private const val ADDCUSTOMER = "customerMaster/CustomerMaster_Upsert"
private const val UPDATECUSTOMER = "customerMaster/CustomerMaster_Upsert"
private const val GETPROPERTIES = "propertyMaster/PropertyMaster_All"
private const val ADDPROPERTY = "propertyMaster/PropertyMaster_Upsert"
private const val UPDATEPROPERTY = "propertyMaster/PropertyMaster_Upsert"
private const val GETPROPERTYDETAILS = "propertyMaster/PropertyMaster_ById"
private const val GETPROPERTYTYPE = "propertyTypeMaster/PropertyTypeMaster_All"
private const val GETAVALIBLEFOR = "availableForMaster/AvailableForMaster_All"
private const val DELETEPROPERTY = "propertyMaster/PropertyMaster_Delete"
private const val GETUSERPROFILE = "users/Users_ById"
private const val UPDATEPROFILE = "users/Users_Upsert"
private const val GETCOUNTRY = "countryMaster/CountryMaster_All"
private const val GETSTATE = "stateMaster/StateMaster_All"
private const val GETCITY = "cityMaster/CityMaster_All"
private const val GETUSERLANGUAGE = "userLanguages/UserLanguages_All"
private const val GETUSERLOCATION = "userPreferedLocations/UserPreferedLocations_All"
private const val UPLOAD_PROFILE_PIC = "users/Users_ProfileUpdate"
private const val GETAVALIBLEJOBS = "jobs/Jobs_All"