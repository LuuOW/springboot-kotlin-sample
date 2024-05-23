package com.cropyback.cropyback.repository

import com.cropyback.cropyback.database.entities.user.UserEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.security.core.userdetails.UserDetails

interface UserRepository: MongoRepository<UserEntity, String> {

}