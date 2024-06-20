package com.cropyback.cropyback.repository

import com.cropyback.cropyback.database.entities.user.UserEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Repository

interface UserRepository: MongoRepository<UserEntity, String> {
    fun findByEmail(email: String): UserEntity?

}