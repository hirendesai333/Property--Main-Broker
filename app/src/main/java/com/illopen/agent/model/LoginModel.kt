package com.illopen.agent.model


import com.fasterxml.jackson.annotation.JsonProperty

data class LoginModel(
    @JsonProperty("Code")
    val code: Int?,
    @JsonProperty("IsSuccessStatusCode")
    val isSuccessStatusCode: Boolean?,
    @JsonProperty("IsValid")
    val isValid: Boolean?,
    @JsonProperty("Item")
    val item: Item?,
    @JsonProperty("Message")
    val message: String?
)

data class Item(
    @JsonProperty("Address")
    val address: Any?,
    @JsonProperty("AverageRatting")
    val averageRatting: Double?,
    @JsonProperty("CompanyName")
    val companyName: String?,
    @JsonProperty("ConfirmPassword")
    val confirmPassword: Any?,
    @JsonProperty("CountryCode")
    val countryCode: Any?,
    @JsonProperty("CountryId")
    val countryId: Int?,
    @JsonProperty("CountryName")
    val countryName: Any?,
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
    @JsonProperty("DeviceToken")
    val deviceToken: String?,
    @JsonProperty("DeviceTokenUpdate")
    val deviceTokenUpdate: Boolean?,
    @JsonProperty("Email")
    val email: String?,
    @JsonProperty("FirstName")
    val firstName: String?,
    @JsonProperty("FullName")
    val fullName: Any?,
    @JsonProperty("HearAboutUs")
    val hearAboutUs: String?,
    @JsonProperty("Id")
    val id: Int?,
    @JsonProperty("IsActive")
    val isActive: Int?,
    @JsonProperty("IsApproved")
    val isApproved: Int?,
    @JsonProperty("IsAvailable")
    val isAvailable: Int?,
    @JsonProperty("IsResetPassword")
    val isResetPassword: Boolean?,
    @JsonProperty("LastLogin")
    val lastLogin: String?,
    @JsonProperty("LastLoginStr")
    val lastLoginStr: String?,
    @JsonProperty("LastName")
    val lastName: String?,
    @JsonProperty("Latitude")
    val latitude: Any?,
    @JsonProperty("LoginType")
    val loginType: Boolean?,
    @JsonProperty("Longitude")
    val longitude: Any?,
    @JsonProperty("NewPassword")
    val newPassword: Any?,
    @JsonProperty("OldPassword")
    val oldPassword: Any?,
    @JsonProperty("Password")
    val password: String?,
    @JsonProperty("PhoneNumber")
    val phoneNumber: String?,
    @JsonProperty("ProfileUrl")
    val profileUrl: String?,
    @JsonProperty("ProfileUrlStr")
    val profileUrlStr: String?,
    @JsonProperty("TotalRatting")
    val totalRatting: Double?,
    @JsonProperty("TotalUsers")
    val totalUsers: Double?,
    @JsonProperty("UpdatedBy")
    val updatedBy: Int?,
    @JsonProperty("UpdatedDate")
    val updatedDate: String?,
    @JsonProperty("UpdatedDateStr")
    val updatedDateStr: String?,
    @JsonProperty("UserTypeMasterId")
    val userTypeMasterId: Int?,
    @JsonProperty("UserTypeName")
    val userTypeName: Any?
)