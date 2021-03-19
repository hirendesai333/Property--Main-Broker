package com.illopen.agent.model

import com.fasterxml.jackson.annotation.JsonProperty

data class AddCustomerModel(
    @JsonProperty("Code")
    val code: Int?,
    @JsonProperty("IsSuccessStatusCode")
    val isSuccessStatusCode: Boolean?,
    @JsonProperty("IsValid")
    val isValid: Boolean?,
    @JsonProperty("Item")
    val item: Items?,
    @JsonProperty("Message")
    val message: String?
)

data class Items(
    @JsonProperty("CountryCode")
    val countryCode: Any?,
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
    val phoneNumber: Any?,
    @JsonProperty("UpdatedBy")
    val updatedBy: Int?,
    @JsonProperty("UpdatedDate")
    val updatedDate: String?,
    @JsonProperty("UpdatedDateStr")
    val updatedDateStr: String?,
    @JsonProperty("UserId")
    val userId: Int?
)