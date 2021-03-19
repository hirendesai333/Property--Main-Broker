package com.illopen.agent.model


import com.fasterxml.jackson.annotation.JsonProperty

data class JobAssignedModel(
    @JsonProperty("Code")
    val code: Int?,
    @JsonProperty("IsSuccessStatusCode")
    val isSuccessStatusCode: Boolean?,
    @JsonProperty("IsValid")
    val isValid: Boolean?,
    @JsonProperty("Item")
    val item: JobAssign?,
    @JsonProperty("Message")
    val message: String?
)

data class JobAssign(
    @JsonProperty("AgentCodePhone")
    val agentCodePhone: String?,
    @JsonProperty("AssignedCountryCode")
    val assignedCountryCode: Any?,
    @JsonProperty("AssignedPhoneNumber")
    val assignedPhoneNumber: String?,
    @JsonProperty("AssignedProfileUrl")
    val assignedProfileUrl: Any?,
    @JsonProperty("AssignedProfileUrlStr")
    val assignedProfileUrlStr: String?,
    @JsonProperty("AssignedUserEmail")
    val assignedUserEmail: String?,
    @JsonProperty("AssignedUserId")
    val assignedUserId: Int?,
    @JsonProperty("AssignedUserName")
    val assignedUserName: String?,
    @JsonProperty("AvailableForMasterId")
    val availableForMasterId: Int?,
    @JsonProperty("AvailableJobsOnly")
    val availableJobsOnly: Boolean?,
    @JsonProperty("AvailalityName")
    val availalityName: Any?,
    @JsonProperty("AverageRatting")
    val averageRatting: Int?,
    @JsonProperty("BidAmount")
    val bidAmount: Int?,
    @JsonProperty("BidNotes")
    val bidNotes: Any?,
    @JsonProperty("CityId")
    val cityId: Int?,
    @JsonProperty("CityName")
    val cityName: Any?,
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
    @JsonProperty("CustomerCountryCode")
    val customerCountryCode: Int?,
    @JsonProperty("CustomerEmail")
    val customerEmail: Any?,
    @JsonProperty("CustomerMasterId")
    val customerMasterId: Int?,
    @JsonProperty("CustomerName")
    val customerName: Any?,
    @JsonProperty("CustomerPhoneNumber")
    val customerPhoneNumber: Any?,
    @JsonProperty("DeletedBy")
    val deletedBy: Int?,
    @JsonProperty("DeletedDate")
    val deletedDate: String?,
    @JsonProperty("DeletedDateStr")
    val deletedDateStr: String?,
    @JsonProperty("FromVisitingDate")
    val fromVisitingDate: Any?,
    @JsonProperty("FromVisitingTime")
    val fromVisitingTime: Any?,
    @JsonProperty("HighestBid")
    val highestBid: Int?,
    @JsonProperty("Id")
    val id: Int?,
    @JsonProperty("JobBidId")
    val jobBidId: Int?,
    @JsonProperty("JobLanguages")
    val jobLanguages: String?,
    @JsonProperty("JobNo")
    val jobNo: String?,
    @JsonProperty("jobProperty")
    val jobProperty: List<Any>?,
    @JsonProperty("JobRatting")
    val jobRatting: Int?,
    @JsonProperty("JobReview")
    val jobReview: Any?,
    @JsonProperty("JobVisitingDate")
    val jobVisitingDate: String?,
    @JsonProperty("JobVisitingTime")
    val jobVisitingTime: String?,
    @JsonProperty("LowestBid")
    val lowestBid: Int?,
    @JsonProperty("ProperTypeMasterName")
    val properTypeMasterName: Any?,
    @JsonProperty("PropertyAddress")
    val propertyAddress: Any?,
    @JsonProperty("PropertyLatitude")
    val propertyLatitude: Any?,
    @JsonProperty("PropertyLongitude")
    val propertyLongitude: Any?,
    @JsonProperty("PropertyMasterId")
    val propertyMasterId: Int?,
    @JsonProperty("PropertyName")
    val propertyName: Any?,
    @JsonProperty("PropertyNotes")
    val propertyNotes: Any?,
    @JsonProperty("PropertyPrice")
    val propertyPrice: Int?,
    @JsonProperty("PropertyTypeMasterId")
    val propertyTypeMasterId: Int?,
    @JsonProperty("Remarks")
    val remarks: String?,
    @JsonProperty("StateId")
    val stateId: Int?,
    @JsonProperty("StateName")
    val stateName: Any?,
    @JsonProperty("StatusMasterId")
    val statusMasterId: Int?,
    @JsonProperty("StatusName")
    val statusName: Any?,
    @JsonProperty("SubAgentCodePhone")
    val subAgentCodePhone: String?,
    @JsonProperty("ToVisitingDate")
    val toVisitingDate: Any?,
    @JsonProperty("ToVisitingTime")
    val toVisitingTime: Any?,
    @JsonProperty("TotalBids")
    val totalBids: Int?,
    @JsonProperty("TotalNoOfRatings")
    val totalNoOfRatings: Int?,
    @JsonProperty("TotalProperty")
    val totalProperty: Int?,
    @JsonProperty("UpdatedBy")
    val updatedBy: Int?,
    @JsonProperty("UpdatedDate")
    val updatedDate: String?,
    @JsonProperty("UpdatedDateStr")
    val updatedDateStr: String?,
    @JsonProperty("UserCountryCode")
    val userCountryCode: Any?,
    @JsonProperty("UserEmail")
    val userEmail: Any?,
    @JsonProperty("UserId")
    val userId: Int?,
    @JsonProperty("UserName")
    val userName: Any?,
    @JsonProperty("UserPhoneNumber")
    val userPhoneNumber: Any?,
    @JsonProperty("UserProfileUrl")
    val userProfileUrl: Any?,
    @JsonProperty("UserProfileUrlStr")
    val userProfileUrlStr: String?,
    @JsonProperty("VisitingDate")
    val visitingDate: Any?,
    @JsonProperty("VistingTime")
    val vistingTime: Any?
)
