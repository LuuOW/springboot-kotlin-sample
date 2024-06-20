package com.cropyback.cropyback.database.entities.auth

import com.cropyback.cropyback.database.entities.user.UserEntity
import com.cropyback.cropyback.database.sql.user.UserModel
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


data class SignUpRequest(
    val username: String,
    val email: String,
    val phone: String,
    val password: String,
    val role: String
) {

    private val passwordEncoder = BCryptPasswordEncoder()

    fun toUser(passwordEncoder: BCryptPasswordEncoder) = UserEntity(
        username = username,
        email = email,
        phone = phone,
        password = passwordEncoder.encode(password),
        role = role
    )

    fun toUserModel() = UserModel(
    username = username,
    email = email,
    phone = phone,
    password = passwordEncoder.encode(password),
    role = role
    )

}