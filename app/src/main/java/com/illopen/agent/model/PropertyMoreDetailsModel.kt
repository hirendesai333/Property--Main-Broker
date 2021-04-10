package com.illopen.agent.model


import com.fasterxml.jackson.annotation.JsonProperty

data class PropertyMoreDetailsModel(
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
    val values: List<PropertyMoreDetailsList>?
)

data class PropertyMoreDetailsList(
    @JsonProperty("Id")
    val id: Int?,
    @JsonProperty("IsAmenity")
    val isAmenity: Int?,
    @JsonProperty("Name")
    val name: String?
)