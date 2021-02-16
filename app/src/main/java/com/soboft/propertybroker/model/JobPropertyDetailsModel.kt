package com.soboft.propertybroker.model


import com.fasterxml.jackson.annotation.JsonProperty

data class JobPropertyDetailsModel(
    @JsonProperty("Code")
    val code: Int?,
    @JsonProperty("IsSuccessStatusCode")
    val isSuccessStatusCode: Boolean?,
    @JsonProperty("IsValid")
    val isValid: Boolean?,
    @JsonProperty("Item")
    val item: JobPropertyDetails?,
    @JsonProperty("Message")
    val message: String?
)

data class JobPropertyDetails(
    @JsonProperty("CreatedBy")
    val createdBy: Int?,
    @JsonProperty("CreatedDate")
    val createdDate: String?,
    @JsonProperty("CreatedDateStr")
    val createdDateStr: String?,
    @JsonProperty("DeletedBy")
    val deletedBy: Int?,
    @JsonProperty("DeletedDate")
    val deletedDate: String?,
    @JsonProperty("DeletedDateStr")
    val deletedDateStr: String?,
    @JsonProperty("Id")
    val id: Int?,
    @JsonProperty("IsAmenity")
    val isAmenity: Int?,
    @JsonProperty("PropertyDetailMasterId")
    val propertyDetailMasterId: Int?,
    @JsonProperty("PropertyDetailMasterName")
    val propertyDetailMasterName: String?,
    @JsonProperty("PropertyMasterId")
    val propertyMasterId: Int?,
    @JsonProperty("UpdatedBy")
    val updatedBy: Int?,
    @JsonProperty("UpdatedDate")
    val updatedDate: String?,
    @JsonProperty("UpdatedDateStr")
    val updatedDateStr: String?,
    @JsonProperty("Value")
    val value: String?
)