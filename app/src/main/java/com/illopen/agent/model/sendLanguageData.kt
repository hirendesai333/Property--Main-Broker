package com.illopen.agent.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SendLanguageData(
    @SerializedName("Id")
    val Id: Int,
    @SerializedName("UserId")
    val UserId: Int,
    @SerializedName("LanguageMasterId")
    val LanguageMasterId: Int,
    @SerializedName("LanguageName")
    val LanguageName : String,
    @SerializedName("UserName")
    var UserName: String
): Serializable
