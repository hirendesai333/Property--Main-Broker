package com.illopen.agent.model


import com.fasterxml.jackson.annotation.JsonProperty

data class NotificationModel(
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
    val values: List<NotificationList>?
)

data class NotificationList(
    @JsonProperty("CreatedDate")
    val createdDate: String?,
    @JsonProperty("CreatedDateStr")
    val createdDateStr: String?,
    @JsonProperty("Email")
    val email: String?,
    @JsonProperty("Id")
    val id: Int?,
    @JsonProperty("IsRead")
    val isRead: Int?,
    @JsonProperty("Notification")
    val notification: String?,
    @JsonProperty("NotificationsDate")
    val notificationsDate: Any?,
    @JsonProperty("UserId")
    val userId: Int?,
    @JsonProperty("UserName")
    val userName: String?
)