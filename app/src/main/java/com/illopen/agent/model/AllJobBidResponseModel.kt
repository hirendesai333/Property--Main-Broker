package com.illopen.agent.model


import com.fasterxml.jackson.annotation.JsonProperty

data class AllJobBidResponseModel(
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
    val values: List<JobBidList>?
)

data class JobBidList(
    @JsonProperty("Amount")
    val amount: Int?,
    @JsonProperty("AvailableFor")
    val availableFor: Any?,
    @JsonProperty("CityName")
    val cityName: Any?,
    @JsonProperty("CountryName")
    val countryName: Any?,
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
    val jobCreateUserEmail: Any?,
    @JsonProperty("JobCreateUserName")
    val jobCreateUserName: Any?,
    @JsonProperty("JobId")
    val jobId: Int?,
    @JsonProperty("JobNo")
    val jobNo: Any?,
    @JsonProperty("JobPropertyId")
    val jobPropertyId: Int?,
    @JsonProperty("Note")
    val note: String?,
    @JsonProperty("Pincode")
    val pincode: Any?,
    @JsonProperty("PropertyAddress")
    val propertyAddress: Any?,
    @JsonProperty("PropertyLatitude")
    val propertyLatitude: Any?,
    @JsonProperty("PropertyLongitude")
    val propertyLongitude: Any?,
    @JsonProperty("PropertyMasterId")
    val propertyMasterId: Int?,
    @JsonProperty("PropertyName")
    val propertyName: Any?,
    @JsonProperty("PropertyPrice")
    val propertyPrice: Any?,
    @JsonProperty("PropertyType")
    val propertyType: Any?,
    @JsonProperty("StateName")
    val stateName: Any?,
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