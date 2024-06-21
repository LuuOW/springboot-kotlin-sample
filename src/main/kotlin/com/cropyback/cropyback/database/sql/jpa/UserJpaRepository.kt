package com.cropyback.cropyback.database.sql.jpa

import com.cropyback.cropyback.database.sql.user.UserModel
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository: JpaRepository<UserModel, Long> {

    fun findUserByUsername(username: String): UserModel

    fun findByPhone(phone: String): UserModel?

    fun findByEmail(email: String): UserModel

}