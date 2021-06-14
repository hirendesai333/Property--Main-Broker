package com.illopen.agent.model


import com.google.gson.annotations.SerializedName

data class PropertyActiveInActiveModel(
    @SerializedName("Code")
    val code: Int?,
    @SerializedName("IsSuccessStatusCode")
    val isSuccessStatusCode: Boolean?,
    @SerializedName("IsValid")
    val isValid: Boolean?,
    @SerializedName("Item")
    val item: PropertyItem?,
    @SerializedName("Message")
    val message: String?
)

data class PropertyItem(
    @SerializedName("AvailableFor")
    val availableFor: Any?,
    @SerializedName("AvailableForMasterId")
    val availableForMasterId: Int?,
    @SerializedName("CityId")
    val cityId: Int?,
    @SerializedName("CityName")
    val cityName: Any?,
    @SerializedName("CountryId")
    val countryId: Int?,
    @SerializedName("CountryName")
    val countryName: Any?,
    @SerializedName("CreatedBy")
    val createdBy: Int?,
    @SerializedName("CreatedDate")
    val createdDate: String?,
    @SerializedName("CreatedDateStr")
    val createdDateStr: String?,
    @SerializedName("DeletedBy")
    val deletedBy: Int?,
    @SerializedName("DeletedDate")
    val deletedDate: String?,
    @SerializedName("DeletedDateStr")
    val deletedDateStr: String?,
    @SerializedName("FullName")
    val fullName: String?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("IsActive")
    val isActive: Int?,
    @SerializedName("IsDeleted")
    val isDeleted: Boolean?,
    @SerializedName("Pincode")
    val pincode: String?,
    @SerializedName("PropertyAddress")
    val propertyAddress: String?,
    @SerializedName("PropertyLatitude")
    val propertyLatitude: String?,
    @SerializedName("PropertyLongitude")
    val propertyLongitude: String?,
    @SerializedName("PropertyName")
    val propertyName: String?,
    @SerializedName("PropertyNotes")
    val propertyNotes: String?,
    @SerializedName("PropertyPrice")
    val propertyPrice: String?,
    @SerializedName("PropertyType")
    val propertyType: Any?,
    @SerializedName("PropertyTypeMasterId")
    val propertyTypeMasterId: Int?,
    @SerializedName("StateId")
    val stateId: Int?,
    @SerializedName("StateName")
    val stateName: Any?,
    @SerializedName("UpdatedBy")
    val updatedBy: Int?,
    @SerializedName("UpdatedDate")
    val updatedDate: String?,
    @SerializedName("UpdatedDateStr")
    val updatedDateStr: String?,
    @SerializedName("UserId")
    val userId: Int?
)