package com.cropyback.cropyback.database.sql.jpa

import com.cropyback.cropyback.database.sql.user.UserModel
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository: JpaRepository<UserModel, Long> {

    fun existsByPhone(phone: String): Boolean

    fun findUserByUsername(username: String): UserModel

}