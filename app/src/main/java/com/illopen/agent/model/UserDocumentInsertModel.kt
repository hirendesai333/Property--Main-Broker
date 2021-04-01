package com.illopen.agent.model


import com.fasterxml.jackson.annotation.JsonProperty

data class UserDocumentInsertModel(
    @JsonProperty("Code")
    val code: Int?,
    @JsonProperty("IsSuccessStatusCode")
    val isSuccessStatusCode: Boolean?,
    @JsonProperty("IsValid")
    val isValid: Boolean?,
    @JsonProperty("Item")
    val item: DocumentItem?,
    @JsonProperty("Message")
    val message: String?
)

data class DocumentItem(
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
    val documentName: Any?,
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

