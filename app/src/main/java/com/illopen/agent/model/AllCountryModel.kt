package com.illopen.agent.model


import com.fasterxml.jackson.annotation.JsonProperty

data class AllCountryModel(
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
    val values: List<Country>?
)

data class Country(
    @JsonProperty("Code")
    val code: String?,
    @JsonProperty("Country")
    val country: String?,
    @JsonProperty("Currency")
    val currency: String?,
    @JsonProperty("Id")
    val id: Int?
)

