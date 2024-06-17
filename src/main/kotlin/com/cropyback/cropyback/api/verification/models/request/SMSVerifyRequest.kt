package com.cropyback.cropyback.api.verification.models.request

data class SMSVerifyRequest(
    val phone: String,
    val code: String
)
