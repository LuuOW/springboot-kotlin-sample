package com.cropyback.cropyback.database.entities.auth

import com.cropyback.cropyback.database.entities.user.UserEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


data class SignUpRequest(
    val username: String,
    val email: String,
    val password: String
) {
    fun toUser(passwordEncoder: BCryptPasswordEncoder) = UserEntity(
        username = username,
        email = email,
        password = passwordEncoder.encode(password)
    )
}