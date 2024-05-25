package com.cropyback.cropyback.api.verification.mail

data class MailCredentials(
    val senderEmail: String,
    val senderPassword: String,
    val smtpHost: String,
    val smtpPort: Int
)