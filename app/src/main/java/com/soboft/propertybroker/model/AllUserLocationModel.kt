package com.soboft.propertybroker.model


import com.fasterxml.jackson.annotation.JsonProperty

data class AllUserLocationModel(
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
    val values: List<UserLocation>?
)

data class UserLocation(
    @JsonProperty("CityId")
    val cityId: Int?,
    @JsonProperty("CityName")
    val cityName: String?,
    @JsonProperty("CountryId")
    val countryId: Int?,
    @JsonProperty("CountryName")
    val countryName: String?,
    @JsonProperty("CreatedDate")
    val createdDate: String?,
    @JsonProperty("CreatedDateStr")
    val createdDateStr: String?,
    @JsonProperty("Id")
    val id: Int?,
    @JsonProperty("Lat")
    val lat: String?,
    @JsonProperty("Long")
    val long: String?,
    @JsonProperty("StateId")
    val stateId: Int?,
    @JsonProperty("StateName")
    val stateName: String?,
    @JsonProperty("UserEmail")
    val userEmail: String?,
    @JsonProperty("UserFirstName")
    val userFirstName: String?,
    @JsonProperty("UserId")
    val userId: Int?,
    @JsonProperty("UserLastName")
    val userLastName: String?
)

