package com.cropyback.cropyback.database.sql.service

import com.cropyback.cropyback.database.sql.jpa.UserJpaRepository
import com.cropyback.cropyback.database.sql.user.UserModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserJpaService: UserDetailsService {

    @Autowired
    private lateinit var userJpaRepository: UserJpaRepository

    fun registerUser(user: UserModel): UserModel {
        return userJpaRepository.save(user)
    }

    fun userExist(user: UserModel): Boolean {
        return  userJpaRepository.existsByPhone(user.phone)
    }

    fun phoneExist(phone: String): Boolean {
        return userJpaRepository.existsByPhone(phone)
    }

    fun findRoleByUsername(username: String): String {
        return userJpaRepository.findUserByUsername(username).role
    }

    fun findUserByUsername(username: String): UserModel {
        return userJpaRepository.findUserByUsername(username)
    }

    fun updateUser(user: UserModel) {
        userJpaRepository.save(user)
    }

    fun getAllUsers(): MutableList<UserModel> {
        return userJpaRepository.findAll()
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userJpaRepository.findUserByUsername(username)
        val userDetails = User.builder()
            .username(user.username)
            .password(user.password)
            .roles(user.role)
            .build()
        return userDetails
    }

}