package com.illopen.agent.model


import com.fasterxml.jackson.annotation.JsonProperty

data class AllUserLanguageModel(
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
    val values: ArrayList<UserLanguage>?
)

data class UserLanguage(
    @JsonProperty("Id")
    val id: Int?,
    @JsonProperty("LanguageMasterId")
    val languageMasterId: Int?,
    @JsonProperty("LanguageName")
    val languageName: String?,
    @JsonProperty("UserId")
    val userId: Int?,
    @JsonProperty("UserName")
    val userName: String?
)