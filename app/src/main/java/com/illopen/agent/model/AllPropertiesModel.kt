package com.illopen.agent.model


import com.fasterxml.jackson.annotation.JsonProperty

data class AllPropertiesModel(
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
    val values: List<AllPropertiesList>?
)

data class AllPropertiesList(
    @JsonProperty("AvailableFor")
    val availableFor: String?,
    @JsonProperty("AvailableForMasterId")
    val availableForMasterId: Int?,
    @JsonProperty("CityId")
    val cityId: Int?,
    @JsonProperty("CityName")
    val cityName: String?,
    @JsonProperty("CountryId")
    val countryId: Int?,
    @JsonProperty("CountryName")
    val countryName: String?,
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
    @JsonProperty("FullName")
    val fullName: String?,
    @JsonProperty("Id")
    val id: Int?,
    @JsonProperty("IsActive")
    val isActive: Int?,
    @JsonProperty("IsDeleted")
    val isDeleted: Boolean?,
    @JsonProperty("Pincode")
    val pincode: String?,
    @JsonProperty("PropertyAddress")
    val propertyAddress: String?,
    @JsonProperty("PropertyLatitude")
    val propertyLatitude: Any?,
    @JsonProperty("PropertyLongitude")
    val propertyLongitude: Any?,
    @JsonProperty("PropertyName")
    val propertyName: String?,
    @JsonProperty("PropertyNotes")
    val propertyNotes: Any?,
    @JsonProperty("PropertyPrice")
    val propertyPrice: String?,
    @JsonProperty("PropertyType")
    val propertyType: String?,
    @JsonProperty("PropertyTypeMasterId")
    val propertyTypeMasterId: Int?,
    @JsonProperty("StateId")
    val stateId: Int?,
    @JsonProperty("StateName")
    val stateName: String?,
    @JsonProperty("UpdatedBy")
    val updatedBy: Int?,
    @JsonProperty("UpdatedDate")
    val updatedDate: String?,
    @JsonProperty("UpdatedDateStr")
    val updatedDateStr: String?,
    @JsonProperty("UserId")
    val userId: Int?
)