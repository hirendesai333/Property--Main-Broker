package com.illopen.agent.model

data class SelectedLanguageModel(
    val Id: Int?,
    val UserId: Int?,
    val LanguageMasterId: Int?,
    val LanguageName: String?,
    var UserName: String?
)


data class SelectedUserLanguageModel(
    val Id: Int?,
    val UserId: Int?,
    val LanguageMasterId: Int?,
    val LanguageName: String?,
    var UserName: String?,
    var isSelected: Boolean?
)

