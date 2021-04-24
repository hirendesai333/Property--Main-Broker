package com.illopen.agent.model


import com.fasterxml.jackson.annotation.JsonProperty

data class ProfileMapLocationModel(
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
    val values: List<ProfileMapList>?
)

data class ProfileMapList(
    @JsonProperty("CityMasterId")
    val cityMasterId: Int?,
    @JsonProperty("CityName")
    val cityName: Any?,
    @JsonProperty("FirstName")
    val firstName: Any?,
    @JsonProperty("Id")
    val id: Int?,
    @JsonProperty("LastName")
    val lastName: Any?,
    @JsonProperty("Latitude")
    val latitude: String?,
    @JsonProperty("LocationName")
    val locationName: Any?,
    @JsonProperty("Longitude")
    val longitude: String?,
    @JsonProperty("Radius")
    val radius: String?,
    @JsonProperty("UserId")
    val userId: Int?
)