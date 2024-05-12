package com.cropyback.cropyback.database.entities.auth

data class LoginRequest(
    val username: String,
    val password: String
)