package com.illopen.agent.model


import com.fasterxml.jackson.annotation.JsonProperty

data class JobPropertyModel(
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
    val values: List<JobPropertyList>?
)

data class JobPropertyList(
    @JsonProperty("AvailableForMasterId")
    val availableForMasterId: Int?,
    @JsonProperty("AvailableForMasterName")
    val availableForMasterName: String?,
    @JsonProperty("BidAmount")
    val bidAmount: Double?,
    @JsonProperty("BidNote")
    val bidNote: String?,
    @JsonProperty("City")
    val city: Any?,
    @JsonProperty("CityId")
    val cityId: Int?,
    @JsonProperty("Country")
    val country: String?,
    @JsonProperty("CountryId")
    val countryId: Int?,
    @JsonProperty("CreatedBy")
    val createdBy: Int?,
    @JsonProperty("CreatedDate")
    val createdDate: String?,
    @JsonProperty("CreatedDateStr")
    val createdDateStr: String?,
    @JsonProperty("Id")
    val id: Int?,
    @JsonProperty("JobId")
    val jobId: Int?,
    @JsonProperty("JobPropertyBidId")
    val jobPropertyBidId: Int?,
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
    @JsonProperty("PropertyTypeMasterId")
    val propertyTypeMasterId: Int?,
    @JsonProperty("PropertyTypeName")
    val propertyTypeName: String?,
    @JsonProperty("Rating")
    val rating: Double?,
    @JsonProperty("RatingBy")
    val ratingBy: Int?,
    @JsonProperty("Review")
    val review: String?,
    @JsonProperty("State")
    val state: Any?,
    @JsonProperty("StateId")
    val stateId: Int?,
    @JsonProperty("UpdatedBy")
    val updatedBy: Int?,
    @JsonProperty("UpdatedDate")
    val updatedDate: String?,
    @JsonProperty("UpdatedDateStr")
    val updatedDateStr: String?,
    @JsonProperty("UserId")
    val userId: Int?
)