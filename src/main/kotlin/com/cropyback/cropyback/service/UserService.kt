package com.cropyback.cropyback.service

import com.cropyback.cropyback.database.entities.user.UserEntity
import com.cropyback.cropyback.database.entities.auth.SignUpRequest
import com.cropyback.cropyback.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
): UserDetailsService {

    private val passwordEncoder = BCryptPasswordEncoder()

    override fun loadUserByUsername(username: String?): UserDetails {
        val user = getAllUsers().find { it.username == username }
        val userDetails = User.builder()
            .username(user?.username)
            .password(user?.password)
            .roles("USER")
            .build()
        return userDetails
    }


    fun saveUser(signUpReq: SignUpRequest) {
        val newUser = signUpReq.toUser(passwordEncoder)
        userRepository.save(
            UserEntity(
            username = newUser.username,
            email = newUser.email,
            password = newUser.password,
            role = "USER"
        )
        )
    }

    fun getAllUsers(): List<UserEntity> {
        return userRepository.findAll()
    }


}