package com.cropyback.cropyback.api.ui.models

import com.google.gson.annotations.SerializedName

data class LoginUI(
    val id: Int,
    @field:SerializedName("login_title") val loginTitle: String,
    @field:SerializedName("login_description") val loginDescription: String,
    @field:SerializedName("login_image") val loginImage: String = "",
    @field:SerializedName("login_primary_color") val primaryColor: String,
    @field:SerializedName("login_secondary_color") val secondaryColor: String,
)

data class DefaultColors(
    @field:SerializedName("default_primary") val primaryColor: String,
    @field:SerializedName("default_secondary") val secondaryColor: String,
    @field:SerializedName("default_text") val textColor: String
)

data class SignUpUI(
    val id: Int,
    @field:SerializedName("sign_up_title") val loginTitle: String,
    @field:SerializedName("sign_up_description") val loginDescription: String,
    @field:SerializedName("sign_up_image") val loginImage: String = "",
    @field:SerializedName("sign_up_primary_color") val primaryColor: String,
    @field:SerializedName("sign_up_secondary_color") val secondaryColor: String
)