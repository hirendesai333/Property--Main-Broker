package com.illopen.agent.network

import com.illopen.agent.model.*
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.*

private const val BASE_URL = "http://realestateapi.lamproskids.com/api/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(JacksonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    )
    .build()

interface ApiKtService{

    @FormUrlEncoded
    @POST(LOGIN)
    suspend fun login(@Field("email")email : String,
                      @Field("password") password : String) : Response<LoginModel>

    @POST(SIGNUP)
    suspend fun signUp(@Body data : Map<String,String>) : Response<Any>

    @FormUrlEncoded
    @POST(LOGIN)
    suspend fun forgotPassword(@Field("Email") Email: String,
                               @Field("LoginType") LoginType : Boolean) : Response<LoginModel>

    @POST(CHANGE_PASSWORD)
    suspend fun changePassword(@Query("Type") Type : Int, @Body data: Map<String, String>) : Response<Any>

    @POST(GETCUSTOMERS)
    suspend fun getAllCustomer(@Query("UserId") UserId : Int, @Body data: Map<String, String>) : Response<AllCustomerModel>

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
    suspend fun getCountry(@Body data: Map<String, String>) : Response<AllCountryModel>

    @POST(GETSTATE)
    suspend fun getState(@Query("CountryId") userId: Int, @Body data: Map<String, String>) : Response<AllStateListModel>

    @POST(GETCITY)
    suspend fun getCity(@Query("StateId") stateId : Int, @Body data: Map<String, String>) : Response<AllCityModel>

    @POST(GETUSERLANGUAGE)
    suspend fun getUserLanguage(@Query("userId") userId: Int, @Body data: Map<String, String>) : Response<AllUserLanguageModel>

    @POST(GETLANGUAGEUPDATE)
    suspend fun userLanguageUpdate(@Body data : ArrayList<sendLanguageData>) : Response<Any>

    @POST(INSERT_LOCATION)
    suspend fun getUserLocationInsert(@Body data: Map<String, String>) : Response<Any>

    @Multipart
    @POST(UPLOAD_PROFILE_PIC)
    suspend fun uploadProfilePic(@Part("id") id: RequestBody, @Part files: MultipartBody.Part): Response<UserProfileModel>

    @POST(GETNEWAVAILABLEJOBS)
    suspend fun getNewAvailableJobs(@Query("userId") userId: Int,
                                 @Query("StatusMasterId") statusMasterId : Int,
                                 @Query("AvailableJobsOnly") availableJobs: Boolean,
                                 @Body data: Map<String, String>) : Response<AllAvalibleJobsModel>

    @POST(GETNEWMYPOSTEDJOBS)
    suspend fun getNewMyPostedJobs(@Query("userId") userId: Int,
                                    @Query("StatusMasterId") statusMasterId : Int,
                                    @Query("AvailableJobsOnly") availableJobs: Boolean,
                                    @Body data: Map<String, String>) : Response<NewMyPostedJobModel>

    @POST(GETONGOINGJOBS)
    suspend fun onGoingMyPostedJobs(@Query("userId") userId : Int,
                                @Query("StatusMasterId") statusMasterId: Int,
                                @Query("AvailableJobsOnly") boolean: Boolean,
                                @Body data: Map<String, String>) : Response<OngoingMyPostedJobModel>

    @POST(GETONGOINGJOBSASSIGNED)
    suspend fun getOnGoingJobsAssigned(@Query("StatusMasterId") statusMasterId: Int,
                                       @Query("AvailableJobsOnly") boolean: Boolean,
                                       @Query("AssignedUserId") assignedUserId: Int,
                                       @Body data: Map<String, String>) : Response<AllAssignedJobModel>

    @POST(GETCOMPLETEDJOBSFORCompleted)
    suspend fun getAssignedCompletedJobs(@Query("StatusMasterId") statusMasterId : Int,
                                 @Query("AvailableJobsOnly") AvailableJobsOnly : Boolean,
                                 @Query("AssignedUserId") assignedUserId: Int,
                                 @Body data: Map<String, String>) : Response<CompletedJobsAssignModel>

    @POST(GETCOMPLETEDJOBS)
    suspend fun getCompletedMyPostedJobs(@Query("userId") userId: Int,
                                         @Query("StatusMasterId") statusMasterId : Int,
                                         @Query("AvailableJobsOnly") boolean: Boolean,
                                         @Body data: Map<String, String>) : Response<AllCompletedJobsModel>

    @POST(GETJOBPROPERTY)
    suspend fun getJobProperty(@Query("JobId") jobId : Int,
                               @Query("UserId") userId: Int,
                                @Body data: Map<String, String>) : Response<JobPropertyModel>

    @POST(GETJOBBID)
    suspend fun jobPropertyBid(@Body data: Map<String, String>) : Response<JobPropertyBidModel>

    @POST(JOBPROPERTYBIDALL)
    suspend fun getJobPropertyBidAll(@Query("userId") userId: Int,
                                    @Query("JobId") jobId: Int,
                                    @Body data: Map<String, String>) : Response<JobPropertyBidAllModel>

    @POST(JOB_PROPERTY_ALL_BIDS)
    suspend fun getJobAllBids(@Query("JobId") jobId: Int,
                              @Body data: Map<String, String>) : Response<AllJobBidResponseModel>

    @POST(JOBASSIGN)
    suspend fun getJobAssignUserId(@Query("Id") Id: Int,
                                   @Query("AssignedUserId") assignedUserId: Int) : Response<Any>

    @POST(PROPERTYDETAILS)
    suspend fun getJobPropertyDetails(@Query("Id") userId: Int) : Response<PropertyDetailsModel>

    @POST(JOBPROPERTYUPDATESHOWN)
    suspend fun jobPropertyUpdateShown(@Body data: Map<String, String>) : Response<Any>

    @POST(MARKASPROPERTYSTATUS)
    suspend fun markJobPropertyStatus(@Query("Id") Id : Int,
                                      @Query("StatusMasterId") statusMasterId: Int) : Response<Any>

    @POST(CREATEJOB)
    suspend fun createJob(@Body jsonObject: RequestBody) : Response<CreateNewJobModel>

    @POST(JOBLANGUAGEAll)
    suspend fun getAllJobLang(@Body data: Map<String, String>) : Response<AllJobLanguageModel>

    @POST(AVAILABLEMAPLOCATION)
    suspend fun getMapLocation(@Query("userId") userId: Int, @Body data: Map<String, String>) : Response<ProfileMapLocationModel>

    @POST(USER_DOCUMENT_ALL)
    suspend fun getUserAllDocuments(@Query("userId") userId: Int,@Body data: Map<String, String>) : Response<UserAllDocumentsModel>

    @POST(USER_DOCUMENT_DELETE)
    suspend fun deleteUserDoc(@Query("Id") Id : Int, @Query("DeletedBy") deleteBy : Int) : Response<Any>

    @Multipart
    @POST(USER_DOCUMENT_INSERT)
    suspend fun uploadDocument(@Part("UserId") userId: Int,
                               @Part("DocumentMasterId") documentMasterId : Int,
                               @Part files: MultipartBody.Part): Response<UserDocumentInsertModel>

    @POST(USER_DOCUMENT_TYPE)
    suspend fun getDocumentType(@Body data: Map<String, String>) : Response<UserDocumentTypeModel>

    @POST(NOTIFICATION)
    suspend fun notification(@Query("UserId") userId: Int, @Body data: Map<String, String>) : Response<NotificationModel>

    @POST(SEARCHDATA)
    suspend fun searchNewJob(@Query("AvailableJobsOnly")AvailableJobsOnly : Boolean,
                             @Query("VisitDateFrom") VisitDateFrom : String,
                             @Query("VisitDateTo") VisitDateTo : String,
                             @Query("VisitTimeFrom") VisitTimeFrom : String,
                             @Query("VisitTimeTo") VisitTimeTo : String,
                            @Body data: Map<String, String>) : Response<AllAvalibleJobsModel>

    @POST(SEARCHDATA)
    suspend fun searchNewPostedJob(@Query("StatusMasterId") StatusMasterId : Int,
                             @Query("AvailableJobsOnly")AvailableJobsOnly : Boolean,
                             @Query("VisitDateFrom") VisitDateFrom : String,
                             @Query("VisitDateTo") VisitDateTo : String,
                             @Query("VisitTimeFrom") VisitTimeFrom : String,
                             @Query("VisitTimeTo") VisitTimeTo : String,
                             @Body data: Map<String, String>) : Response<AllAvalibleJobsModel>

    @POST(SEARCHDATA)
    suspend fun searchOngoingJob(@Query("StatusMasterId") StatusMasterId : Int,
                                 @Query("AvailableJobsOnly") availableJobs: Boolean,
                                 @Query("VisitDateFrom") VisitDateFrom : String,
                                 @Query("VisitDateTo") VisitDateTo : String,
                                 @Query("VisitTimeFrom") VisitTimeFrom : String,
                                 @Query("VisitTimeTo") VisitTimeTo : String,
                                @Body data: Map<String, String>) : Response<AllAssignedJobModel>

    @POST(SEARCHDATA)
    suspend fun searchPostedOngoingJob(@Query("UserId") UserId : Int,
                                 @Query("StatusMasterId") StatusMasterId : Int,
                                 @Query("AvailableJobsOnly") availableJobs: Boolean,
                                 @Query("VisitDateFrom") VisitDateFrom : String,
                                 @Query("VisitDateTo") VisitDateTo : String,
                                 @Query("VisitTimeFrom") VisitTimeFrom : String,
                                 @Query("VisitTimeTo") VisitTimeTo : String,
                                 @Body data: Map<String, String>) : Response<OngoingMyPostedJobModel>

    @POST(SEARCHDATA)
    suspend fun searchCompleteJob(@Query("UserId") UserId : Int,
                                  @Query("StatusMasterId")StatusMasterId : Int,
                                  @Query("AvailableJobsOnly") availableJobs: Boolean,
                                  @Query("VisitDateFrom") VisitDateFrom : String,
                                  @Query("VisitDateTo") VisitDateTo : String,
                                  @Query("VisitTimeFrom") VisitTimeFrom : String,
                                  @Query("VisitTimeTo") VisitTimeTo : String,
                                  @Body data: Map<String, String>) : Response<AllCompletedJobsModel>

    @POST(SEARCHDATA)
    suspend fun searchCompleteAssignJob(@Query("StatusMasterId")StatusMasterId : Int,
                                  @Query("AvailableJobsOnly") availableJobs: Boolean,
                                  @Query("VisitDateFrom") VisitDateFrom : String,
                                  @Query("VisitDateTo") VisitDateTo : String,
                                  @Query("VisitTimeFrom") VisitTimeFrom : String,
                                  @Query("VisitTimeTo") VisitTimeTo : String,
                                  @Body data: Map<String, String>) : Response<CompletedJobsAssignModel>

    @POST(PROPERTYMOREDETAILS)
    suspend fun propertyMoreDetailsAll(@Query("PropertyMasterId") PropertyMasterId : Int,
                                    @Body data: Map<String, String>) : Response<PropertyMoreDetailsModel>

    @POST(PROPERTYDETAILSTYPE)
    suspend fun propertyDetailsType(@Body data: Map<String, String>) : Response<PropertyMoreDetailsTypeModel>

    @POST(PROPERTYMOREDETAILSINSERT)
    suspend fun propertyMoreDetailsInsert(@Body data: Map<String, String>) : Response<Any>

    @POST(PROPERTYMOREDETAILSINSERT)
    suspend fun updatePropertyMoreDetails(@Body data: Map<String, String>) : Response<Any>

    @POST(PROPERTYMOREDETAILSDELETE)
    suspend fun deletePropertyMoreDetails(@Query("Id") Id : Int,
                                          @Query("DeletedBy") DeletedBy : Int) : Response<Any>

    @POST(PROPERTYIMAGEALL)
    suspend fun propertyImageAll(@Query("PropertyMasterId") PropertyMasterId : Int,
                                 @Body data: Map<String, String>) : Response<PropertyImageAllModel>

    @Multipart
    @POST(PROPERTYIMAGE)
    suspend fun propertyImageUpload(@Part("PropertyMasterId") PropertyMasterId : Int,
                                    @Part files: MultipartBody.Part) : Response<Any>

    @POST(PROPERTYIMAGEDELETE)
    suspend fun propertyImageDelete(@Query("Id") id : Int) : Response<Any>

//    @POST(PROPERTYIMAGEUPDATE)
//    suspend fun propertyImageUpdate(@Body data: Map<String, String>) : Response<Any>

    @POST(USERAGENTRATING)
    suspend fun userAgentRating(@Body data: Map<String, String>) : Response<Any>

    @POST(PROPERTY_ACTIVE_INACTIVE)
    suspend fun propertyActiveInActive(@Query("Id") Id: Int) : Response<Any>

}

object ServiceApi{
    val retrofitService : ApiKtService by lazy { retrofit.create(ApiKtService::class.java) }
}

private const val LOGIN = "users/Users_Login"
private const val SIGNUP = "users/Users_Upsert"
private const val GETUSERPROFILE = "users/Users_ById"
private const val UPDATEPROFILE = "users/Users_Upsert"
private const val CHANGE_PASSWORD = "users/Users_ChangePassword"
private const val GETUSERLANGUAGE = "userLanguages/UserLanguages_All"
private const val GETLANGUAGEUPDATE = "userLanguages/UserLanguages_Upsert"
private const val UPLOAD_PROFILE_PIC = "users/Users_ProfileUpdate"
private const val GETCUSTOMERS = "customerMaster/CustomerMaster_All"
private const val ADDCUSTOMER = "customerMaster/CustomerMaster_Upsert"
private const val UPDATECUSTOMER = "customerMaster/CustomerMaster_Upsert"
private const val GETPROPERTIES = "propertyMaster/PropertyMaster_All"
private const val ADDPROPERTY = "propertyMaster/PropertyMaster_Upsert"
private const val UPDATEPROPERTY = "propertyMaster/PropertyMaster_Upsert"
private const val GETPROPERTYDETAILS = "propertyMaster/PropertyMaster_ById"
private const val GETPROPERTYTYPE = "propertyTypeMaster/PropertyTypeMaster_All"
private const val GETAVALIBLEFOR = "availableForMaster/AvailableForMaster_All"
private const val PROPERTY_ACTIVE_INACTIVE = "propertyMaster/PropertyMaster_ActInAct"
private const val DELETEPROPERTY = "propertyMaster/PropertyMaster_Delete"
private const val GETCOUNTRY = "countryMaster/CountryMaster_All"
private const val GETSTATE = "stateMaster/StateMaster_All"
private const val GETCITY = "cityMaster/CityMaster_All"
private const val GETNEWAVAILABLEJOBS = "jobs/Jobs_All"
private const val GETNEWMYPOSTEDJOBS = "jobs/Jobs_All"
private const val GETONGOINGJOBS = "jobs/Jobs_All"
private const val GETCOMPLETEDJOBS = "jobs/Jobs_All"
private const val GETCOMPLETEDJOBSFORCompleted = "jobs/Jobs_All"
private const val GETONGOINGJOBSASSIGNED = "jobs/Jobs_All"
private const val GETJOBPROPERTY = "jobs/JobProperty_All"
private const val GETJOBBID = "jobPropertyBid/JobPropertyBid_Upsert"
private const val JOBPROPERTYBIDALL = "jobPropertyBid/JobPropertyBid_All"
private const val JOB_PROPERTY_ALL_BIDS = "jobPropertyBid/JobPropertyBid_AllDistinctAgent"
private const val JOBASSIGN = "jobs/Jobs_AssignedUserId"
private const val PROPERTYDETAILS = "propertyMaster/PropertyMaster_ById"
private const val JOBPROPERTYUPDATESHOWN = "jobs/JobProperty_UpdateAfterPropertyShown"
private const val MARKASPROPERTYSTATUS = "jobs/Jobs_UpdateStatus"
private const val CREATEJOB = "jobs/Jobs_Upsert"
private const val JOBLANGUAGEAll = "languageMaster/LanguageMaster_All"
private const val AVAILABLEMAPLOCATION = "userPreferedLocations/UserPreferedLocations_All"
private const val INSERT_LOCATION = "userPreferedLocations/UserPreferedLocations_Upsert"
private const val USER_DOCUMENT_ALL = "userDocuments/UserDocuments_All"
private const val USER_DOCUMENT_DELETE = "userDocuments/UserDocuments_Delete"
private const val USER_DOCUMENT_INSERT = "userDocuments/UserDocuments_Upsert"
private const val USER_DOCUMENT_TYPE = "documentMaster/DocumentMaster_All"
private const val NOTIFICATION = "userNotifications/UserNotifications_All"
private const val SEARCHDATA = "jobs/Jobs_All"
private const val PROPERTYMOREDETAILSINSERT = "propertyDetails/PropertyDetails_Upsert"
private const val PROPERTYMOREDETAILSDELETE = "propertyDetails/PropertyDetails_Delete"
private const val PROPERTYIMAGE = "propertyImage/PropertyImage_Upsert"
private const val PROPERTYIMAGEUPDATE = "propertyImage/PropertyImage_Update"
private const val PROPERTYIMAGEDELETE = "propertyImage/PropertyImage_Delete"
private const val PROPERTYMOREDETAILS = "propertyDetails/PropertyDetails_All"
private const val PROPERTYDETAILSTYPE = "propertyDetailMaster/PropertyDetailMaster_All"
private const val PROPERTYIMAGEALL = "propertyImage/PropertyImage_All"
private const val USERAGENTRATING = "userRatting/UserRatting_Upsert"
