package com.illopen.agent.model


import com.fasterxml.jackson.annotation.JsonProperty

data class UserAllDocumentsModel(
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
    val values: List<UserDocumentList>?
)

data class UserDocumentList(
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
    @JsonProperty("DocumentMasterId")
    val documentMasterId: Int?,
    @JsonProperty("DocumentName")
    val documentName: String?,
    @JsonProperty("Id")
    val id: Int?,
    @JsonProperty("IsApproved")
    val isApproved: Int?,
    @JsonProperty("UpdatedBy")
    val updatedBy: Int?,
    @JsonProperty("UpdatedDate")
    val updatedDate: String?,
    @JsonProperty("UpdatedDateStr")
    val updatedDateStr: String?,
    @JsonProperty("Url")
    val url: String?,
    @JsonProperty("UrlStr")
    val urlStr: String?,
    @JsonProperty("UserId")
    val userId: Int?
)