package com.cropyback.cropyback.api.reset

import com.cropyback.cropyback.database.sql.jpa.UserJpaRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class PasswordResetService(
    private val usersRepository: UserJpaRepository
){

    fun resetUserPassword(phone: String, newPassword: String) {

        val user = usersRepository.findByPhone(phone)
        if (user != null) {
            val bcrypt = BCryptPasswordEncoder()
            val hashedPassword = bcrypt.encode(newPassword)
            val updatedUser = user.copy(password = hashedPassword)
            usersRepository.save(updatedUser)
        }
    }

}