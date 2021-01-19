package com.soboft.propertybroker.model


import com.fasterxml.jackson.annotation.JsonProperty

data class AllCustomerModel(
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
    val values: List<Value>?
)

data class Value(
    @JsonProperty("CountryCode")
    val countryCode: String?,
    @JsonProperty("CreatedBy")
    val createdBy: Int?,
    @JsonProperty("CreatedDate")
    val createdDate: String?,
    @JsonProperty("CreatedDateStr")
    val createdDateStr: String?,
    @JsonProperty("CustomerCountryCode")
    val customerCountryCode: Int?,
    @JsonProperty("CustomerEmail")
    val customerEmail: String?,
    @JsonProperty("CustomerName")
    val customerName: String?,
    @JsonProperty("CustomerPhoneNumber")
    val customerPhoneNumber: String?,
    @JsonProperty("Id")
    val id: Int?,
    @JsonProperty("PhoneNumber")
    val phoneNumber: String?,
    @JsonProperty("UpdatedBy")
    val updatedBy: Int?,
    @JsonProperty("UpdatedDate")
    val updatedDate: String?,
    @JsonProperty("UpdatedDateStr")
    val updatedDateStr: String?,
    @JsonProperty("UserId")
    val userId: Int?
)