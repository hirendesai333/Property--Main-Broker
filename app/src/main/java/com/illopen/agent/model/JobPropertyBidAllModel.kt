package com.illopen.agent.model


import com.fasterxml.jackson.annotation.JsonProperty

data class JobPropertyBidAllModel(
    @JsonProperty("IsOffsetProvided")
    val isOffsetProvided: Boolean?,
    @JsonProperty("IsPageProvided")
    val isPageProvided: Boolean?,
    @JsonProperty("Limit")
    val limit: Int?,
    @JsonProperty("Offset")
    val offset: Int?,
    @JsonProperty("Page")
    val page: Int?,
    @JsonProperty("PageSize")
    val pageSize: Int?,
    @JsonProperty("SortBy")
    val sortBy: String?,
    @JsonProperty("SortDirection")
    val sortDirection: Any?,
    @JsonProperty("TotalCount")
    val totalCount: Int?,
    @JsonProperty("TotalRecords")
    val totalRecords: Int?,
    @JsonProperty("Values")
    val values: List<JobPropertyBidAllList>?
)

data class JobPropertyBidAllList(
    @JsonProperty("Amount")
    val amount: Int?,
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
    val totalAmount: Int?,
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
    val userAverageRating: Int?,
    @JsonProperty("UserEmail")
    val userEmail: Any?,
    @JsonProperty("UserId")
    val userId: Int?,
    @JsonProperty("UserName")
    val userName: Any?,
    @JsonProperty("UserPhoneNumber")
    val userPhoneNumber: Any?,
    @JsonProperty("UserProfileUrl")
    val userProfileUrl: Any?,
    @JsonProperty("UserProfileUrlStr")
    val userProfileUrlStr: String?,
    @JsonProperty("UserTotalRating")
    val userTotalRating: Int?
)