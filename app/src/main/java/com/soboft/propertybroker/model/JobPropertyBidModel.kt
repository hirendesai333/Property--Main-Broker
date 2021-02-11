package com.soboft.propertybroker.model


import com.fasterxml.jackson.annotation.JsonProperty

data class JobPropertyBidModel(
    @JsonProperty("Code")
    val code: Int?,
    @JsonProperty("IsSuccessStatusCode")
    val isSuccessStatusCode: Boolean?,
    @JsonProperty("IsValid")
    val isValid: Boolean?,
    @JsonProperty("Item")
    val item: JobPropertyBidList?,
    @JsonProperty("Message")
    val message: String?
)

data class JobPropertyBidList(
    @JsonProperty("Amount")
    val amount: Double?,
    @JsonProperty("AvailableFor")
    val availableFor: String?,
    @JsonProperty("CityName")
    val cityName: String?,
    @JsonProperty("CountryName")
    val countryName: String?,
    @JsonProperty("CreatedBy")
    val createdBy: Int?,
    @JsonProperty("CreatedDate")
    val createdDate: String?,
    @JsonProperty("CreatedDateStr")
    val createdDateStr: String?,
    @JsonProperty("HouseNo")
    val houseNo: Any?,
    @JsonProperty("Id")
    val id: Int?,
    @JsonProperty("JobCreateUserEmail")
    val jobCreateUserEmail: String?,
    @JsonProperty("JobCreateUserName")
    val jobCreateUserName: String?,
    @JsonProperty("JobId")
    val jobId: Int?,
    @JsonProperty("JobNo")
    val jobNo: String?,
    @JsonProperty("JobPropertyId")
    val jobPropertyId: Int?,
    @JsonProperty("Note")
    val note: String?,
    @JsonProperty("Pincode")
    val pincode: String?,
    @JsonProperty("PropertyAddress")
    val propertyAddress: String?,
    @JsonProperty("PropertyLatitude")
    val propertyLatitude: String?,
    @JsonProperty("PropertyLongitude")
    val propertyLongitude: String?,
    @JsonProperty("PropertyMasterId")
    val propertyMasterId: Int?,
    @JsonProperty("PropertyName")
    val propertyName: String?,
    @JsonProperty("PropertyPrice")
    val propertyPrice: String?,
    @JsonProperty("PropertyType")
    val propertyType: String?,
    @JsonProperty("StateName")
    val stateName: String?,
    @JsonProperty("TotalAmount")
    val totalAmount: Double?,
    @JsonProperty("TotalProperty")
    val totalProperty: Int?,
    @JsonProperty("TotalPropertyBid")
    val totalPropertyBid: Int?,
    @JsonProperty("UpdatedBy")
    val updatedBy: Int?,
    @JsonProperty("UpdatedDate")
    val updatedDate: String?,
    @JsonProperty("UpdatedDateStr")
    val updatedDateStr: String?,
    @JsonProperty("UserAverageRating")
    val userAverageRating: Double?,
    @JsonProperty("UserEmail")
    val userEmail: String?,
    @JsonProperty("UserId")
    val userId: Int?,
    @JsonProperty("UserName")
    val userName: String?,
    @JsonProperty("UserPhoneNumber")
    val userPhoneNumber: String?,
    @JsonProperty("UserProfileUrl")
    val userProfileUrl: String?,
    @JsonProperty("UserProfileUrlStr")
    val userProfileUrlStr: String?,
    @JsonProperty("UserTotalRating")
    val userTotalRating: Int?
)

